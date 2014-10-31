package com.travelsky.service.logservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.FareFlight;
import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FareInfo;
import com.travelsky.domain.FlightInfo;


/**
 * 与log相关的业务逻辑处理
 * @author gongp
 */
public class LogService implements ILogService {
	
	private static Log log = LogFactory.getLog(LogService.class);


	/**
	 * 通过log来得到要往缓存中存放的bean
	 * <单程直达的情况>
	 * <往返的情况>
	 * <打包价的情况>
	 * @throws Exception 
	 */
	
	public List<FareFlightInfo> getSortedListFromLog(String logString){
		List<FareFlightInfo> list = new ArrayList<FareFlightInfo>();
		String[] primaryInfo = logString.split("\\=");
		for(String s:primaryInfo){
			try {
				FareFlightInfo fareFlightInfo = getFlightFareInfoFromPrimaryStr(s);
				list.add(fareFlightInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(list);
		return list;
	}
	
	
	/**
	 * 
	 * @param primaryElement
	 * @return
	 * @throws Exception
	 */
	private static FareFlightInfo getFlightFareInfoFromPrimaryStr(String primaryElement) throws Exception{
		FareFlightInfo fareFlightInfo = new FareFlightInfo();
		String[] routs = primaryElement.split("&");
		if(routs.length!=2){
			throw new Exception("routs size should be 2");
		}else{
			String[] flightInfos=routs[0].split("%");
			String[] fareInfos=routs[1].split("%");
			FareInfo fareInfo=null;
			FareInfo fareInfo2=null;
			FlightInfo flightInfo=null;
			FlightInfo flightInfo2=null;
//			FareFlight fareFlight=new FareFlight();
			//fareInfo 和flightInfo关联的关键key为:  1,Y,2014-06-22CA1294HTNURC
			//由于officecode是在flightinfo中,所以只能在解析flightinfo的时候提起flightinfo
			//同时由于Stiring为值传递,所以只能用StringBuffer作为替代方案 2014-3-6 10:39:07
			StringBuffer officeCode=new StringBuffer();
			StringBuffer isinter=new StringBuffer();
			if(flightInfos.length==1&&fareInfos.length==1){
				//单程直达的情况
				flightInfo = resolve4FlightInfo(flightInfos[0],officeCode);
				fareInfo = resolve4FareInfo(fareInfos[0],isinter);
			}else if(flightInfos.length==2&&fareInfos.length==2){
				//往返非打包价的情况
				flightInfo = resolve4FlightInfo(flightInfos[0],officeCode);
				fareInfo = resolve4FareInfo(fareInfos[0],isinter);
				//清空officeCode否则出现两个officeCode
				isinter.setLength(0);
				officeCode.setLength(0);
				flightInfo2 = resolve4FlightInfo(flightInfos[1],officeCode);
				flightInfo2.setSubid("2");
				fareInfo2 = resolve4FareInfo(fareInfos[1],isinter);
			}else if(flightInfos.length==2&&fareInfos.length==1){
				//打包价的情况
				flightInfo = resolve4FlightInfo(flightInfos[0],officeCode);
				officeCode.setLength(0);
				isinter.setLength(0);
				flightInfo2 = resolve4FlightInfo(flightInfos[1],officeCode);
				fareInfo = resolve4FareInfo(fareInfos[0],isinter);
			}else{
				log.error("check string :"+primaryElement+ " for detail this should be an error !");
			}
			FareFlight fareFlight = padFareFlight(flightInfo, flightInfo2, fareInfo, fareInfo2,officeCode,isinter);
			fareFlightInfo.setFareFlight(fareFlight);
			fareFlightInfo.getFareInfoList().add(fareInfo);
			fareFlightInfo.getFlightInfoList().add(flightInfo);
			if(fareInfo2!=null){
				fareFlightInfo.getFareInfoList().add(fareInfo2);
			}
			if(flightInfo2!=null){
				fareFlightInfo.getFlightInfoList().add(flightInfo2);
			}
		}
		return fareFlightInfo;
	}
	
	/**
	 * 
	 * @param fl
	 * @param fl2
	 * @param fr
	 * @param fr2
	 */
	private static FareFlight padFareFlight(FlightInfo fl,FlightInfo fl2,FareInfo fr,FareInfo fr2,StringBuffer officeCode,StringBuffer isinter){
		FareFlight fareFlight = new FareFlight();
		fareFlight.setAirlineId(fl.getAirlineId());
		fareFlight.setOri(fl.getOriginlocation());
		fareFlight.setDst(fl.getDestinationlocation());
		fareFlight.setFlightDate(fl.getFlightdate());
		fareFlight.setOfficecode(officeCode.toString());
		fareFlight.setIsinter(isinter.toString());
		fareFlight.setInfofrom("0");
		fareFlight.setIsb2b("0");
		fareFlight.setTotalPrice(fr.getFare()+(fr2==null?0:fr2.getFare()));
		Double tax=(fr.getCntax()==null?0:fr.getCntax())+(fr.getYqtax()==null?0:fr.getYqtax());
		Double tax2=fr2==null?0:((fr.getCntax()==null?0:fr.getCntax())+(fr.getYqtax()==null?0:fr.getYqtax()));
		fareFlight.setTotalTax(tax+tax2);
		String directpage=DirectpageBuilder.build(fl, fl2,false);
		fareFlight.setDirectpage(directpage);
		StringBuffer sb=new StringBuffer();
		if(fl2==null){
			//1,Y,2014-06-22CA1294HTNURC
			sb.append("1,");
			sb.append(fr.getCabin());
			sb.append(",");
			sb.append(fl.getFlightdate());
			sb.append(fl.getMarketingflightnumber());
			sb.append(fl.getOriginlocation());
			sb.append(fl.getDestinationlocation());
			fr.setFlightinfoKey(sb.toString());
		}else{
			if(fr2==null){
				//打包价
				sb.append("1,");
				sb.append(fr.getCabin());
				sb.append(",");
				sb.append(fl.getFlightdate());
				sb.append(fl.getMarketingflightnumber());
				sb.append(fl.getOriginlocation());
				sb.append(fl.getDestinationlocation());
				sb.append(";2,");
				sb.append(fr.getCabin());
				sb.append(",");
				sb.append(fl2.getFlightdate());
				sb.append(fl2.getMarketingflightnumber());
				sb.append(fl2.getOriginlocation());
				sb.append(fl2.getDestinationlocation());
				fr.setFlightinfoKey(sb.toString());
				fareFlight.setReturnDate(fl2.getFlightdate());
			}else{
				//往返,但是相当于两个单程的组合
				sb.append("1,");
				sb.append(fr.getCabin());
				sb.append(",");
				sb.append(fl.getFlightdate());
				sb.append(fl.getMarketingflightnumber());
				sb.append(fl.getOriginlocation());
				sb.append(fl.getDestinationlocation());
				fr.setFlightinfoKey(sb.toString());
				sb.delete(0, sb.length());
				sb.append("1,");
				sb.append(fr2.getCabin());
				sb.append(",");
				sb.append(fl2.getFlightdate());
				sb.append(fl2.getMarketingflightnumber());
				sb.append(fl2.getOriginlocation());
				sb.append(fl2.getDestinationlocation());
				fr2.setFlightinfoKey(sb.toString());
				fareFlight.setReturnDate(fl2.getFlightdate());
			}
		}
		return fareFlight;
	}
	
	
	private static FlightInfo resolve4FlightInfo(String flightInfoLog,StringBuffer officeCode){
		String[] flightitem = flightInfoLog.split("\\^");
		officeCode.append(flightitem[13]);
		FlightInfo flightInfo = new FlightInfo();
		StringBuffer id = new StringBuffer();
//	System.out.println(flightitem[6]);
		id.append(flightitem[6].substring(0,10));
		id.append(flightitem[1]);
		id.append(flightitem[3]);
		id.append(flightitem[4]);
		
		flightInfo.setFlightinfoId(id.toString());
		flightInfo.setAirlineId(flightitem[0]);
		flightInfo.setMarketingflightnumber(flightitem[1]);
		//如果相等或者为NULL 那么就不设置operatingflightnumber
		if(!(flightitem[1].equals(flightitem[2])||flightitem[2].equals("null"))){
			flightInfo.setOperatingflightnumber(flightitem[2]);
		}
		flightInfo.setOriginlocation(flightitem[3]);
		flightInfo.setDestinationlocation(flightitem[4]);
		flightInfo.setDepartingdatetime(flightitem[6]);
		flightInfo.setArrivalingdatetime(flightitem[7]);
		flightInfo.setFlightdate(flightInfo.getDepartingdatetime().substring(0,10));
		flightInfo.setTerminals(flightitem[8]);
		flightInfo.setEquipment(flightitem[9]);
		flightInfo.setMealcode(flightitem[12]);
		flightInfo.setStops(flightitem[11]);
		flightInfo.setSubid("1");
		return flightInfo;
	}
	
	
	
	private static FareInfo resolve4FareInfo(String fareInfoLog,StringBuffer isinter){
		String[] fareitem = fareInfoLog.split("\\^");
		FareInfo fareInfo = new FareInfo();
		fareInfo.setFare(Double.valueOf(fareitem[0]));
		fareInfo.setCntax(Double.valueOf(fareitem[1].equals("null")?"0":fareitem[1]));
		fareInfo.setYqtax(Double.valueOf(fareitem[2].equals("null")?"0":fareitem[2]));
		fareInfo.setCabin(fareitem[3].equals("null")?"0":fareitem[3]);
		//由于在日志中发现很多 farebasis 中null 和"" 的判断结果为false ,但是这个逻辑是不正确的.故改进 2014-1-23 15:56:06 gp
		if(!fareitem[4].equals("null")){
			fareInfo.setFarebasis(fareitem[4]);
		}
		fareInfo.setPtc(fareitem[5]);
		fareInfo.setSubid("1");
		//如果没有设置国际票,那么默认是国内票
		if(fareitem.length==6	){
				isinter.append("0");
			}else{
				if(!fareitem[6].equals("null")){
				isinter.append(fareitem[6]);
			}else{
				isinter.append("0");
			}
		}
		return fareInfo;
	}
	
	
	
	
//	public List<FareFlightInfo> getSortedListFromLog(String logstring) {
//		List <FareFlightInfo> list = new ArrayList<FareFlightInfo>();
//		try {
//			String[] p=logstring.split("\\=");
//			//p[0] like "SC^SC4935^SC4935^XMN^JDZ^null^2014-02-02 02:40:00^2014-02-02 03:45:00^-:-^738^0^0^1^SDH999&560^50^70^Y^Y^AD"
//			//p代表的是一段航程 可能是1:2 可能是1:1 -----------由于是日志,只考虑这两种情况
//			for(int i=0;i<p.length;i++){
//				FareFlightInfo fareFlightInfo = new FareFlightInfo();
//				FareFlight fareFlight = new FareFlight();
//				String rout= p[i];
//				String[] totle = rout.split("&");
//				String[] flightitem = totle[0].split("\\^");
//				//flightitem[0] like "SC^SC4935^SC4935^XMN^JDZ^null^2014-02-02 02:40:00^2014-02-02 03:45:00^-:-^738^0^0^1^SDH999"
//				String[] fareitem = totle[1].split("\\^");
//				//fareitem[0] like "560^50^70^Y^Y^AD"
//				FlightInfo flightinfo1 = new FlightInfo();
//				FareInfo fareinfo  = new FareInfo();
//	
//				//联程数据
//				//***********************flight 拼装开始*********************************
//				if(flightitem[0].contains("%")){
//					//暂略
//				}else{
//					//根据航班的具体信息设置
//					StringBuffer id = new StringBuffer();
////					System.out.println(flightitem[6]);
//					id.append(flightitem[6].substring(0,10));
//					id.append(flightitem[1]);
//					id.append(flightitem[3]);
//					id.append(flightitem[4]);
//					
//					flightinfo1.setFlightinfoId(id.toString());
//					flightinfo1.setAirlineId(flightitem[0]);
//					flightinfo1.setMarketingflightnumber(flightitem[1]);
//					//如果相等或者为NULL 那么就不设置operatingflightnumber
//					if(!(flightitem[1].equals(flightitem[2])||flightitem[2].equals("null"))){
//						flightinfo1.setOperatingflightnumber(flightitem[2]);
//					}
//					flightinfo1.setOriginlocation(flightitem[3]);
//					flightinfo1.setDestinationlocation(flightitem[4]);
//					flightinfo1.setDepartingdatetime(flightitem[6]);
//					flightinfo1.setArrivalingdatetime(flightitem[7]);
//					flightinfo1.setFlightdate(flightinfo1.getDepartingdatetime().substring(0,10));
//					flightinfo1.setTerminals(flightitem[8]);
//					flightinfo1.setEquipment(flightitem[9]);
//					flightinfo1.setMealcode(flightitem[12]);
//					flightinfo1.setStops(flightitem[11]);
//					flightinfo1.setSubid("1");
//					fareFlightInfo.getFlightInfoList().add(flightinfo1);
//					//获取这个航班对应的真实日期,作为冗余数据存在
//				}
//				//***********************flight 拼装结束*********************************
//				//***********************fare 拼装开始*********************************		
////				fareinfo.setFareinfoId(fareinfoId);
//				fareinfo.setFare(Double.valueOf(fareitem[0]));
//				fareinfo.setCntax(Double.valueOf(fareitem[1].equals("null")?"0":fareitem[1]));
//				fareinfo.setYqtax(Double.valueOf(fareitem[2].equals("null")?"0":fareitem[2]));
//				fareinfo.setCabin(fareitem[3].equals("null")?"0":fareitem[3]);
//				//由于在日志中发现很多 farebasis 中null 和"" 的判断结果为false ,但是这个逻辑是不正确的.故改进 2014-1-23 15:56:06 gp
//				if(!fareitem[4].equals("null")){
//					fareinfo.setFarebasis(fareitem[4]);
//				}
////				fareinfo.setFarebasis(fareitem[4].equals("null")?"":fareitem[4]);
////				fareinfo.setFareFlightId(fareFlightId);
//				FlightInfo tempFlightInfo = null;
//				StringBuffer flightDetails = new StringBuffer();
//				for(int j=0;j<fareFlightInfo.getFlightInfoList().size();j++){
//					tempFlightInfo=fareFlightInfo.getFlightInfoList().get(j);
//					if(j!=0){
//						flightDetails.append(";");
//					}
//					flightDetails.append("1");
//					flightDetails.append(",");
//					flightDetails.append(fareinfo.getCabin());
//					flightDetails.append(",");
//					flightDetails.append(tempFlightInfo.getFlightdate());
//					flightDetails.append(tempFlightInfo.getMarketingflightnumber());
//					flightDetails.append(tempFlightInfo.getOriginlocation());
//					flightDetails.append(tempFlightInfo.getDestinationlocation());
//				}
//				fareinfo.setFlightinfoKey(flightDetails.toString());
//				fareinfo.setPtc(fareitem[5]);
//				fareinfo.setSubid("1");
//				fareFlightInfo.getFareInfoList().add(fareinfo);
//				//***********************fare 拼装结束*********************************
//				//***********************fareflight拼装开始****************************
//				if(flightitem[0].contains("%")){
//					//暂略
//				}else{
//					fareFlight.setAirlineId(flightitem[0]);
//					fareFlight.setOri(flightitem[3]);
//					fareFlight.setDst(flightitem[4]);
//					fareFlight.setFlightDate(flightinfo1.getDepartingdatetime().substring(0,10));
//					fareFlight.setOfficecode(flightitem[13]);
//				}
//				fareFlight.setInfofrom("0");
//				fareFlight.setIsb2b("0");
//				fareFlight.setTotalPrice(Double.valueOf(fareitem[0]));
//				Double totalTax=0d;
//				for(int t=0;t<fareFlightInfo.getFareInfoList().size();t++){
//					totalTax+=fareFlightInfo.getFareInfoList().get(t).getCntax();
//					totalTax+=fareFlightInfo.getFareInfoList().get(t).getYqtax();
//				}
//				fareFlight.setTotalTax(totalTax);
////				StringBuffer directpage = new StringBuffer();
//				String directpage=DirectpageBuilder.build(flightinfo1, false);
//				fareFlight.setDirectpage(directpage);
//				//***********************fareflight拼装结束****************************	
//				fareFlightInfo.setFareFlight(fareFlight);
//				list.add(fareFlightInfo);
//			}
//			Collections.sort(list);
//		} catch (LogConfigurationException e) {
//			e.printStackTrace();
//		}
//		return list;
//	
//	
//	}
	
	//测试用!
	public static void main(String[] args) throws Exception {
		String filepath="d:/test/a/a.txt";
		File file=new File(filepath);
		if(!file.exists()){
			return ;
		}
		LogService logService = new LogService();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String tempLogString;
		while((tempLogString=reader.readLine())!=null){
			tempLogString = tempLogString.substring(tempLogString.indexOf("MSI")+25);
			System.out.println("当前读取的字符串为:"+tempLogString);
			List<FareFlightInfo> list = logService.getSortedListFromLog(tempLogString);
			for(FareFlightInfo f:list){
				System.out.println(f);
			}
		}
		String kk="HU^HU001^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU002^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU003^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU004^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU005^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU006^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD=HU^HU007^null^BBC^BBC^null^2014-01-12 11:02:00^2014-01-12 08:45:02^T1:-^333^0^0^1^HAK969&1300^null^null^K^AD";
	}
}
