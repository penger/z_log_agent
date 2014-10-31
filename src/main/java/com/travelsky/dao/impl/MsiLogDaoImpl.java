package com.travelsky.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.travelsky.dao.IMsiLogDao;
import com.travelsky.domain.MsiLog;
import com.travelsky.framework.util.DateUtil;

public class MsiLogDaoImpl extends BaseDaoImpl  implements IMsiLogDao {
	
	private static final String TABLE_NAME="MSI_LOG";
	
	private static final String MODEL_NAME="MODEL_NAME";
	private static final String KEY_WORD="KEY_WORD";
	private static final String LEV="LEV";
	
	private static final String CONTENT="CONTENT";
	private static final String OP_TIME="OP_TIME";
	private static final String RESERVE="RESERVE";
	
	private static final String IP="IP";
	private static final String SERVERID="SERVERID";
	private static final String ADDTIME="ADDTIME";
	

	@Override
	public boolean addList(final List<MsiLog> list) {
		
		boolean isSuccess=true;
		//放入sql的时候addtime选择oracle自动填充!
		String sql="insert into "+TABLE_NAME+"( "+MODEL_NAME+","+KEY_WORD+","+LEV+","+CONTENT+","+OP_TIME+","+RESERVE+","+IP+","+SERVERID+") values(?,?,?,?,?  ,?,?,?)";
//		System.out.println(sql);
//		System.out.println(list.size());
//		System.out.println(list);
		try {
			batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					
					MsiLog msiLog = list.get(index);
					
					//必填---start
					ps.setString(1, msiLog.getModelName()==null?"common":msiLog.getModelName());
					ps.setString(2, msiLog.getKeyWord()==null?"common":msiLog.getKeyWord());
					ps.setString(3,msiLog.getLev()==null?"info":msiLog.getLev());
					ps.setString(4, msiLog.getContent()==null?"没有内容":msiLog.getContent());
//					ps.setString(5, msiLog.getOpTime()==null?);
					ps.setTimestamp(5, DateUtil.string2timestamp(msiLog.getOpTime()==null?DateUtil.date2longNormal(new Date()):msiLog.getOpTime()));
					//非必填---start
					ps.setString(6, msiLog.getReserve());
					ps.setString(7, msiLog.getIp());
					ps.setString(8, msiLog.getServerid());
//					ps.setString(9, msiLog.getAddtime());
//					ps.setTimestamp(9, DateUtil.string2timestamp(msiLog.getAddtime()));
					
				}
				@Override
				public int getBatchSize() {
					return list.size();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess=false;
		}
		return isSuccess;
	}

	
}
