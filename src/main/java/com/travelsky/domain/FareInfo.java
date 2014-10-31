package com.travelsky.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.LogFactory;

/**
 * ZFareinfo entity. @author MyEclipse Persistence Tools
 */

public class FareInfo implements java.io.Serializable, Cloneable {

	// Fields

	private static final long serialVersionUID = -1731694589864338936L;
	
	
	
	
	
	//�۸�id
	private String fareinfoId;
	//����ļ۸�
	private Double fare;
	//��λ��Ϣ
	private String cabin;
	//�˼ۻ���
	private String farebasis;
	//�ÿ�����
	private String ptc;
	
	
	
	
	
	//cn˰��
	private Double cntax;
	//yq˰��
	private Double yqtax;
	//"(������ţ���λ,flightinfoID),���ӣ�1,Y,2013-12-30MF8101;2,Y,2013-12-30MF8102��
	private String flightinfoKey;
	//��������ID
	private String fareFlightId;
	//�������
	private String subid;
	
	
	
	
	public String getFareinfoId() {
		return fareinfoId;
	}

	public void setFareinfoId(String fareinfoId) {
		this.fareinfoId = fareinfoId;
	}

	public Double getFare() {
		return fare;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public String getCabin() {
		return cabin;
	}

	public void setCabin(String cabin) {
		this.cabin = cabin;
	}

	public String getFarebasis() {
		return farebasis;
	}

	public void setFarebasis(String farebasis) {
		this.farebasis = farebasis;
	}

	public String getPtc() {
		return ptc;
	}

	public void setPtc(String ptc) {
		this.ptc = ptc;
	}

	public Double getCntax() {
		return cntax;
	}

	public void setCntax(Double cntax) {
		this.cntax = cntax;
	}

	public Double getYqtax() {
		return yqtax;
	}

	public void setYqtax(Double yqtax) {
		this.yqtax = yqtax;
	}

	public String getFlightinfoKey() {
		return flightinfoKey;
	}

	public void setFlightinfoKey(String flightinfoKey) {
		this.flightinfoKey = flightinfoKey;
	}

	public String getFareFlightId() {
		return fareFlightId;
	}

	public void setFareFlightId(String fareFlightId) {
		this.fareFlightId = fareFlightId;
	}
	
	public void setSubid(String subid) {
		this.subid = subid;
	}

	public String getSubid() {
		return subid;
	}


	public boolean equals(Object o) {
		String[] excude=new String[]{"fareinfoId","fareFlightId","flightInfoList"};
		boolean equals = EqualsBuilder.reflectionEquals(this, o,excude);
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

	@Override
	public FareInfo clone() {
		FareInfo fareInfo = null;
		try {
			fareInfo = (FareInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			LogFactory.getLog(this.getClass()).error("�۸���Ϣ��¡ʧ��:" + this);
		}
		return fareInfo;
	}
}