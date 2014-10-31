package com.travelsky.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 此类用于存储存放日志服务器的信息
 * @author gongp
 */
public class AgentServer {
	
	private String serverId;
	//服务器的ip
	private String ip;
	//登录的用户名
	private String username;
	//登录的密码
	private String password;
	//日志存放的路径
	private String logpath;
	//要下载到本地的路径
	private String localpath;
	//使用状态
	private String status;
	
	public AgentServer() {
	}

	public AgentServer(String ip,String serverId, String username, String password,
			String logpath, String localpath,String status) {
		super();
		this.setIp(ip);
		this.serverId = serverId;
		this.username = username;
		this.password = password;
		this.logpath = logpath;
		this.localpath = localpath;
		this.status =status;
	}
	
	
	public String getServerId() {
		return serverId;
	}
	public void setServerid(String serverId) {
		this.serverId = serverId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogpath() {
		return logpath;
	}
	public void setLogpath(String logpath) {
		this.logpath = logpath;
	}
	public String getLocalpath() {
		return localpath;
	}
	public void setLocalpath(String localpath) {
		this.localpath = localpath;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	public boolean equals(Object o) {
		boolean equals = EqualsBuilder.reflectionEquals(this, o);
		return equals;
	}
	public int hashCode() {
		int hashCode = HashCodeBuilder.reflectionHashCode(this);
		return hashCode;
	}
	public String toString() {
		String toString = ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		return toString;
	}


	
}
