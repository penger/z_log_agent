package com.travelsky.manager.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.dao.impl.FareFlightDaoImpl;
import com.travelsky.dao.impl.FareInfoDaoImpl;
import com.travelsky.dao.impl.FlightInfoDaoImpl;
import com.travelsky.domain.FareFlight;
import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FareInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.domain.InputInfo;
import com.travelsky.framework.util.BeanUtil;
import com.travelsky.manager.IFareFlightManager;

public class FareFlightManagerImpl implements IFareFlightManager {
	
	private static final Log log = LogFactory.getLog(FareFlightManagerImpl.class);
	
	private FlightInfoDaoImpl flightInfoDao;
	private FareInfoDaoImpl fareInfoDao;
	private FareFlightDaoImpl fareFlightDao;

	
	public FlightInfoDaoImpl getFlightInfoDao() {
		return flightInfoDao;
	}

	public void setFlightInfoDao(FlightInfoDaoImpl flightInfoDao) {
		this.flightInfoDao = flightInfoDao;
	}

	public FareInfoDaoImpl getFareInfoDao() {
		return fareInfoDao;
	}

	public void setFareInfoDao(FareInfoDaoImpl fareInfoDao) {
		this.fareInfoDao = fareInfoDao;
	}

	public FareFlightDaoImpl getFareFlightDao() {
		return fareFlightDao;
	}

	public void setFareFlightDao(FareFlightDaoImpl fareFlightDao) {
		this.fareFlightDao = fareFlightDao;
	}
	
	
	/**
	 * 将数据存放到数据库中
	 * 主要是fareinfo和fareflight,flightinfo由于是字典需要单独处理
	 */
	@Override
	public void addSortedList2DB(List<FareFlightInfo> sortedList) {
		FareFlightInfo tempFareFlightInfo = null;
		FareFlight tempFareFlight=null;
		FareInfo tempFareInfo=null;
		if(sortedList.size()!=0){
			for(int i=0;i<sortedList.size();i++){
				tempFareFlightInfo=sortedList.get(i);
				//插入中间表
				tempFareFlight = tempFareFlightInfo.getFareFlight();
				String fareFlightId = fareFlightDao.add(tempFareFlight);
				//插入fare表
				List<FareInfo> list = tempFareFlightInfo.getFareInfoList();
				for(int j=0;j<list.size();j++){
					tempFareInfo = list.get(j);
					tempFareInfo.setFareFlightId(fareFlightId);
					fareInfoDao.add(tempFareInfo);
				}
			}
		}else{
			return ;
		}
		
	}

	/**
	 * 从数据库中得到对应的sortedlist
	 */
	@Override
	public List<FareFlightInfo> querySortedListFromLocalDB(InputInfo inputinfo) {
		List<FareFlight> fareFlightList = fareFlightDao.getListByInput(inputinfo);
		if(fareFlightList==null||fareFlightList.size()==0){
			return null;
		}
	    String ids = getIdsStringByFareFlgightList(fareFlightList);
		List<FareInfo> fareInfoList = fareInfoDao.getListByIdsString(ids);
		List<FareFlightInfo> sortedListFromLocalDB = BeanUtil.mixFareInfoAndFareFlight2SortedList(fareInfoList, fareFlightList);
		return sortedListFromLocalDB;
	}

	@Override
	public void deleteByInput(InputInfo inputinfo) {
		//先查找再删除
		List<FareFlight> fareFlightList = fareFlightDao.getListByInput(inputinfo);
		if(fareFlightList.size()==0){
			return ;
		}
		String ids = getIdsStringByFareFlgightList(fareFlightList);
		fareInfoDao.deleteByIdsString(ids);
		fareFlightDao.deleteListByInput(inputinfo);
		
	}


	private String getIdsStringByFareFlgightList(List<FareFlight> fareFlightList){
		StringBuffer ids = new StringBuffer();
		for( int i=0;i<fareFlightList.size();i++){
			if(i!=0){
				ids.append(",");
			}
			ids.append("'");
			ids.append(fareFlightList.get(i).getFareFlightId());
			ids.append("'");
		}
		return ids.toString();
	}
	
	private String getIdsStringByFareFlgightList(FareFlight fareFlight){
		StringBuffer ids = new StringBuffer();
			ids.append("'");
			ids.append(fareFlight.getFareFlightId());
			ids.append("'");
		return ids.toString();
	}

	@Override
	public void addFlightInfo2DB(FlightInfo flightInfo) {
		FlightInfo flightInfoFromDB = flightInfoDao.getFlightInfoById(flightInfo.getFlightinfoId());
		if(flightInfoFromDB==null){
//			log.info("new flightinfo id is :"+flightInfo.getFlightinfoId());
			flightInfoDao.add(flightInfo);
			return ;
		}
		boolean equals = flightInfo.equals(flightInfoFromDB);
		if(equals){
//			log.info("flightinfo is Equal");
		}else{
			log.info("flightinfo has changed");
			flightInfoDao.deleteByID(flightInfo.getFlightinfoId());
			flightInfoDao.add(flightInfo);
		}
	}

	@Override
	public FlightInfo queryFlightInfoById(String flightinfoId) {
		FlightInfo flightInfo = flightInfoDao.getFlightInfoById(flightinfoId);
		return flightInfo;
	}

	@Override
	public void deleteByDate(String date) {
		//删除航班
		flightInfoDao.deleteByDate(date);
		List<FareFlight> fareFlightList = fareFlightDao.getListByDate(date);
		log.info("there are 	:"+fareFlightList.size()+" flight to delete in	"+date);
		for(int i=0;i<fareFlightList.size();i++){
			String ids = getIdsStringByFareFlgightList(fareFlightList.get(i));
			fareInfoDao.deleteByIdsString(ids);
			fareFlightDao.deleteListById(fareFlightList.get(i).getFareFlightId());
		}
		log.info(" delete finish in	"+date);
	}

}
