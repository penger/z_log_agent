package com.travelsky.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.dao.IFareInfoDao;
import com.travelsky.domain.FareInfo;
import com.travelsky.framework.BaseRowMapper;

public class FareInfoDaoImpl  extends BaseDaoImpl implements IFareInfoDao{
	
	private static final Log log = LogFactory.getLog(FareInfoDaoImpl.class);
	
	private final String TABLENAME = "MSI_FAREINFO";
	
	private final String FAREINFO_SEQ="MSI_FAREINFO_SEQ";
	
	
	private final String FAREINFO_ID="FAREINFO_ID";
	//航班的价格
	private final String FARE="FARE";
	//舱位信息
	private final String CABIN="CABIN";
	//运价基础
	private final String FAREBASIS="FAREBASIS";
	//旅客类型
	private final String PTC="PTC";
	
	
	//cn税费
	private final String CNTAX="CNTAX";
	//yq税费
	private final String YQTAX="YQTAX";
	//航班的详细信息
	private final String FLIGHTINFO_KEY="FLIGHTINFO_KEY";
	//参考的外键
	private final String FARE_FLIGHT_ID="FARE_FLIGHT_ID";
	//子航段代码
	private final String SUBID="SUBID";
	
	
	private class FareInfoRowMapper extends BaseRowMapper {
		@SuppressWarnings("unchecked")
		protected Object doMapRow(ResultSet rs, int index) throws SQLException {
			FareInfo fareinfo = new FareInfo();
			Map allColumns = this.getAllColumns();
			if (hasColumn(allColumns, FAREINFO_ID)) {
				fareinfo.setFareinfoId(rs.getString(FAREINFO_ID));
			}
			if (hasColumn(allColumns, FARE)) {
				fareinfo.setFare(rs.getDouble(FARE));
			}
			if (hasColumn(allColumns, CNTAX)) {
				fareinfo.setCntax(rs.getDouble(CNTAX));
			}
			if (hasColumn(allColumns, YQTAX)) {
				fareinfo.setYqtax(rs.getDouble(YQTAX));
			}
			if (hasColumn(allColumns, CABIN)) {
				fareinfo.setCabin(rs.getString(CABIN));
			}
			if (hasColumn(allColumns, FAREBASIS)) {
				fareinfo.setFarebasis(rs.getString(FAREBASIS));
			}
			if (hasColumn(allColumns, PTC)) {
				fareinfo.setPtc(rs.getString(PTC));
			}
			if(hasColumn(allColumns, FLIGHTINFO_KEY)){
				fareinfo.setFlightinfoKey(rs.getString(FLIGHTINFO_KEY));
			}
			if(hasColumn(allColumns, SUBID)){
				fareinfo.setSubid(rs.getString(SUBID));
			}
			if(hasColumn(allColumns, FARE_FLIGHT_ID)){
				fareinfo.setFareFlightId(rs.getString(FARE_FLIGHT_ID));
			}
			return fareinfo;
		}
	}
	@Override
	@SuppressWarnings("unchecked")
	public List <FareInfo> findAll() {
		List <FareInfo> list = new LinkedList<FareInfo>();
		String sql = "select * from " + TABLENAME;
		list = query(sql, new FareInfoRowMapper());
		return list;
	}
	
	@Override
	public int add(FareInfo fareinfo) {
		StringBuffer fareInfoId=new StringBuffer("FR");
		fareInfoId.append(getNextId(FAREINFO_SEQ));
		fareinfo.setFareinfoId(fareInfoId.toString());
		StringBuffer sql = new StringBuffer();
		sql.append("insert into "+TABLENAME+"(FAREINFO_ID,FARE,CABIN,FAREBASIS" +
				",PTC,CNTAX,YQTAX,FLIGHTINFO_KEY,FARE_FLIGHT_ID,SUBID) values(" +
				"?,?,?,?,?,      ?,?,?,?,?)");
		Object[] params =new Object[]{
				fareinfo.getFareinfoId(),
				fareinfo.getFare(),
				fareinfo.getCabin(),
				fareinfo.getFarebasis(),
				fareinfo.getPtc(),
				
				fareinfo.getCntax(),
				fareinfo.getYqtax(),
				fareinfo.getFlightinfoKey(),
				fareinfo.getFareFlightId(),
				fareinfo.getSubid()
		};
		int[] types =new int[]{
				Types.VARCHAR,
				Types.DOUBLE,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				
				Types.DOUBLE,
				Types.DOUBLE,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR
		};
		return update(sql.toString(), params, types);
	}

	@Override
	public List<FareInfo> getListByIdsString(String ids) {
		List <FareInfo> list = null;
		String sql = "select * from " + TABLENAME + " where "+FARE_FLIGHT_ID+" in (" + ids + ") order by "+FLIGHTINFO_KEY+" asc, "+SUBID+" desc";
		list = query(sql, new FareInfoRowMapper());
		return list;
	}

	@Override
	public void deleteByIdsString(String ids) {
		String sql="delete from "+TABLENAME+" where "+FARE_FLIGHT_ID+" in ("+ids+")";
		execute(sql);
	}
	

}
