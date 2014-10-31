package com.travelsky.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.MsiLog;
import com.travelsky.framework.CacheMsiLogSet;
import com.travelsky.framework.ManagerFactory;
import com.travelsky.framework.util.PropertiesUtil;
import com.travelsky.service.logservice.ILogService;

public class Agent implements IAgent{
	
	private static Log log = LogFactory.getLog(Agent.class);
	//当前操作的set
	private static HashSet<String>  currentSet= new HashSet<String>();
	//上一次已经提交过的set,为set去重提供参考
	private static HashSet <String> previousSet=new HashSet<String>();
	//上一次推送数据的时间
	private static long previousPushTime=0l;
	//上次读取的文件的文件名,如果为空那么表示文件已经全部被读取完
	private static File lastReadingFile=null;
	//当前读取的文件的指针作为下次读取的记录器,如果为-1那么表示上次文件已经读取完成
	public static int cursor=-1;
	private static long timeout=0l;
	private static String downloadfilepath="";
	private static int maxSizeBeforeSplit=0;
	private static int maxlines=0;

	//非法数据的总量
	private static int illegalcount=0;
	//重复元素的过滤情况
	private static int repeatStringscount=0;

	
	static{
		timeout=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_TIME_OUT);
		downloadfilepath = PropertiesUtil.getPropertieAsString(PropertiesUtil.DOWNLOAD_LOG_PATH);
		 maxSizeBeforeSplit=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_SET_SIZE_BEFORE_SPLIT);
		 maxlines =PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_FILE_MAX_LINES);
	}

	
	/**
	 * 根据set的大小来推送数据,将大set转换为小set的list
	 * @return
	 * @throws IOException 
	 */
	private static List<Set> getUniqueSetList() throws IOException{
		illegalcount=0;
		repeatStringscount=0;
		//读取上次未读完的文件
		if(cursor!=-1){
			readFile2Set(lastReadingFile, currentSet);
			if(currentSet.size()>=maxSizeBeforeSplit){
				log.info("read unfinshed file reached :"+maxSizeBeforeSplit);
				List newList = switchSetAndCreateNewList();
				return newList;
			}
		}
		
		StringBuffer message=new StringBuffer();
		boolean isTimeOut=false;

		List<File> files = getFileList(downloadfilepath);
		if(files.size()==0){
			//判断是不是已经超过了必须提交的时间
			isTimeOut=System.currentTimeMillis()-previousPushTime>timeout?true:false;
			if(isTimeOut){
				message.append("there is no file in the folder "+downloadfilepath+" but timeout force commit");
				//已经超时,转换set,将结果推送出去
				List newList = switchSetAndCreateNewList();
				log.info(message);
				makeUpLogInfo(message.toString());
				previousPushTime=System.currentTimeMillis();
				return newList;
			}else{
				message.append("there is no file in the folder"+downloadfilepath+" not timeout mission canceled");
				log.info(message);
				return null;
			}
		}else{
			//文件数量不为0
			message.append(" file size is :"+files.size());
			while(currentSet.size()<maxSizeBeforeSplit){
				if(files.size()==0){
					break;
				}
				File file = files.remove(0);
				message.append("read "+file.getName());
				readFile2Set(file,currentSet);
			}
			if(currentSet.size()<maxSizeBeforeSplit){
				isTimeOut=System.currentTimeMillis()-previousPushTime>timeout?true:false;
				if(isTimeOut){
					message.append(" after read currentSet size is"+currentSet.size()+"less than "+maxSizeBeforeSplit+" but timeout force commit");
					//已经超时,转换set,将结果推送出去
					List newList = switchSetAndCreateNewList();
					log.info(message);
					makeUpLogInfo(message.toString());
					previousPushTime=System.currentTimeMillis();
					return newList;
				}else{
					//如果小于,那么不推送
					message.append("currentSet size is"+currentSet.size()+"less than "+maxSizeBeforeSplit+"mission canceled");
					log.info(message);
				}
				return null;
			}else{
				message.append(" currentSet size is"+currentSet.size()+"more than "+maxSizeBeforeSplit+"commit");
				List newList = switchSetAndCreateNewList();
				log.info(message);
				makeUpLogInfo(message.toString());
				return newList;
			}
		}
	}



	
	/**
	 * 将set 转换为小set的list
	 * @param size
	 * @param bigSet
	 * @return
	 */
	private static List<Set> splitBigSet2TinySetWithSize(int size,Set bigSet){
		log.info("bigset's size is :"+bigSet.size());
		List<Set> tinysets=new ArrayList();
		Iterator it = bigSet.iterator();
		int count=0;
		Set set =new HashSet();
		while(it.hasNext()){
			if(count==size){
				if(set!=null){
					tinysets.add(set);
				}
				set=new HashSet();
				count=0;
			}
			set.add(it.next());
			count++;
		}
		//添加剩余的
		if(count!=0){
			tinysets.add(set);
		}
		log.info("tinysets's size is :"+tinysets.size());
		return tinysets;
	}
	
//	public Queue getUniqueQueue(){
//		
//	}
	
	/**
	 * 通过配置的路径获取要得到标志文件列表
	 * @param dir
	 * @return
	 */
	private static List<File> getFileList(String dir){
		File f= new File(dir);
		File[] files = f.listFiles();
		List<File> trueFiles =new ArrayList<File>();
		for(File file: files){
			if(file.getAbsolutePath().contains("flag")){
				trueFiles.add(file);
			}
		}
		return trueFiles;
	}

	/**
	 * 将文件中的数据存放到set中(文件有已经读取的文件<cursor的值为已经读取过的行数>)
	 * 返回的结果有两种
	 * 								1.文件读取完成(删除文件)
	 * 								2.文件读取未完成(记录下当前的信息)
	 * @param file
	 * @param set
	 * @throws IOException
	 */
	private static void readFile2Set(File file,Set<String> set) throws IOException{
		File logfile=file;
		//由于第一次读取的时候已经删除了标志文件,所以此次的操作会在第二次及以后的操作中略过
		if(cursor==-1){
			//删除flag文件
			file.delete();
			logfile = new File(file.getAbsolutePath().replace("flag", "log"));
			log.info("read "+logfile.getAbsolutePath()+" into set there are "+currentSet.size()+" in the set");
		}
		String tempstring="";
		FileInputStream in=null;
		BufferedReader reader=null;
		String truestring = null;
		in = new FileInputStream(logfile);
		reader = new BufferedReader(new InputStreamReader(in));
		int count=0;
		boolean shouldDelete=true;
		while((tempstring=reader.readLine())!= null){
			count++;
			//如果当前行数小于上次读到的行数,那么直接跳过,如果是个新文件,那么从头开始读取
			if(count<cursor){
				continue;
			}
			//读取达到了能读取的最大值,那么丢弃剩下的文件
			if(cursor>maxlines){
				cursor=-1;
				lastReadingFile=null;
				break;
			}
			//读取到了能够提交的阈值,那么记录下当前文件的位置和读取的信息,等待下次读取
			if(set.size()>=maxSizeBeforeSplit){
				lastReadingFile=logfile;
				cursor=count;
				shouldDelete=false;
				log.info("read file "+lastReadingFile.getAbsolutePath()+" current cursor is : "+cursor+"currentSet size is: "+set.size());
				break;
			}
			boolean isAvaliable = checkAvailable(tempstring);
			if(isAvaliable){
				truestring  = tempstring.substring(tempstring.indexOf("MSI")+25);
				if(set.contains(truestring)||previousSet.contains(truestring)){
					repeatStringscount++;
				}else{
					set.add(truestring);
				}
			}else{
				illegalcount++;
			}
		}
		//释放资源
		reader.close();
		in.close();
		if(shouldDelete){
			cursor=-1;
			logfile.delete();
		}
	}

	/**
	 * 判断字符串是不是可用
	 * @param logstring
	 * @return
	 */
//	public static boolean  checkAvailable(String logstring){
//		int index = logstring.indexOf("MSI");
//		if (index <0){
//			return false;
//		}
//		logstring = logstring.substring(logstring.indexOf("MSI"));
//		String testlogstring;
//		try {
//			testlogstring = logstring.substring(25,logstring.length());
//			String[] logtemp=testlogstring.split("\\=");
//			//记录每一个航班的出发日期如果出发日期不同那么直接判定为非法数据
//			String oldDate="";
//		for(int k=0;k<logtemp.length;k++){
//			String[] split = logtemp[k].split("\\^");
//			if(oldDate.equals("")){
//				oldDate=split[6].substring(0,10);
//			}else{
//				String newDate=split[6].substring(0,10);
//				if(!oldDate.equals(newDate)){
//					log.error(oldDate+"!="+newDate+" detail string is "+logstring);
//					return false;
//				}
//			}
//			//gp 2014-2-11 10:16:55 正则验证修改
//			String regexp="\\w{2}\\^\\w+\\^\\w+\\^[A-Z]{3}\\^[A-Z]{3}\\^(null|[A-Z]{3})\\^20(\\d{2}-){2}\\d{2}\\s(\\d{2}:){2}\\d{2}\\^20(\\d{2}-){2}\\d{2}\\s(\\d{2}:){2}\\d{2}\\^(-|[^^]+):(-|[^^]+)\\^\\w+\\^(0|1)\\^(0|1)\\^(0|1)\\^\\w+\\&(null|-?\\d+(\\.\\d+)?)\\^(null|-?\\d+(\\.\\d+)?)\\^(null|-?\\d+(\\.\\d+)?)\\^(null|\\w+)\\^(null|[^^]*)\\^(AD)";
//			Pattern p = Pattern.compile(regexp);
//			Matcher m = p.matcher(logtemp[k]);
//			boolean b = m.matches();
//			if(b==false){
//				log.info("LOG  String :"+logstring+" format error");
//				return false;
//			}
//		}
//		return true;
//		} catch (Exception e) {
//			log.info("LOG  String :"+logstring+" format error");
//			return false;
//		}
//	}
	
	/**
	 * 修改验证规则用以适应往返的情况(单程情况依然支持)
	 * 为了提供外部的测试,将测试方法公开
	 * @param logString
	 * @return
	 */
	public static boolean checkAvailable(String logString){
		//验证航线 例:MF^MF8378^MF8378^NNG^TSN^null^2014-03-12 19:10:00^2014-03-12 23:55:00^-:-^738^0^1^1^XMN021
		//1.实际,承运航空公司如果一致的时候,第二项可以保持为空.在解析的时候会自主进行判断
		String flightReg="\\w{2}\\^\\w+\\^\\w+\\^[A-Z]{3}\\^[A-Z]{3}\\^(null|[A-Z]{3})\\^20(\\d{2}-){2}\\d{2}\\s(\\d{2}:){2}\\d{2}\\^20(\\d{2}-){2}\\d{2}\\s(\\d{2}:){2}\\d{2}\\^(-|[^^]+):(-|[^^]+)\\^\\w+\\^(0|1)\\^(0|1)\\^(0|1)\\^\\w+";
		//验证运价 例:980^null^null^U^WEB03^AD
		//1.添加对带小数点的运价和税费的支持
		//2.默认规则下旅客类型是AD
		//3.由于出现了税费是负数的情况,当为负数的时候继续通过校验
		//4.对于国际航班情况,为了保证对老log的支持,所以对字段进行特殊处理
		String fareReg="(null|-?\\d+(\\.\\d+)?)\\^(null|-?\\d+(\\.\\d+)?)\\^(null|-?\\d+(\\.\\d+)?)\\^(null|\\w+)\\^(null|[^^]*)\\^(AD)(\\^(null|1|0))?";
		Pattern farePattern = Pattern.compile(fareReg);
		Pattern flightPattern = Pattern.compile(flightReg);
		String tempFlightString;
		String tempFareString;
		int index =logString.indexOf("MSI");
		//此步骤拦截空白打印
		if(!logString.contains("^")){
			return false;
		}
		logString =logString.substring(index+25,logString.length());
		String[] fareFlights = logString.split("=");
		for(int i=0;i<fareFlights.length;i++){
			String fareFlight = fareFlights[i];
			String[] flightsAndFares = fareFlight.split("&");
			//因为运价和航线是分开来存放的所以必然有一个是运价一个是航线
			if(flightsAndFares.length!=2){
				return false;
			}else{
				String[] flights= flightsAndFares[0].split("%");
				String[] fares=flightsAndFares[1].split("%");
				for(int j=0;j<flights.length;j++){
					tempFlightString=flights[j];
					boolean b = flightPattern.matcher(tempFlightString).matches();
					if(!b){
						return false;
					}
				}
				for(int j=0;j<fares.length;j++){
					tempFareString=fares[j];
					boolean b = farePattern.matcher(tempFareString).matches();
					if(!b){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	//转换两个set,并且生成一个新的list<此步骤会将字符串直接转换为javabean>
	private static List switchSetAndCreateNewList(){
		log.info("before swith  currentSet size:"+currentSet.size()+"       previousSet size:"+previousSet.size());
		if(currentSet.size()==0){
			return null;
		}
		Set<List> cacheSetOfFareFlightInfo = new HashSet<List>();
		int maxSize=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_SET_SIZE_AFTER_SPLIT);
		int currentsize = currentSet.size();
		//清空上一个
		previousSet=(HashSet<String>) currentSet.clone();
		Iterator<String> iterator = currentSet.iterator();
		ILogService logService = ManagerFactory.getlogService();
		while(iterator.hasNext()){
			List<FareFlightInfo> sortedListFromLog = logService.getSortedListFromLog(iterator.next());
			cacheSetOfFareFlightInfo.add(sortedListFromLog);
		}
		currentSet.clear();
		log.info("switch after currentSet size:"+currentSet.size()+"       previousSet size:"+previousSet.size());
		int sizeAfterSplit=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_SET_SIZE_AFTER_SPLIT);
		List<Set> tinysetlist = splitBigSet2TinySetWithSize(sizeAfterSplit, cacheSetOfFareFlightInfo);
		log.info("currentSet split int"+tinysetlist.size()+" * "+sizeAfterSplit+" tiny sets");
		return tinysetlist;
	}
	
	private static void makeUpLogInfo(String message){
		 MsiLog msiLog = new MsiLog();
		 msiLog.setContent(message);
		 msiLog.setKeyWord("push data");
		 msiLog.setLev("1");
		 msiLog.setModelName("Push");
		CacheMsiLogSet.getInstance().addMsiLog2Vector(msiLog);
	}
	

	@Override
	public List<Set> provideData() {
			try {
				 return getUniqueSetList();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	}
	/**
	 * HU^HU7382^null^PEK^HAK^null^2014-03-02 08:00:00^2014-03-02 12:00:00^T1:-^787^0^0^1^HAK969&980^null^null^U^WEB03^AD
	 * MF^MF8378^MF8378^NNG^TSN^null^2014-03-12 19:10:00^2014-03-12 23:55:00^-:-^738^0^1^1^XMN021&680.0^50.0^120.0^H^H07015^AD
	 * 对应的各项的含义
	 * 航空公司两字码
	 * 实际承运航空公司
	 * 出发地三字码
	 * 到达地三字码
	 * 中转机场三字码
	 * 
	 */
	
	
	
	
//	读取当前工程目录下的文件 4test
	public static void main(String[] args) {
		
//		String kk="MSI[2013-11-27 14:35:00]^HU^HU001^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU002^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU003^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU004^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU005^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU006^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU007^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD";
//		String kk="MSI[2013-11-27 14:35:00]^SC^SC4935^SC4935^XMN^JDZ^null^2014-02-02 02:40:00^2014-02-02 03:45:00^-:-^738^0^0^1^SDH999&560^-1^70^Y^Y^AD";
//		String kk="MSI[2013-11-27 15:05:16]^HU^HU7852^null^XIY^LHW^null^2013-11-28 16:15:00^2013-11-28 15:10:00^T2:-^738^0^0^0^HAK969&210^null^null^N^NEE^AD";
//		String kk="MSI[2014-01-20 10:03:02]^SC^SC4885^SC4885^YNT^HGH^null^2014-02-08 04:20:00^2014-02-08 05:55:00^-:-^738^0^0^1^SDH999&1060^50^120^A^A/HDQ13A045^AD";
//		String kk = "MSI[2013-11-27 14:35:00]^HU^HU7382^null^PEK^HAK^null^2014-03-02 08:00:00^2014-03-02 12:00:00^T1:-^787^0^0^1^HAK969&980^null^null^U^WEB03^AD=HU^HU7182^null^PEK^HAK^null^2014-03-02 12:30:00^2014-03-02 16:20:00^T1:-^738^0^0^1^HAK969&1309^null^null^Q^WEB03^AD=HU^HU7782^null^PEK^HAK^null^2014-03-02 15:40:00^2014-03-02 19:30:00^T1:-^767^0^0^1^HAK969&980^null^null^U^WEB03^AD=HU^HU7082^null^PEK^HAK^null^2014-03-02 17:15:00^2014-03-02 21:05:00^T1:-^738^0^0^1^HAK969&873^null^null^E^WEB03^AD=HU^HU7282^null^PEK^HAK^null^2014-03-02 21:00:00^2014-03-03 00:50:00^T1:-^738^0^0^0^HAK969&680^null^null^T^Y30STJ^AD=HU^GS6574^null^PEK^HAK^null^2014-03-02 13:40:00^2014-03-02 19:15:00^T1:-^190^0^1^0^HAK969&1130^null^null^X^null^AD";
		String kk="MSI[2013-11-27 14:35:00]^MF^MF8378^MF8378^NNG^TSN^null^2014-03-12 19:10:00^2014-03-12 23:55:00^-:-^738^0^1^1^XMN021&680.5^50.5^120.5^H^H07015^AD^null";
		boolean checkAvailable = checkAvailable(kk);
		System.out.println(checkAvailable);
		
	}

	
	
}
