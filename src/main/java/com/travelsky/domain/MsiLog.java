package com.travelsky.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MsiLog {
	//ģ��
	private String modelName;
	//�ؼ���
	private String keyWord;
	//����
	private String lev;
	//��־����
	private String content;
	//����ʱ��[�ַ���-->Date]
	private String opTime;
	
	
	//����
	private String reserve;
	//ip
	private String ip;
	//������id
	private String serverid;
	//��־���ʱ��[����setʹ��Ĭ��ϵͳʱ��]
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
