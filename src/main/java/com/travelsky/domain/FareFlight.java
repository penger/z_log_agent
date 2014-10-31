package com.travelsky.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.LogFactory;

public class FareFlight implements Serializable, Cloneable {
	private static final long serialVersionUID = 1973685182993440823L;
	//关联表id
	private String fareFlightId;
	//出发地
	private String ori;
	//到达地
	private String dst;
	//HAK969 etc.
	private String officecode;
	//出发日期(2013-12-03)
	private String flightDate;
	
	private String returnDate;
	
	//HUB2C
	private String airlineId;
	//指向的网站
	private String directpage;
	//总价格
	private Double totalPrice;
	//总税费
	private Double totalTax;
	//信息来源 0:log 1:shopping
	private String infofrom;
	
	//是不是 b2b
	private String isb2b;
	
	private String officelist;
	
	//是否国际票 2014-3-12 10:13:25 add
	private String isinter;
	
	
	public void setFareFlightId(String fareFlightId) {
		this.fareFlightId = fareFlightId;
	}
	public String getFareFlightId() {
		return fareFlightId;
	}
	public String getOri() {
		return ori;
	}
	public void setOri(String ori) {
		this.ori = ori;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getOfficecode() {
		return officecode;
	}
	public void setOfficecode(String officecode) {
		this.officecode = officecode;
	}
	public String getFlightDate() {
		return flightDate;
	}
	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}
	public String getAirlineId() {
		return airlineId;
	}
	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}
	public String getDirectpage() {
		return directpage;
	}
	public void setDirectpage(String directpage) {
		this.directpage = directpage;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Double getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}
	public String getInfofrom() {
		return infofrom;
	}
	public void setInfofrom(String infofrom) {
		this.infofrom = infofrom;
	}
	public String getIsb2b() {
		return isb2b;
	}
	public void setIsb2b(String isb2b) {
		this.isb2b = isb2b;
	}
	public String getOfficelist() {
		return officelist;
	}
	public void setOfficelist(String officelist) {
		this.officelist = officelist;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getIsinter() {
		return isinter;
	}
	public void setIsinter(String isinter) {
		this.isinter = isinter;
	}
	
	
	public int hashCode() {
		int hashCode = HashCodeBuilder.reflectionHashCode(this);
		return hashCode;
	}
	public String toString() {
		String toString = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		return toString;
	}
	
	public boolean equals(Object o) {
		boolean equals = EqualsBuilder.reflectionEquals(this, o);
		return equals;
	}
	
	@Override
	public FareFlight clone() {
		FareFlight fareFlight = null;
		try {
			fareFlight = (FareFlight) super.clone();
		} catch (CloneNotSupportedException e) {
			LogFactory.getLog(this.getClass()).error("价格-航班信息克隆失败:" + this);
		}
		return fareFlight;
	}

}
