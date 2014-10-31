package com.travelsky.framework.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.FareFlight;
import com.travelsky.domain.FareFlightInfo;
import com.travelsky.domain.FareInfo;

public class BeanUtil{
	
	
	private static final Log log = LogFactory.getLog(BeanUtil.class);
	
	public static List<FareFlightInfo> mixFareInfoAndFareFlight2SortedList(List<FareInfo> fareInfoList, List<FareFlight> fareFlightList){
		List<FareFlightInfo> list = new ArrayList<FareFlightInfo>();
		boolean b[] = new boolean[fareInfoList.size()];
		for(int i=0;i<fareFlightList.size();i++){
			FareFlightInfo fareFlightInfo = new FareFlightInfo();
			for(int j=0;j<fareInfoList.size();j++){
				if(!b[j]){
					if(fareInfoList.get(j).getFareFlightId().equals(fareFlightList.get(i).getFareFlightId())){
						fareFlightInfo.getFareInfoList().add(fareInfoList.get(j));
						b[j]=true;
						if(fareInfoList.get(j).getSubid().equals("1")){
							fareFlightInfo.setFareFlight(fareFlightList.get(i));
							list.add(fareFlightInfo);
							break;
						}
					}
				}
			}
		}
		Collections.sort(list);
		return list;
	}
	
	
	/**
	 * 对比两个已经排序过的list的值
	 * @param localList
	 * @param remoteList
	 * @return
	 */
	public static boolean compareSortedList(List<FareFlightInfo> localList,List<FareFlightInfo> remoteList){
		if(remoteList==null){
			return false;
		}
		if(localList.size()==remoteList.size()){
			for(int i=0;i<localList.size();i++){
				FareFlightInfo local = (FareFlightInfo) localList.get(i);
				FareFlightInfo remote = (FareFlightInfo) remoteList.get(i);
				if(local.getFareInfoList().size()==remote.getFareInfoList().size()){
					int size= local.getFareInfoList().size();
					for(int j=0;j<size;j++){
						FareInfo remotefare=remote.getFareInfoList().get(j);
						FareInfo localfare=local.getFareInfoList().get(j);
						if(!remotefare.equals(localfare)){
							log.info("fare的值不相等:"+localfare+" remotefare:"+remotefare);
							return false;
						}
					}
				}else{
					return false;
				}
			}
			return true;
		}else{
			log.info("组合运价的长度不一样 localList.size="+localList.size()+"        remoteList.size="+remoteList.size());
			return false;
		}
	}

	
	
}