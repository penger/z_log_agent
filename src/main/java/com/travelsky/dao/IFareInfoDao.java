package com.travelsky.dao;

import java.util.List;

import com.travelsky.domain.FareInfo;

public interface IFareInfoDao {
	public int add(FareInfo fareinfo);
	public List <FareInfo> findAll();
	public List<FareInfo> getListByIdsString(String ids);
	public void deleteByIdsString(String ids);
}
