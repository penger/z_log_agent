package com.travelsky.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MsiLog {
	//模块
	private String modelName;
	//关键词
	private String keyWord;
	//级别
	private String lev;
	//日志内容
	private String content;
	//操作时间[字符串-->Date]
	private String opTime;
	
	
	//保留
	private String reserve;
	//ip
	private String ip;
	//服务器id
	private String serverid;
	//日志添加时间[无需set使用默认系统时间]
	private String addtime;
	
	
	public MsiLog(String modelName, String keyWord, String lev, String content,
			String opTime, String reserve, String ip, String serverid,
			String addtime) {
		super();
		this.modelName = modelName;
		this.keyWord = keyWord;
		this.lev = lev;
		this.content = content;
		this.opTime = opTime;
		this.reserve = reserve;
		this.ip = ip;
		this.serverid = serverid;
		this.addtime = addtime;
	}
	
	
	public MsiLog() {
	}


	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getLev() {
		return lev;
	}
	public void setLev(String lev) {
		this.lev = lev;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getServerid() {
		return serverid;
	}
	public void setServerid(String serverid) {
		this.serverid = serverid;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
	
}
