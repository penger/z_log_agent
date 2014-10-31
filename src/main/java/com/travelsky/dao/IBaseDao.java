package com.travelsky.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public interface IBaseDao {
	
	public void setJdbcTemplate(JdbcTemplate template) ;
	 public JdbcTemplate getJdbcTemplate();
	  public boolean hasColumn(ResultSet rs, String clmName) throws SQLException;
	  public boolean hasColumn(Map allColumns, String clmName);
	  public List pageQuery(String baseSql, RowMapper rm, int begin, int length);
	  public List pageQuery(String baseSql, Object[] params, RowMapper rm, int begin, int length);
	  public List pageQuery(String baseSql, Object[] params, int[] types,  RowMapper rm, int begin, int length);
	  public List query(String sql, RowMapper rm);
	  public List query(String sql, Object[] params, RowMapper rm);
	  public List query(String sql, Object[] params, int[] types, RowMapper rm) ;
	  public int update(String sql, Object[] params, int[] types);
	  public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter);
	  public void execute(String sql);
	  public  String getSingleNextId(String sequenceName);
	  public  String getNextId(String sequenceName);
	  public  int queryCount(String sql);
	  public int queryCount(String sql,Object[] params,int[] types);
	  public   boolean isUpdateSuccess(int[] res);
	  public String queryString(String sql);
	  
	  
	  
}
