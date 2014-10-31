package com.travelsky.service.dbservice;

import java.util.List;

import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.domain.InputInfo;

public interface IDBService {
	//从数据库中得到mixMap
//	public Map getMixMapFromLocalDB(InputInfo inputinfo);
	//从数据库中得到cachedbeanlist
//	public CachedBeanList getCachedBeanListByInputInfo(InputInfo inputinfo);
	//将得到的结果存放到数据库中
//	public void addMixMap2DB(Map map);
	
	
	
//	//从数据库中得到已经排序过的list
//	public List <FlightFareInfo> querySortedListFromLocalDB(InputInfo inputinfo);
//	//根据input删除原有的数据
//	public void deleteByInput(InputInfo inputinfo);
//	//将得到的List形式的结果存放到数据库中
//	public void addSortedList2DB(List <FlightFareInfo> sortedList);
	
	
	
	public List <FareFlightInfo> querySortedListFromLocalDB2(InputInfo inputinfo);
	public void addSortedList2DB2(List <FareFlightInfo> sortedList);
	public void deleteByInput2(InputInfo inputinfo);
	public FlightInfo queryFlightInfoById(String tempid);
	public void deleteByDate(String date);
	
	
}
