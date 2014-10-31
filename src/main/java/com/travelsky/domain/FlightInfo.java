package com.travelsky.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ZFlightinfo entity. @author MyEclipse Persistence Tools
 */

/**
 * 2013��9��6��10:54:37
 * gp �����subid,routno,infofrom �����ֶ�,��ע
 */
public class FlightInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6314924876465479169L;
	// Fields
	//����id
	private String flightinfoId;
	//���չ�˾�Ĵ���
	private String airlineId;
	//���ۺ��չ�˾����
	private String marketingflightnumber;
	//ʵ�ʳ��˺��չ�˾����
	private String operatingflightnumber;
	//������������
	private String originlocation;
	
	
	//�����������
	private String destinationlocation;
	//����ʱ��
	private String departingdatetime;
	//����ʱ��
	private String arrivalingdatetime;
	//��ѯ������
	private String flightdate;
	//���α��
	private String subid;
	
	
	//�ܷ���ʱ��ͷֶη���ʱ��
	private String eft;
	//��վ¥
	private String terminals;
	//����
	private String equipment;
	//���޲�ʳ
	private String mealcode;
	//���޾�ͣ
	private String stops;
	
	
	//׼����
	private Double ontimeperformance;
	//ƽ��ʱ���ӳ�
	private Double averdelaytime;

	


	public String getFlightinfoId() {
		return flightinfoId;
	}


	public void setFlightinfoId(String flightinfoId) {
		this.flightinfoId = flightinfoId;
	}


	public String getAirlineId() {
		return airlineId;
	}


	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}


	public String getMarketingflightnumber() {
		return marketingflightnumber;
	}


	public void setMarketingflightnumber(String marketingflightnumber) {
		this.marketingflightnumber = marketingflightnumber;
	}


	public String getOperatingflightnumber() {
		return operatingflightnumber;
	}


	public void setOperatingflightnumber(String operatingflightnumber) {
		this.operatingflightnumber = operatingflightnumber;
	}


	public String getOriginlocation() {
		return originlocation;
	}


	public void setOriginlocation(String originlocation) {
		this.originlocation = originlocation;
	}


	public String getDestinationlocation() {
		return destinationlocation;
	}


	public void setDestinationlocation(String destinationlocation) {
		this.destinationlocation = destinationlocation;
	}


	public String getDepartingdatetime() {
		return departingdatetime;
	}


	public void setDepartingdatetime(String departingdatetime) {
		this.departingdatetime = departingdatetime;
	}


	public String getArrivalingdatetime() {
		return arrivalingdatetime;
	}


	public void setArrivalingdatetime(String arrivalingdatetime) {
		this.arrivalingdatetime = arrivalingdatetime;
	}


	public String getFlightdate() {
		return flightdate;
	}


	public void setFlightdate(String flightdate) {
		this.flightdate = flightdate;
	}


	public String getSubid() {
		return subid;
	}


	public void setSubid(String subid) {
		this.subid = subid;
	}


	public String getEft() {
		return eft;
	}


	public void setEft(String eft) {
		this.eft = eft;
	}


	public String getTerminals() {
		return terminals;
	}


	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}


	public String getEquipment() {
		return equipment;
	}


	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}



	public String getMealcode() {
		return mealcode;
	}


	public void setMealcode(String mealcode) {
		this.mealcode = mealcode;
	}


	public String getStops() {
		return stops;
	}


	public void setStops(String stops) {
		this.stops = stops;
	}


	public Double getOntimeperformance() {
		return ontimeperformance;
	}


	public void setOntimeperformance(Double ontimeperformance) {
		this.ontimeperformance = ontimeperformance;
	}


	public Double getAverdelaytime() {
		return averdelaytime;
	}


	public void setAverdelaytime(Double averdelaytime) {
		this.averdelaytime = averdelaytime;
	}


	/** default constructor */
	public FlightInfo() {
	}


	public boolean equals(Object o) {
		boolean equals = EqualsBuilder.reflectionEquals(this, o,new String[]{"ontimeperformance","averdelaytime"});
		return equals;
	}
	public int hashCode() {
		int hashCode = HashCodeBuilder.reflectionHashCode(this);
		return hashCode;
	}
	public String toString() {
		String toString = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		return toString;
	}
	public String toComparableString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.getMarketingflightnumber());
		sb.append(this.getDepartingdatetime());
		sb.append(this.getArrivalingdatetime());
		sb.append(this.getEquipment());
		sb.append(this.getTerminals());
		return sb.toString();
	}
}