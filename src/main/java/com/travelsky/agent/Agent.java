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
	//��ǰ������set
	private static HashSet<String>  currentSet= new HashSet<String>();
	//��һ���Ѿ��ύ����set,Ϊsetȥ���ṩ�ο�
	private static HashSet <String> previousSet=new HashSet<String>();
	//��һ���������ݵ�ʱ��
	private static long previousPushTime=0l;
	//�ϴζ�ȡ���ļ����ļ���,���Ϊ����ô��ʾ�ļ��Ѿ�ȫ������ȡ��
	private static File lastReadingFile=null;
	//��ǰ��ȡ���ļ���ָ����Ϊ�´ζ�ȡ�ļ�¼��,���Ϊ-1��ô��ʾ�ϴ��ļ��Ѿ���ȡ���
	public static int cursor=-1;
	private static long timeout=0l;
	private static String downloadfilepath="";
	private static int maxSizeBeforeSplit=0;
	private static int maxlines=0;

	//�Ƿ����ݵ�����
	private static int illegalcount=0;
	//�ظ�Ԫ�صĹ������
	private static int repeatStringscount=0;

	
	static{
		timeout=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_TIME_OUT);
		downloadfilepath = PropertiesUtil.getPropertieAsString(PropertiesUtil.DOWNLOAD_LOG_PATH);
		 maxSizeBeforeSplit=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_SET_SIZE_BEFORE_SPLIT);
		 maxlines =PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_FILE_MAX_LINES);
	}

	
	/**
	 * ����set�Ĵ�С����������,����setת��ΪСset��list
	 * @return
	 * @throws IOException 
	 */
	private static List<Set> getUniqueSetList() throws IOException{
		illegalcount=0;
		repeatStringscount=0;
		//��ȡ�ϴ�δ������ļ�
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
			//�ж��ǲ����Ѿ������˱����ύ��ʱ��
			isTimeOut=System.currentTimeMillis()-previousPushTime>timeout?true:false;
			if(isTimeOut){
				message.append("there is no file in the folder "+downloadfilepath+" but timeout force commit");
				//�Ѿ���ʱ,ת��set,��������ͳ�ȥ
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
			//�ļ�������Ϊ0
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
					//�Ѿ���ʱ,ת��set,��������ͳ�ȥ
					List newList = switchSetAndCreateNewList();
					log.info(message);
					makeUpLogInfo(message.toString());
					previousPushTime=System.currentTimeMillis();
					return newList;
				}else{
					//���С��,��ô������
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
	 * ��set ת��ΪСset��list
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
		//���ʣ���
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
	 * ͨ�����õ�·����ȡҪ�õ���־�ļ��б�
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
	 * ���ļ��е����ݴ�ŵ�set��(�ļ����Ѿ���ȡ���ļ�<cursor��ֵΪ�Ѿ���ȡ��������>)
	 * ���صĽ��������
	 * 								1.�ļ���ȡ���(ɾ���ļ�)
	 * 								2.�ļ���ȡδ���(��¼�µ�ǰ����Ϣ)
	 * @param file
	 * @param set
	 * @throws IOException
	 */
	private static void readFile2Set(File file,Set<String> set) throws IOException{
		File logfile=file;
		//���ڵ�һ�ζ�ȡ��ʱ���Ѿ�ɾ���˱�־�ļ�,���Դ˴εĲ������ڵڶ��μ��Ժ�Ĳ������Թ�
		if(cursor==-1){
			//ɾ��flag�ļ�
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
			//�����ǰ����С���ϴζ���������,��ôֱ������,����Ǹ����ļ�,��ô��ͷ��ʼ��ȡ
			if(count<cursor){
				continue;
			}
			//��ȡ�ﵽ���ܶ�ȡ�����ֵ,��ô����ʣ�µ��ļ�
			if(cursor>maxlines){
				cursor=-1;
				lastReadingFile=null;
				break;
			}
			//��ȡ�����ܹ��ύ����ֵ,��ô��¼�µ�ǰ�ļ���λ�úͶ�ȡ����Ϣ,�ȴ��´ζ�ȡ
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
		//�ͷ���Դ
		reader.close();
		in.close();
		if(shouldDelete){
			cursor=-1;
			logfile.delete();
		}
	}

	/**
	 * �ж��ַ����ǲ��ǿ���
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
//			//��¼ÿһ������ĳ�����������������ڲ�ͬ��ôֱ���ж�Ϊ�Ƿ�����
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
//			//gp 2014-2-11 10:16:55 ������֤�޸�
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
	 * �޸���֤����������Ӧ���������(���������Ȼ֧��)
	 * Ϊ���ṩ�ⲿ�Ĳ���,�����Է�������
	 * @param logString
	 * @return
	 */
	public static boolean checkAvailable(String logString){
		//��֤���� ��:MF^MF8378^MF8378^NNG^TSN^null^2014-03-12 19:10:00^2014-03-12 23:55:00^-:-^738^0^1^1^XMN021
		//1.ʵ��,���˺��չ�˾���һ�µ�ʱ��,�ڶ�����Ա���Ϊ��.�ڽ�����ʱ������������ж�
		String flightReg="\\w{2}\\^\\w+\\^\\w+\\^[A-Z]{3}\\^[A-Z]{3}\\^(null|[A-Z]{3})\\^20(\\d{2}-){2}\\d{2}\\s(\\d{2}:){2}\\d{2}\\^20(\\d{2}-){2}\\d{2}\\s(\\d{2}:){2}\\d{2}\\^(-|[^^]+):(-|[^^]+)\\^\\w+\\^(0|1)\\^(0|1)\\^(0|1)\\^\\w+";
		//��֤�˼� ��:980^null^null^U^WEB03^AD
		//1.��ӶԴ�С������˼ۺ�˰�ѵ�֧��
		//2.Ĭ�Ϲ������ÿ�������AD
		//3.���ڳ�����˰���Ǹ��������,��Ϊ������ʱ�����ͨ��У��
		//4.���ڹ��ʺ������,Ϊ�˱�֤����log��֧��,���Զ��ֶν������⴦��
		String fareReg="(null|-?\\d+(\\.\\d+)?)\\^(null|-?\\d+(\\.\\d+)?)\\^(null|-?\\d+(\\.\\d+)?)\\^(null|\\w+)\\^(null|[^^]*)\\^(AD)(\\^(null|1|0))?";
		Pattern farePattern = Pattern.compile(fareReg);
		Pattern flightPattern = Pattern.compile(flightReg);
		String tempFlightString;
		String tempFareString;
		int index =logString.indexOf("MSI");
		//�˲������ؿհ״�ӡ
		if(!logString.contains("^")){
			return false;
		}
		logString =logString.substring(index+25,logString.length());
		String[] fareFlights = logString.split("=");
		for(int i=0;i<fareFlights.length;i++){
			String fareFlight = fareFlights[i];
			String[] flightsAndFares = fareFlight.split("&");
			//��Ϊ�˼ۺͺ����Ƿֿ�����ŵ����Ա�Ȼ��һ�����˼�һ���Ǻ���
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
	
	
	//ת������set,��������һ���µ�list<�˲���Ὣ�ַ���ֱ��ת��Ϊjavabean>
	private static List switchSetAndCreateNewList(){
		log.info("before swith  currentSet size:"+currentSet.size()+"       previousSet size:"+previousSet.size());
		if(currentSet.size()==0){
			return null;
		}
		Set<List> cacheSetOfFareFlightInfo = new HashSet<List>();
		int maxSize=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_SET_SIZE_AFTER_SPLIT);
		int currentsize = currentSet.size();
		//�����һ��
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
	 * ��Ӧ�ĸ���ĺ���
	 * ���չ�˾������
	 * ʵ�ʳ��˺��չ�˾
	 * ������������
	 * �����������
	 * ��ת����������
	 * 
	 */
	
	
	
	
//	��ȡ��ǰ����Ŀ¼�µ��ļ� 4test
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
