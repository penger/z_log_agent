package com.travelsky.manager;

import java.util.List;

import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FlightInfo;
import com.travelsky.domain.InputInfo;

public interface IFareFlightManager {
	
	public void addSortedList2DB(List <FareFlightInfo> sortedList);

	public List<FareFlightInfo> querySortedListFromLocalDB(InputInfo inputinfo);

	public void deleteByInput(InputInfo inputinfo);

	public void addFlightInfo2DB(FlightInfo tempFlightInfo);

	public FlightInfo queryFlightInfoById(String tempid);

	public void deleteByDate(String date);
}
