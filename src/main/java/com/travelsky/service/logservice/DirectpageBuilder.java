package com.travelsky.service.logservice;

import com.travelsky.domain.Config;
import com.travelsky.domain.FlightInfo;
import com.travelsky.framework.ManagerFactory;

public  class DirectpageBuilder {
	public static String build(FlightInfo flightInfo,FlightInfo flightInfo2,boolean isB2B){
		StringBuffer directpage = new StringBuffer();
		String airlineid=flightInfo.getAirlineId();
		if(airlineid.equals("HU")&&!isB2B){
			//如果是hu并且是hub2c
			directpage.append(ManagerFactory.getConfigController().getPropertyAsString("HUB2C", Config.DIRECTPAGE_HEAD));
			directpage.append("?tripType=");
			if(flightInfo2==null){
				directpage.append("ONEWAY&orgCity=");
			}else{
				directpage.append("ONEWAY&orgCity=");
			}
			directpage.append(flightInfo.getOriginlocation());
			directpage.append("&dstCity=");
			directpage.append(flightInfo.getDestinationlocation());
			directpage.append("&takeoffDate=");
			directpage.append(flightInfo.getFlightdate());
			directpage.append("&returnDate=");
			if(flightInfo2!=null){
				directpage.append(flightInfo2.getFlightdate());
			}
			directpage.append("&bookSeatClass=E&adultNum=1");
			
		}else if(airlineid.equals("MF")&&!isB2B){
			directpage.append("http://et.xiamenair.com.cn/xiamenair/SearchFlightsEPM.do");
			directpage.append("?takeoffDate=");
			directpage.append(flightInfo.getDepartingdatetime().substring(0,10));
			directpage.append("&orgCity=");
			directpage.append(flightInfo.getOriginlocation());
			directpage.append("&dstCity=");
			directpage.append(flightInfo.getDestinationlocation());
			directpage.append("&tripType=ONEWAY&cabinClass=0");
		}else if(airlineid.equals("SC")&&!isB2B){
			//scb2c
			directpage.append(ManagerFactory.getConfigController().getPropertyAsString("SCB2C", Config.DIRECTPAGE_HEAD));
			directpage.append("?cityCodeOrg=");
			directpage.append(flightInfo.getOriginlocation());
			directpage.append("&cityCodeDes=");
			directpage.append(flightInfo.getDestinationlocation());
			directpage.append("&cityNameOrg=&cityNameDes=");
			directpage.append("&takeoffDate=");
			directpage.append(flightInfo.getFlightdate());
			directpage.append("&returnDate=&travelType=0&contrytype=0&adultNum=1&childNum=0");
		}else{
			return null;
		}
		return directpage.toString();
	}
}
