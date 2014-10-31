package com.travelsky.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.dao.IFareFlightDao;
import com.travelsky.domain.FareFlight;
import com.travelsky.domain.InputInfo;
import com.travelsky.framework.BaseRowMapper;

public class FareFlightDaoImpl extends BaseDaoImpl implements IFareFlightDao {
	private static final Log log = LogFactory.getLog(FareFlightDaoImpl.class);
	
	private static final String FARE_FLIGHT_SEQ="MSI_FARE_FLIGHT_SEQ";
	private static final String TABLENAME="MSI_FARE_FLIGHT";
	
	private static final String FARE_FLIGHT_ID="FARE_FLIGHT_ID";
	private static final String ORI="ORI";
	private static final String DST="DST";
	private static final String OFFICECODE="OFFICECODE";
	private static final String FLIGHT_DATE="FLIGHT_DATE";
	
	private static final String AIRLINE_ID="AIRLINE_ID";
	private static final String DIRECTPAGE="DIRECTPAGE";
	private static final String TOTAL_PRICE="TOTAL_PRICE";
	private static final String TOTAL_TAX="TOTAL_TAX";
	private static final String INFOFROM="INFOFROM";
	
	private static final String OFFICELIST="OFFICELIST";
	private static final String ISB2B="ISB2B";
	
	private class FareFlightRowMapper extends BaseRowMapper{
		
		@Override
		protected Object doMapRow(ResultSet rs, int index) throws SQLException {
			FareFlight fareFlight = new FareFlight();
			Map allColumns=this.getAllColumns();
			if(hasColumn(allColumns, FARE_FLIGHT_ID)){
				fareFlight.setFareFlightId(rs.getString(FARE_FLIGHT_ID));
			}
			if(hasColumn(allColumns, ORI)){
				fareFlight.setOri(rs.getString(ORI));
			}
			if(hasColumn(allColumns, DST)){
				fareFlight.setDst(rs.getString(DST));
			}
			if(hasColumn(allColumns, OFFICECODE)){
				fareFlight.setOfficecode(rs.getString(OFFICECODE));
			}
			if(hasColumn(allColumns, FLIGHT_DATE)){
				fareFlight.setFlightDate(rs.getString(FLIGHT_DATE));
			}
			
			
			if(hasColumn(allColumns, AIRLINE_ID)){
				fareFlight.setAirlineId(rs.getString(AIRLINE_ID));
			}
			if(hasColumn(allColumns, DIRECTPAGE)){
				fareFlight.setDirectpage(rs.getString(DIRECTPAGE));
			}
			if(hasColumn(allColumns, TOTAL_PRICE)){
				fareFlight.setTotalPrice(rs.getDouble(TOTAL_PRICE));
			}
			if(hasColumn(allColumns, TOTAL_TAX)){
				fareFlight.setTotalTax(rs.getDouble(TOTAL_TAX));
			}
			if(hasColumn(allColumns, INFOFROM)){
				fareFlight.setInfofrom(rs.getString(INFOFROM));
			}
			
			
			if(hasColumn(allColumns, OFFICELIST)){
				fareFlight.setOfficelist(rs.getString(OFFICELIST));
			}
			if(hasColumn(allColumns, ISB2B)){
				fareFlight.setIsb2b(rs.getString(ISB2B));
			}
			return fareFlight;
		}
		
	}

	@Override
	public String add(FareFlight fareFlight) {
		
		StringBuffer fareFlightId=new StringBuffer("FF");
		fareFlightId.append(super.getNextId(FARE_FLIGHT_SEQ));
		fareFlight.setFareFlightId(fareFlightId.toString());
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("insert into "+TABLENAME+"(FARE_FLIGHT_ID,ORI,DST,OFFICECODE,FLIGHT_DATE,AIRLINE_ID,DIRECTPAGE,TOTAL_PRICE,TOTAL_TAX,INFOFROM,ISB2B,OFFICELIST) values (?,?,?,?,?  ,?,?,?,?,?  ,?,?)");
		Object[] params =new Object[]{
				fareFlight.getFareFlightId(),
				fareFlight.getOri(),
				fareFlight.getDst(),
				fareFlight.getOfficecode(),
				fareFlight.getFlightDate(),
				
				fareFlight.getAirlineId(),
				fareFlight.getDirectpage(),
				fareFlight.getTotalPrice(),
				fareFlight.getTotalTax(),
				fareFlight.getInfofrom(),
				
				fareFlight.getIsb2b(),
				fareFlight.getOfficelist()
		};
		int [] types= new int[]{
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				
				Types.VARCHAR,
				Types.VARCHAR,
				Types.DOUBLE,
				Types.DOUBLE,
				Types.VARCHAR,
				
				Types.VARCHAR,
				Types.VARCHAR
				
		};
		
		super.update(insertSql.toString(), params, types);
		return fareFlightId.toString();
	}

	/**
	 * 根据Input 来得到主表的数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FareFlight> getListByInput(InputInfo inputinfo) {
		String ori=inputinfo.getOri();
		String dst=inputinfo.getDst();
		String date=inputinfo.getDate();
		String officecode=inputinfo.getOfficecode();
		String isb2b=inputinfo.getIsb2b();
		String airlinecode = inputinfo.getAirlinecode();
		
		List <FareFlight> list ;
//		String sql = "select * from "+TABLENAME+" where "+ORI+"=? and  "+DST+"=? and "+FLIGHT_DATE+" =? and "+OFFICECODE+"=? and "+ISB2B+" = ?   order by "+TOTAL_PRICE+" , "+TOTAL_TAX+" asc";
		String sql = "select * from "+TABLENAME+" where "+ORI+"=? and  "+DST+"=? and "+FLIGHT_DATE+" =? and "+OFFICECODE+"=? and "+ISB2B+" = ?  and "+AIRLINE_ID+" =?";
		
		Object[] params =new Object[]{
				ori,
				dst,
				date,
				officecode,
				isb2b,
				airlinecode
		};
		int [] types= new int[]{
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR
		};
		list = query(sql, params, types, new FareFlightRowMapper());
		return list;
	}
	@Override
	public void deleteListByInput(InputInfo inputinfo) {
		String ori=inputinfo.getOri();
		String dst=inputinfo.getDst();
		String date=inputinfo.getDate();
		String officecode=inputinfo.getOfficecode();
		String isb2b=inputinfo.getIsb2b();
		String airlinecode = inputinfo.getAirlinecode();
		String sql = "delete from "+TABLENAME+" where "+ORI+"='"+ori+"' and  "+DST+"='"+dst+"'  and "+FLIGHT_DATE+" ='"+date+"' and "+OFFICECODE+"='"+officecode+"' and "+ISB2B+" = '"+isb2b+"' and "+AIRLINE_ID+" = '"+airlinecode+"'";
		execute(sql);
		
	}
	
	public void deleteListById(String id) {
		String sql = "delete from "+TABLENAME+" where "+FARE_FLIGHT_ID+"='"+id+"'";
		super.execute(sql);
		
	}
	
	public List<FareFlight> getListByDate(String date) {
		
		List <FareFlight> list ;
		String sql = "select * from "+TABLENAME+" where "+FLIGHT_DATE+" =?   ";
		
		Object[] params =new Object[]{
				date
		};
		int [] types= new int[]{
				Types.VARCHAR
		};
		list = query(sql, params, types, new FareFlightRowMapper());
		return list;
	}
	
	
}
