package com.travelsky.framework;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public abstract class BaseRowMapper implements RowMapper {
	private Map allColumns = new HashMap();
	
	/**
	 * ���ܣ�����ResultSet���ɰ�������������Map
	 * ���룺ResultSet
	 * �����void
	 * ˵����
	 */
	private void fillHash(ResultSet rs) throws SQLException{
		if(!allColumns.isEmpty())	return;
		ResultSetMetaData md = rs.getMetaData();
		for(int i=0;i<md.getColumnCount();i++){
			if(!allColumns.containsKey(md.getColumnName(i+1)))
			    allColumns.put(md.getColumnName(i+1).toUpperCase(),new Integer(1));
		}
	}
    /* ���� Javadoc��
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public Object mapRow(ResultSet rs, int index) throws SQLException {
        if (index==0){
            fillHash(rs);
        }
        return doMapRow(rs,index);
    }
    protected abstract Object doMapRow(ResultSet rs, int index) throws SQLException;
    /**
     * @return ���� allColumns��
     */
    public Map getAllColumns() {
        return allColumns;
    }
}
