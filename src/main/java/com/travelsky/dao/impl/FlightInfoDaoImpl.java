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
	
	//航班id
	private String FLIGHTINFO_ID="FLIGHTINFO_ID";
	//航空公司编码
	private String AIRLINE_ID="AIRLINE_ID";
	//销售航空公司代码
	private final String MARKETINGFLIGHTNUMBER="MARKETINGFLIGHTNUMBER";
	//实际承运航空公司代码
	private final String OPERATINGFLIGHTNUMBER="OPERATINGFLIGHTNUMBER";
	//出发地三字码
	private final String ORIGINLOCATION="ORIGINLOCATION";
	//到达地三字码
	private final String DESTINATIONLOCATION="DESTINATIONLOCATION";
	//到达时间
	private final String ARRIVALINGDATETIME="ARRIVALINGDATETIME";
	//出发时间
	private final String DEPARTINGDATETIME="DEPARTINGDATETIME";
	//查询的日期
	private final String FLIGHT_DATE="FLIGHT_DATE";
	//航段编号
	private final String SUBID="SUBID";
	//总飞行时间和分段飞信时间
	private final String EFT="EFT";
	//航站楼
	private final String TERMINALS="TERMINALS";
	//机型
	private final String EQUIPMENT="EQUIPMENT";
	//有无餐食
	private final String MEALCODE="MEALCODE";
	//有无经停
	private final String STOPS="STOPS";
	//准点率
	private final String ONTIMEPERFORMANCE="ONTIMEPERFORMANCE";
	//平均时间延迟
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
				//数据库里面存放的timstamp数据转换为 string类型存储到javabean中
				Timestamp timestamp = rs.getTimestamp(ARRIVALINGDATETIME);
				flightInfo.setArrivalingdatetime(DateUtil.timestamp2string(timestamp));
			}
			if (hasColumn(allColumns, DEPARTINGDATETIME)) {
				//数据库里面存放的timstamp数据转换为 string类型存储到javabean中
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
	 * 单条新增
	 * @param flightInfo
	 * @return 新增的flightinfoId
	 */
	public String add(FlightInfo flightInfo){
		// 设置主键:出发日期+销售航空公司航班号+出发+到达
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
	 * 删除区间段的数据
	 * 如今天2013-11-29,调用delete(2, 5),则删除2天前2013-11-23至2013-11-27的数据(5天,采用闭区间)
	 * 当expiredDays不小于0时生效;当days小于0,删除expiredDays前所有数据
	 * @param expiredDays 几天前
	 * @param days 多少天
	 * @throws Exception 
	 */
	public void deleteByDate(String date){
		
		String sql="delete from "+TABLENAME+" where "+FLIGHT_DATE+"='"+date+"'";
		super.execute(sql.toString());
	}
	
	/**
	 * 根据id查询航班信息
	 * @param flightinfoId
	 * @return 航班信息
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
	 * 根据odd来删除数据
	 */
	public void deleteFlightInfoByInputInfo(InputInfo inputinfo){
		String ori=inputinfo.getOri();
		String dst=inputinfo.getDst();
		String date=inputinfo.getDate();
		String officecode=inputinfo.getOfficecode();
		String isb2b=inputinfo.getIsb2b();
		
		String delflightsql="delete  from "+TABLENAME+" a where a.FLIGHTINFO_ID in(  select FLIGHTINFO_ID from "+TABLENAME+" where originlocation='"+ori+"' and  destinationlocation='"+dst
		 +"' and FLIGHT_DATE ='"+date+"' and officecode='"+officecode+"' and isb2b = '"+isb2b+"')";
		//先删除对应的fareinfo
	//	execute(delfaresql);
		//再删除对应的flightinfo
		execute(delflightsql);
	}
	
	
	/**
	 * 根据ID来删除数据
	 */
	public void deleteByID(String flightinfoid){
		String delflightsql = "delete  from " + TABLENAME + "  where "
				+ FLIGHTINFO_ID + " ='" + flightinfoid + "' ";
		log.info("执行的删除语句为:"+delflightsql);
		execute(delflightsql);
	}
	
	

}
