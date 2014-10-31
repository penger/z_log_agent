package com.travelsky.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * 此类表示的是一名旅客所有的航班和运价信息
 * 此结构为需要向数据库存储的结构.
 * 由于flight作为字典形式存储,所以设置为null即可
 *  另: fareInfoList.size()<=flightInfoList.size()  命名做了调整
 *  此类组成的sortedList 将作为元素存放于cache中
 * @author gongp
 */
public class FareFlightInfo implements Serializable,Comparable<FareFlightInfo>{
	
	private static final long serialVersionUID = -6316915699195887572L;
	
	private List<FareInfo> fareInfoList;
	
	private List<FlightInfo> flightInfoList;
	
	private FareFlight fareFlight;
	
	
	public FareFlightInfo() {
		this.fareInfoList=new ArrayList<FareInfo>();
		this.flightInfoList = new ArrayList<FlightInfo>();
	}
	
	
	

	public void setFlightInfoList(List<FlightInfo> flightInfoList) {
		this.flightInfoList = flightInfoList;
	}

	public List<FlightInfo> getFlightInfoList() {
		return flightInfoList;
	}


	public List<FareInfo> getFareInfoList() {
		return fareInfoList;
	}
	
	public void setFareInfoList(List <FareInfo>fareInfoList) {
		this.fareInfoList = fareInfoList;
	}
	

	public void setFareFlight(FareFlight fareFlight) {
		this.fareFlight = fareFlight;
	}

	public FareFlight getFareFlight() {
		return fareFlight;
	}

	/**
	 * 先按照总价格排序
	 * 按照税费排序
	 * 按照航班号和舱位,自然排序
	 */
	@Override
	public int compareTo(FareFlightInfo o) {
		//先比较价格
		int condition1 =this.fareFlight.getTotalPrice()-o.fareFlight.getTotalPrice()==0?0:(this.fareFlight.getTotalPrice()-o.fareFlight.getTotalPrice()>0?1:-1);
		if(condition1==0){
			int condition2 =this.fareFlight.getTotalTax()-o.fareFlight.getTotalTax()==0?0:(this.fareFlight.getTotalTax()-o.fareFlight.getTotalTax()>0?1:-1);
			if(condition2==0){
				return this.getFareInfoList().get(0).getFlightinfoKey().compareTo(o.getFareInfoList().get(0).getFlightinfoKey());
			}
			return condition2;
		}else{
			return condition1;
		}
	}




	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	

}
