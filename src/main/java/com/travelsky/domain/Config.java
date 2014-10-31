package com.travelsky.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 此类负责将各类参数填入到数据库中的config表中,其中静态变量的定义为防止输入错误
 * 如果需要可以自行添加静态变量.
 * @author Esc_penger
 *
 */
public class Config {
	// ------------module="shopping" start------------------
	public final static String SHOPPING_ACCESS_LIMIT_NUM = "SHOPPING_ACCESS_LIMIT_NUM";// 每台服务器shopping访问限制总数量
	// ------------module="shopping" end--------------------------------------

	// ------------module="B2BShopping" start--------------------------------------
	/*
	 * B2B配置 add by zhangli B2B_SHOPPING_TASK_RUN_FLAG:任务是否执行
	 * B2B_SHOPPING_TASK_DELAY_MIN：任务执行间隔时间
	 * B2B_SHOPPING_TASK_TIME_OUT:任务超时时间限制(分钟)
	 * B2B_SHOPPING_TASK_DAY_BEGIN:从多少天后开始(距离今天),0代表从今天开始
	 * B2B_SHOPPING_TASK_DAYS:查询多少天的数据量 B2B_SHOPPING_THREAD_COUNT:每台服务器并发的线程数量
	 */
	public final static String B2B_SHOPPING_TASK_TIME_OUT = "B2B_SHOPPING_TASK_TIME_OUT";
	public final static String B2B_SHOPPING_TASK_DAY_BEGIN = "B2B_SHOPPING_TASK_DAY_BEGIN";
	public final static String B2B_SHOPPING_TASK_DAYS = "B2B_SHOPPING_TASK_DAYS";
	public final static String B2B_SHOPPING_THREAD_COUNT = "B2B_SHOPPING_THREAD_COUNT";

	/*
	 * B2B访问shopping的配置信息
	 */
	public final static String B2B_SHOPPING_EBR_CLIENT_APPNAME = "B2B_SHOPPING_EBR_CLIENT_APPNAME";
	public final static String B2B_SHOPPING_EBR_CLIENT_CUSTOM_NO = "B2B_SHOPPING_EBR_CLIENT_CUSTOM_NO";
	public final static String B2B_SHOPPING_EBR_CLIENT_CUSTOM_OFFICE = "B2B_SHOPPING_EBR_CLIENT_CUSTOM_OFFICE";
	public final static String B2B_SHOPPING_EBR_CLIENT_CUSTOM_VALIDATE = "B2B_SHOPPING_EBR_CLIENT_CUSTOM_VALIDATE";
	public final static String B2B_SHOPPING_EBR_CLIENT_IP = "B2B_SHOPPING_EBR_CLIENT_IP";
	public final static String B2B_SHOPPING_EBR_CLIENT_PORT = "B2B_SHOPPING_EBR_CLIENT_PORT";
	public final static String B2B_SHOPPING_FS_ACCESSNUM = "B2B_SHOPPING_FS_ACCESSNUM";
	public final static String B2B_SHOPPING_FS_CHANNELID = "B2B_SHOPPING_FS_CHANNELID";
	public final static String B2B_SHOPPING_FS_CHANNELTYPE = "B2B_SHOPPING_FS_CHANNELTYPE";
	public final static String B2B_SHOPPING_FS_DEBUG = "B2B_SHOPPING_FS_DEBUG";
	public final static String B2B_SHOPPING_FS_SYSCODE = "B2B_SHOPPING_FS_SYSCODE";

	/*
	 * B2B通用参数配置信息
	 */
	public final static String B2B_SHOPPING_SYSCODE = "B2B_SHOPPING_SYSCODE";
	public final static String B2B_SHOPPING_SYSTYPE = "B2B_SHOPPING_SYSTYPE";
	public final static String B2B_SHOPPING_CHANNELID = "B2B_SHOPPING_CHANNELID";
	public final static String B2B_SHOPPING_CHANNEL_TYPE = "B2B_SHOPPING_CHANNEL_TYPE";
	public final static String B2B_SHOPPING_LANGUAGE = "B2B_SHOPPING_LANGUAGE";
	public final static String B2B_SHOPPING_COMMAND_TYPE = "B2B_SHOPPING_COMMAND_TYPE";
	public final static String B2B_SHOPPING_AGENCY_CITY = "B2B_SHOPPING_AGENCY_CITY";
	public final static String B2B_SHOPPING_AGENCY_PID = "B2B_SHOPPING_AGENCY_PID";

	public final static String B2B_SHOPPING_DISPLAY_CURR_CODE = "B2B_SHOPPING_DISPLAY_CURR_CODE";
	public final static String B2B_SHOPPING_CURR_CODE = "B2B_SHOPPING_CURR_CODE";
	public final static String B2B_SHOPPING_IS_DIRECT_FLIGHT_ONLY = "B2B_SHOPPING_IS_DIRECT_FLIGHT_ONLY";
	public final static String B2B_SHOPPING_JOURNEY_TYPE = "B2B_SHOPPING_JOURNEY_TYPE";
	public final static String B2B_SHOPPING_PASSENGER_NUM = "B2B_SHOPPING_PASSENGER_NUM";
	public final static String B2B_SHOPPING_PASSENGER_TYPE = "B2B_SHOPPING_PASSENGER_TYPE";

	public final static String B2B_SHOPPING_IS_AV_NEEDED = "B2B_SHOPPING_IS_AV_NEEDED";
	public final static String B2B_SHOPPING_IS_PSN_NEEDED = "B2B_SHOPPING_IS_PSN_NEEDED";
	public final static String B2B_SHOPPING_IS_PS_AV_BINDS_NEEDED = "B2B_SHOPPING_IS_PS_AV_BINDS_NEEDED";
	public final static String B2B_SHOPPING_IS_FARES_NEEDED = "B2B_SHOPPING_IS_FARES_NEEDED";
	public final static String B2B_SHOPPING_RULE_TYPE_NEEDED = "B2B_SHOPPING_RULE_TYPE_NEEDED";
	public final static String B2B_SHOPPING_LOWEST_OR_ALL = "B2B_SHOPPING_LOWEST_OR_ALL";
	public final static String B2B_SHOPPING_FARE_SOURCE = "B2B_SHOPPING_FARE_SOURCE";
	public final static String B2B_SHOPPING_FORMAT = "B2B_SHOPPING_FORMAT";
	// ------------module="B2BShopping" end-------------------

	// ------------module="system" start----------------------
	/**
	 * 系统相关信息
	 */
	public final static String SYSTEM_USERNAME = "SYSTEM_USERNAME";
	public final static String SYSTEM_PASSWORD = "SYSTEM_PASSWORD";
	public final static String SYSTEM_REFRESH_CACHE_URLS = "SYSTEM_REFRESH_CACHE_URLS";
	// -----------module="system" end---------------------

	// -----------module="etcwip、webservice" start--------------
	public final static String ETCWIP_SERVICE_URL = "ETCWIP_SERVICE_URL";// etcwip访问地址
	public final static String ETCWIP_FLOW_CHECK_FLAG = "ETCWIP_FLOW_CHECK_FLAG";// 流量检查表计
	public final static String WEBSERVIC_HTTP_ENDPOINT = "WEBSERVIC_HTTP_ENDPOINT";// webservice访问地址
	// -----------module="etcwip、webservice" end--------------------------------------

	// -----------module="all" start--------------------------------------

	public final static String IS_DEL_INVALID_DATA = "IS_DEL_INVALID_DATA";// 是不是执行
																			// 删过期数据的任务
	public final static String LOCAL_SERVER_COUNT = "LOCAL_SERVER_COUNT";// 本程序的总数量
	// -----------module="all" end--------------------------------------

	// -----------module="ebcache" start--------------------------------------
	public final static String IS_EBCACHE_REFRESH = "IS_EBCACHE_REFRESH";// 往缓存中存放数据的等待时间
	public final static String SHOPPING_TIME_OUT_MILLISECOND = "SHOPPING_TIME_OUT_MILLISECOND";// 往缓存中存放数据的等待时间
	// -----------module="ebcache" end--------------------------------------

	// -----------module="log" start--------------------------------------
	public final static String LOG_LOCAL_PATH = "LOG_LOCAL_PATH";
	public final static String IS_DELETE_LOCAL_FILE = "IS_DELETE_LOCAL_FILE";
	public final static String MAX_BATCH_SIZE = "MAX_BATCH_SIZE";
	public final static String MAX_LOG_LINES = "MAX_LOG_LINES";
	public final static String LOG_TASK_RUN = "LOG_TASK_RUN";// 任务是不是需要执行
	public final static String SHOPPING_ALLOW_CONNECTING_FLIGHT = "SHOPPING_ALLOW_CONNECTING_FLIGHT";// 在获取航班的时候要不要获取联程的航班
	public final static String DIRECTPAGE_HEAD="DIRECTPAGE_HEAD";
	// ------------module="log" end--------------------------------------

	// ------------module="deletetask" start--------------------------------------
	public final static String FORWORD_DAYS_DEL_END = "FORWORD_DAYS_DEL_END";
	public final static String FORWORD_DAYS_DEL_LENGTH = "FORWORD_DAYS_DEL_LENGTH";
	public final static String FORWORD_DAYS_DEL_START_TIME = "FORWORD_DAYS_DEL_START_TIME";
	// ------------module="deletetask" end--------------------------------------

	// ------------module="downloadtask" start--------------------------------------
	public final static String DOWN_LOAD_INITIAL_DELAY = "DOWN_LOAD_INITIAL_DELAY";// 第一次延迟时间
	public final static String DOWN_LOAD_DELAY = "DOWN_LOAD_DELAY";// 间隔时间
	// ------------module="downloadtask"
	// end--------------------------------------

	// ------------module="logtask" start--------------------------------------
	public final static String LOG_MAX_THREAD_NUM = "LOG_MAX_THREAD_NUM";
	public final static String LOG_ANALYSIS_TIME_OUT = "LOG_ANALYSIS_TIME_OUT";
	public final static String LOG_CHECK_TIME_SPACE = "LOG_CHECK_TIME_SPACE";
	//-------------module="logtask" end--------------------------------------
	
	// ------------module="recorder" start------------记录器--------------------------
	public final static String IS_PRINT_SQL = "IS_PRINT_SQL";
	public final static String INSERT_MAX_SIZE_FOR_MSILOG = "INSERT_MAX_SIZE_FOR_MSILOG";
	public final static String INSERT_MSILOG_TIME_OUT = "INSERT_MSILOG_TIME_OUT";
	//-------------module="recorder" end--------------------记录器------------------
	
	
	// ------------module="rmi-msiLog" start--------------------------------------
	public final static String RMI_MSI_LOG_REGIST_PORT = "RMI_MSI_LOG_REGIST_PORT";
	public final static String RMI_MSI_LOG_REGIST_URL = "RMI_MSI_LOG_REGIST_URL";
	public final static String RMI_MSI_LOG_ACCESS_IP_LIST = "RMI_MSI_LOG_ACCESS_IP_LIST";
	//-------------module="rmi-msiLog" end--------------------------------------
	
	//-------------module  is agent task run------------------------------
	public final static String DEL_TASK_RUN = "DEL_TASK_RUN";// 删除的任务运行不运行
	public final static String DOWNLOAD_TASK_RUN = "DOWNLOAD_TASK_RUN";// 下载文件运行不运行
	public final static String PUSH_TASK_RUN="PUSH_TASK_RUN";
	//-------------module  is agent task run------------------------------
	
	//----javabean start--------------------------------------
	private String airlineId;
	private String key;
	private String value;
	private String remark;
	private String module;
	private String creator;
	private String createTime;

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAirlineId() {
		return airlineId;
	}
	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
