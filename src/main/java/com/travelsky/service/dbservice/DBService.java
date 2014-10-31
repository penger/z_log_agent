package com.travelsky.service.dbservice;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.domain.InputInfo;
import com.travelsky.framework.ManagerFactory;
import com.travelsky.manager.IFareFlightManager;

/**
 * 用于得到数据库中的mixMap
 * 已经数据的对比处理
 * @author gongp
 */
public class DBService implements IDBService{
	
	private  static final Log log = LogFactory.getLog(DBService.class);

	
	
	
	
//	//如果不相等,那么首先是删除已经存在的那些数据(包括flightinfo和fareinfo)
//	public void deleteByInput(InputInfo inputinfo) {
//		ManagerFactory.getFlightFareManager().deleteByInput(inputinfo);
//	}
//
//
//	@Override
//	public List <FlightFareInfo> querySortedListFromLocalDB(InputInfo inputinfo) {
//		List<FlightFareInfo> listFromLocalDB = ManagerFactory.getFlightFareManager().querySortedListFromLocalDB(inputinfo);
//		return listFromLocalDB;
//	}
//
//
//	@Override
//	public void addSortedList2DB(List<FlightFareInfo> sortedList) {
//		ManagerFactory.getFlightFareManager().addSortedList2DB(sortedList);
//	}


	@Override
	public void addSortedList2DB2(List<FareFlightInfo> sortedList) {
		IFareFlightManager fareFlightManager = ManagerFactory.getFareFlightManager();
		fareFlightManager.addSortedList2DB(sortedList);
		
	}


	@Override
	public List<FareFlightInfo> querySortedListFromLocalDB2(InputInfo inputinfo) {
		List<FareFlightInfo> sortedListFromLocalDB = ManagerFactory.getFareFlightManager().querySortedListFromLocalDB(inputinfo);
		if(sortedListFromLocalDB==null||sortedListFromLocalDB.size()==0){
			return null;
		}
		Collections.sort(sortedListFromLocalDB);
		return sortedListFromLocalDB;
	}


	@Override
	public void deleteByInput2(InputInfo inputinfo) {
		IFareFlightManager fareFlightManager = ManagerFactory.getFareFlightManager();
		fareFlightManager.deleteByInput(inputinfo);
	}


	@Override
	public FlightInfo queryFlightInfoById(String flightInfoId) {
		IFareFlightManager fareFlightManager = ManagerFactory.getFareFlightManager();
		FlightInfo flightInfo =fareFlightManager.queryFlightInfoById(flightInfoId);
		return flightInfo;
		
	}


	@Override
	public void deleteByDate(String date) {
		IFareFlightManager fareFlightManager = ManagerFactory.getFareFlightManager();
		fareFlightManager.deleteByDate(date);
		
	}


	

	
}
