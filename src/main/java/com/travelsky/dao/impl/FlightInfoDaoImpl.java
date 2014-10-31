package com.travelsky.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.FlightInfo;
import com.travelsky.domain.InputInfo;
import com.travelsky.framework.BaseRowMapper;
import com.travelsky.framework.util.DateUtil;

public class FlightInfoDaoImpl extends BaseDaoImpl {
	
	private static Log log = LogFactory.getLog(FlightInfo.class);
	
	public static final String TABLENAME = "MSI_FLIGHTINFO";
	// Fields
	
	//����id
	private String FLIGHTINFO_ID="FLIGHTINFO_ID";
	//���չ�˾����
	private String AIRLINE_ID="AIRLINE_ID";
	//���ۺ��չ�˾����
	private final String MARKETINGFLIGHTNUMBER="MARKETINGFLIGHTNUMBER";
	//ʵ�ʳ��˺��չ�˾����
	private final String OPERATINGFLIGHTNUMBER="OPERATINGFLIGHTNUMBER";
	//������������
	private final String ORIGINLOCATION="ORIGINLOCATION";
	//�����������
	private final String DESTINATIONLOCATION="DESTINATIONLOCATION";
	//����ʱ��
	private final String ARRIVALINGDATETIME="ARRIVALINGDATETIME";
	//����ʱ��
	private final String DEPARTINGDATETIME="DEPARTINGDATETIME";
	//��ѯ������
	private final String FLIGHT_DATE="FLIGHT_DATE";
	//���α��
	private final String SUBID="SUBID";
	//�ܷ���ʱ��ͷֶη���ʱ��
	private final String EFT="EFT";
	//��վ¥
	private final String TERMINALS="TERMINALS";
	//����
	private final String EQUIPMENT="EQUIPMENT";
	//���޲�ʳ
	private final String MEALCODE="MEALCODE";
	//���޾�ͣ
	private final String STOPS="STOPS";
	//׼����
	private final String ONTIMEPERFORMANCE="ONTIMEPERFORMANCE";
	//ƽ��ʱ���ӳ�
	private final String AVERDELAYTIME="AVERDELAYTIME";
	
	private class FlightInfoRowMapper extends BaseRowMapper {
		protected Object doMapRow(ResultSet rs, int index) throws SQLException {
			FlightInfo flightInfo = new FlightInfo();
			Map allColumns = this.getAllColumns();
			if (hasColumn(allColumns, FLIGHTINFO_ID)) {
				flightInfo.setFlightinfoId(rs.getString(FLIGHTINFO_ID));
			}
			if (hasColumn(allColumns, AIRLINE_ID)) {
				flightInfo.setAirlineId(rs.getString(AIRLINE_ID));
			}
			if (hasColumn(allColumns, MARKETINGFLIGHTNUMBER)) {
				flightInfo.setMarketingflightnumber(rs.getString(MARKETINGFLIGHTNUMBER));
			}
			if (hasColumn(allColumns, OPERATINGFLIGHTNUMBER)) {
				flightInfo.setOperatingflightnumber(rs.getString(OPERATINGFLIGHTNUMBER));
			}
			if (hasColumn(allColumns, ORIGINLOCATION)) {
				flightInfo.setOriginlocation(rs.getString(ORIGINLOCATION));
			}
			if (hasColumn(allColumns,DESTINATIONLOCATION)) {
				flightInfo.setDestinationlocation(rs.getString(DESTINATIONLOCATION));
			}
			if (hasColumn(allColumns, ARRIVALINGDATETIME)) {
				//���ݿ������ŵ�timstamp����ת��Ϊ string���ʹ洢��javabean��
				Timestamp timestamp = rs.getTimestamp(ARRIVALINGDATETIME);
				flightInfo.setArrivalingdatetime(DateUtil.timestamp2string(timestamp));
			}
			if (hasColumn(allColumns, DEPARTINGDATETIME)) {
				//���ݿ������ŵ�timstamp����ת��Ϊ string���ʹ洢��javabean��
				Timestamp timestamp = rs.getTimestamp(DEPARTINGDATETIME);
				flightInfo.setDepartingdatetime(DateUtil.timestamp2string(timestamp));
			}
			if (hasColumn(allColumns, FLIGHT_DATE)) {
				flightInfo.setFlightdate(rs.getString(FLIGHT_DATE));
			}
			if (hasColumn(allColumns, SUBID)) {
				flightInfo.setSubid(rs.getString(SUBID));
			}
			if (hasColumn(allColumns, EFT)) {
				flightInfo.setEft(rs.getString(EFT));
			}
			if (hasColumn(allColumns, TERMINALS)) {
				flightInfo.setTerminals(rs.getString(TERMINALS));
			}
			if (hasColumn(allColumns,EQUIPMENT)) {
				flightInfo.setEquipment(rs.getString(EQUIPMENT));
			}
			if (hasColumn(allColumns,MEALCODE)) {
				flightInfo.setMealcode(rs.getString(MEALCODE));
			}
			if (hasColumn(allColumns, STOPS)) {
				flightInfo.setStops(rs.getString(STOPS));
			}
			if (hasColumn(allColumns, ONTIMEPERFORMANCE)) {
				flightInfo.setOntimeperformance(rs.getDouble(ONTIMEPERFORMANCE));
			}
			if (hasColumn(allColumns, AVERDELAYTIME)) {
				flightInfo.setAverdelaytime(rs.getDouble(AVERDELAYTIME));
			}
			return flightInfo;
		}
	}
	
	/**
	 * ��������
	 * @param flightInfo
	 * @return ������flightinfoId
	 */
	public String add(FlightInfo flightInfo){
		// ��������:��������+���ۺ��չ�˾�����+����+����
		String flightinfoId = flightInfo.getFlightinfoId();
		if(null == flightinfoId || "".equals(flightinfoId)) {
			flightinfoId = flightInfo.getFlightdate() + flightInfo.getAirlineId() + flightInfo.getMarketingflightnumber();
			flightinfoId += flightInfo.getDestinationlocation() + flightInfo.getDestinationlocation();
			flightInfo.setFlightinfoId(flightinfoId);
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into " + TABLENAME + "(");
		sql.append("flightinfo_id,airline_id,marketingflightnumber,operatingflightnumber,originlocation");
		sql.append(",destinationlocation,departingdatetime,arrivalingdatetime,flight_date,subid");
		sql.append(",eft,terminals,equipment,mealcode,stops,ontimeperformance,averdelaytime) ");
		sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Object[] params =new Object[]{
				flightInfo.getFlightinfoId(),
				flightInfo.getAirlineId(),
				flightInfo.getMarketingflightnumber(),
				flightInfo.getOperatingflightnumber(),
				flightInfo.getOriginlocation(),
				
				flightInfo.getDestinationlocation(),
				DateUtil.string2timestamp(flightInfo.getDepartingdatetime()),
				DateUtil.string2timestamp(flightInfo.getArrivalingdatetime()),
				flightInfo.getFlightdate(),
				flightInfo.getSubid(),
				
				flightInfo.getEft(),
				flightInfo.getTerminals(),
				flightInfo.getEquipment(),
				flightInfo.getMealcode(),
				flightInfo.getStops(),
				flightInfo.getOntimeperformance(),
				flightInfo.getAverdelaytime()
		};
		int[] types =new int[]{
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				
				Types.VARCHAR,
				Types.TIMESTAMP,
				Types.TIMESTAMP,
				Types.VARCHAR,
				Types.VARCHAR,
				
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.DOUBLE,
				Types.DOUBLE
		};
		super.update(sql.toString(), params, types);
		return flightinfoId;
	}
	
	/**
	 * ɾ������ε�����
	 * �����2013-11-29,����delete(2, 5),��ɾ��2��ǰ2013-11-23��2013-11-27������(5��,���ñ�����)
	 * ��expiredDays��С��0ʱ��Ч;��daysС��0,ɾ��expiredDaysǰ��������
	 * @param expiredDays ����ǰ
	 * @param days ������
	 * @throws Exception 
	 */
	public void deleteByDate(String date){
		
		String sql="delete from "+TABLENAME+" where "+FLIGHT_DATE+"='"+date+"'";
		super.execute(sql.toString());
	}
	
	/**
	 * ����id��ѯ������Ϣ
	 * @param flightinfoId
	 * @return ������Ϣ
	 */
	public FlightInfo getFlightInfoById(String flightinfoId) {
		StringBuffer sql = new StringBuffer("select * from " + TABLENAME + " where 1=1 ");
		sql.append("and flightinfo_id=?");
		List list = super.query(sql.toString(), new Object[] { flightinfoId }, new FlightInfoRowMapper());
		if(null != list && !list.isEmpty()) {
			return (FlightInfo) list.get(0);
		}
		return null;
	}
	
	
	
	/**
	 * ����odd��ɾ������
	 */
	public void deleteFlightInfoByInputInfo(InputInfo inputinfo){
		String ori=inputinfo.getOri();
		String dst=inputinfo.getDst();
		String date=inputinfo.getDate();
		String officecode=inputinfo.getOfficecode();
		String isb2b=inputinfo.getIsb2b();
		
		String delflightsql="delete  from "+TABLENAME+" a where a.FLIGHTINFO_ID in(  select FLIGHTINFO_ID from "+TABLENAME+" where originlocation='"+ori+"' and  destinationlocation='"+dst
		 +"' and FLIGHT_DATE ='"+date+"' and officecode='"+officecode+"' and isb2b = '"+isb2b+"')";
		//��ɾ����Ӧ��fareinfo
	//	execute(delfaresql);
		//��ɾ����Ӧ��flightinfo
		execute(delflightsql);
	}
	
	
	/**
	 * ����ID��ɾ������
	 */
	public void deleteByID(String flightinfoid){
		String delflightsql = "delete  from " + TABLENAME + "  where "
				+ FLIGHTINFO_ID + " ='" + flightinfoid + "' ";
		log.info("ִ�е�ɾ�����Ϊ:"+delflightsql);
		execute(delflightsql);
	}
	
	

}
