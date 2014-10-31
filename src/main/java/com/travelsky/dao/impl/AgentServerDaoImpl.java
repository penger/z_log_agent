package com.travelsky.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import com.travelsky.domain.AgentServer;
import com.travelsky.framework.BaseRowMapper;

public class AgentServerDaoImpl extends BaseDaoImpl{
	public static final String TABLENAME = "MSI_AGENT_SERVER";
	private final String SERVER_ID="SERVER_ID";
	//服务器的ip
	private final String IP="IP";
	//登录的用户名
	private final String USERNAME="USERNAME";
	//登录的密码
	private final String PASSWORD="PASSWORD";
	//日志存放的路径
	private final String LOGPATH="LOGPATH";
	//要下载到本地的路径
	private final String LOCALPATH="LOCALPATH";
	//使用状态
	private final String STATUS="STATUS";
	
	
	private class LogServerRowMapper extends BaseRowMapper {
		protected Object doMapRow(ResultSet rs, int index) throws SQLException {
			AgentServer logserver = new AgentServer();
			Map allColumns = this.getAllColumns();
			if (hasColumn(allColumns, SERVER_ID)) {
				logserver.setServerid(rs.getString(SERVER_ID));
			}
			if (hasColumn(allColumns, IP)) {
				logserver.setIp(rs.getString(IP));
			}
			if (hasColumn(allColumns, LOCALPATH)) {
				logserver.setLocalpath(rs.getString(LOCALPATH));
			}
			if (hasColumn(allColumns, LOGPATH)) {
				logserver.setLogpath(rs.getString(LOGPATH));
			}
			if (hasColumn(allColumns, PASSWORD)) {
				logserver.setPassword(rs.getString(PASSWORD));
			}
			if (hasColumn(allColumns,USERNAME)) {
				logserver.setUsername(rs.getString(USERNAME));
			}
			if(hasColumn(allColumns,STATUS)){
				logserver.setStatus(rs.getString(STATUS));
			}
			return logserver;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AgentServer> getAll() {
		List<AgentServer> list = null;
		String sql = "select * from " + TABLENAME +" order by "+SERVER_ID;
		list = query(sql, new LogServerRowMapper());
		return list;
	}
	
	public int addLogServer(AgentServer logserver){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into "+TABLENAME+"(SERVER_ID,ip,localpath,logpath,username,password) values(" +
				"?,?,?,?,?,?)");
		Object[] params =new Object[]{
				logserver.getServerId(),
				logserver.getIp(),
				logserver.getLocalpath(),
				logserver.getLogpath(),
				logserver.getUsername(),
				logserver.getPassword()
		};
		int[] types =new int[]{
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
		};
		return update(sql.toString(), params, types);
	}
}
