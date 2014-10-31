package com.travelsky.service.dbservice;

import java.util.List;

import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.domain.InputInfo;

public interface IDBService {
	//�����ݿ��еõ�mixMap
//	public Map getMixMapFromLocalDB(InputInfo inputinfo);
	//�����ݿ��еõ�cachedbeanlist
//	public CachedBeanList getCachedBeanListByInputInfo(InputInfo inputinfo);
	//���õ��Ľ����ŵ����ݿ���
//	public void addMixMap2DB(Map map);
	
	
	
//	//�����ݿ��еõ��Ѿ��������list
//	public List <FlightFareInfo> querySortedListFromLocalDB(InputInfo inputinfo);
//	//����inputɾ��ԭ�е�����
//	public void deleteByInput(InputInfo inputinfo);
//	//���õ���List��ʽ�Ľ����ŵ����ݿ���
//	public void addSortedList2DB(List <FlightFareInfo> sortedList);
	
	
	
	public List <FareFlightInfo> querySortedListFromLocalDB2(InputInfo inputinfo);
	public void addSortedList2DB2(List <FareFlightInfo> sortedList);
	public void deleteByInput2(InputInfo inputinfo);
	public FlightInfo queryFlightInfoById(String tempid);
	public void deleteByDate(String date);
	
	
}
