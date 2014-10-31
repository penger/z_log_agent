package com.travelsky.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.LogFactory;

/**
 * �������Ϣ,�����ڽ�����������ڵõ�����������Ϣ��Ҫ�����Զ����̺ͽ������������
 * @author gongp
 */
public class InputInfo implements Cloneable {
	
	//date ������Ϊ:	String date=DateUtil.String2String(inputinfo.getDate());   23OCT13
	//date ������ ��Ϊ      yyyy-MM-dd      gp 2013-11-15
	
	private String date;
	private String ori;
	private String dst;
	private String isb2b;
	private String officecode;
	private String airlinecode;
	
	public String getAirlinecode() {
		return airlinecode;
	}
	public void setAirlinecode(String airlinecode) {
		this.airlinecode = airlinecode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public String getIsb2b() {
		return isb2b;
	}
	public void setIsb2b(String isb2b) {
		this.isb2b = isb2b;
	}
	public void setOfficecode(String officecode) {
		this.officecode = officecode;
	}
	public String getOfficecode() {
		return officecode;
	}
	
	public String toCachedKey(){
		StringBuffer sb=new StringBuffer();
		sb.append(getAirlinecode());
		sb.append(getOri());
		sb.append(getDst());
		sb.append("OW");
		sb.append(getDate());
		//������������Ϊnull
		sb.append("AD");
		sb.append(getOfficecode());
		sb.append(getIsb2b());
		String returnstring = sb.toString();
		return returnstring;
	}
	public boolean equals(Object o) {
		boolean equals = EqualsBuilder.reflectionEquals(this, o);
		return equals;
	}
	public int hashCode() {
		int hashCode = HashCodeBuilder.reflectionHashCode(this);
		return hashCode;
	}
	public String toString() {
		String toString = ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		return toString;
	}
	
	@Override
	public InputInfo clone() {
		InputInfo inputInfo = null;
		try {
			inputInfo = (InputInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			LogFactory.getLog(this.getClass()).error("InputInfo��Ϣ��¡ʧ��:" + this);
		}
		return inputInfo;
	}
}
