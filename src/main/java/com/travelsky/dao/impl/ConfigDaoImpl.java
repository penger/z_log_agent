package com.travelsky.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.Config;
import com.travelsky.framework.BaseRowMapper;

public class ConfigDaoImpl extends BaseDaoImpl {
	public static final String TABLENAME = "MSI_CONFIG";
	public static final String AIRLINEID = "AIRLINE_ID";
	public static final String KEY = "KEY";
	public static final String REMARK = "REMARK";
	public static final String VALUE = "VALUE";
	public static final String MODULE = "MODULE";
	public static final String CREATOR = "CREATOR";
	public static final String CREATE_TIME = "CREATE_TIME";
    
	private class ConfigRowMapper extends BaseRowMapper {
		protected Object doMapRow(ResultSet rs, int index) throws SQLException {
			Config config = new Config();
			Map allColumns = this.getAllColumns();
			if (hasColumn(allColumns, AIRLINEID)) {
				config.setAirlineId(rs.getString(AIRLINEID));
			}
			if (hasColumn(allColumns, KEY)) {
				config.setKey(rs.getString(KEY));
			}
			if (hasColumn(allColumns, REMARK)) {
				config.setRemark(rs.getString(REMARK));
			}
			if (hasColumn(allColumns, VALUE)) {
				config.setValue(rs.getString(VALUE));
			}
			if (hasColumn(allColumns, MODULE)) {
				config.setModule(rs.getString(MODULE));
			}
			if (hasColumn(allColumns, CREATOR)) {
				config.setCreator(rs.getString(CREATOR));
			}
			if (hasColumn(allColumns, CREATE_TIME)) {
				config.setCreateTime(rs.getString(CREATE_TIME));
			}
			return config;
		}
	}
    
	public List getAll() {
		String sql = "select * from " + TABLENAME;
		List list = query(sql, new ConfigRowMapper());
		return list;
	}
  
	/*
	 * 此方法只对ALL开头的config进行更改 2013年10月12日16:50:46 gp
	 */
	public void update(String key, String value) {
		String sql = "update " + TABLENAME + " set VALUE='" + value
				+ "' where AIRLINE_ID='ALL' and KEY='" + key + "'";
		LogFactory.getLog(this.getClass()).info("更新的config执行的语句为: " + sql);
		execute(sql);
	}
	
	/**
	 * 执行sql
	 */
	public void insert(Config config) {
		String sql = "insert into " + TABLENAME
				+ "(AIRLINE_ID,KEY,VALUE,REMARK) values('"
				+ config.getAirlineId() + "','" + config.getKey() + "','"
				+ config.getValue() + "','" + config.getRemark() + "')";
		LogFactory.getLog(this.getClass()).info("添加config执行的语句为:" + sql);
		execute(sql);
	}
	
	public String getExactFieldValue(String key){
		String sql="select * from "+TABLENAME+ " where "+KEY+"='"+key+"'";
		List list = query(sql, new ConfigRowMapper());
		Config config = (Config) list.get(0);
		String value = config.getValue();
		return value;
	}
	
}
