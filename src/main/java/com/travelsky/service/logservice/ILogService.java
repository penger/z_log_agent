package com.travelsky.service.logservice;

import java.util.List;

import com.travelsky.domain.FareFlightInfo;

public interface ILogService {
	
	public List <FareFlightInfo> getSortedListFromLog(String logstring);
	
}
