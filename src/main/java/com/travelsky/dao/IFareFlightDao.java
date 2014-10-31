package com.travelsky.dao;

import java.util.List;

import com.travelsky.domain.FareFlight;
import com.travelsky.domain.InputInfo;

public interface IFareFlightDao {

	//·µ»ØÖµÎªid
	public String add(FareFlight fareFlight);
	
	//
	public List<FareFlight> getListByInput(InputInfo inputinfo);
	
	public void deleteListByInput(InputInfo inputinfo);
	
}
