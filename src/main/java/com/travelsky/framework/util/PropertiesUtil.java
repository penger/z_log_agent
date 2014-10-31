package com.travelsky.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PropertiesUtil {
	
	private static Log log = LogFactory.getLog(Properties.class);

	//数据推送
	//推送的地址
	public static final String PUSH_RMI_PUBLISH_URL="PUSH_RMI_PUBLISH_URL";
//	//推送前保存文件的最小值(为了能充分利用set来排除数据,理论上越大越好,同时也是push执行的最小条件)
//	public static final String PUSH_MIN_FILE_SIZE="PUSH_MIN_FILE_SIZE";
//	//推送前保存文件的最大值(防止set过度膨胀,理论上越小越好)
//	public static final String PUSH_MAX_FILE_SIZE="PUSH_MAX_FILE_SIZE";
	//切分后set的最大值
	public static final String PUSH_MAX_SET_SIZE_AFTER_SPLIT="PUSH_MAX_SET_SIZE_AFTER_SPLIT";
	//切分后set的最小值
	public static final String PUSH_MAX_SET_SIZE_BEFORE_SPLIT="PUSH_MAX_SET_SIZE_BEFORE_SPLIT";
	//推送数据的超时时间
	public static final String PUSH_MAX_TIME_OUT="PUSH_MAX_TIME_OUT";
	//读取的文件最大行数
	public static final String PUSH_FILE_MAX_LINES="PUSH_FILE_MAX_LINES";
	//一次读取操作能处理的最大阈值
	public static final String THRESHOLD_OF_ONCE_OPT="THRESHOLD_OF_ONCE_OPT";
	
	
	
	//数据删除
	//保留几天的数据
	public static final String DEL_DAYS_KEEP="DEL_DAYS_KEEP";
	//保留前要删除几天的数据
	public static final String DEL_DAYS_ENSURE="DEL_DAYS_ENSURE";
	
	//数据下载
	//日志下载后存放的路径
	public static final String DOWNLOAD_LOG_PATH="DOWNLOAD_LOG_PATH";
	//一次日志下载的最大数量
	public static final String DOWNLOAD_MAX_LOG_SIZE_PER_TIME="DOWNLOAD_MAX_LOG_SIZE_PER_TIME";
	
	
	private static boolean hasInit = false;
	public static Map<String,String> agentConfigMap;
	
	//静态方法,每次项目启动的时候执行此方法
	public static  void init() throws IOException{
		agentConfigMap = new Hashtable<String,String>();
		InputStream in = PropertiesUtil.class.getClassLoader().getResource("agent.properties").openStream();
		Properties p = new Properties();
		p.load(in);
		Set<String> names = p.stringPropertyNames();
		if(names.isEmpty()){
			log.info("agent.properties has no content");
		}else{
			Iterator<String> it = names.iterator();
			while(it.hasNext()){
				String tempname=it.next();
				String property = p.getProperty(tempname);
				agentConfigMap.put(tempname, property);
				log.info(tempname+"  :  "+property);
			}
			log.info("init   agent.properties size :"+agentConfigMap.size());
		}
		hasInit=true;
	}
	
	public static String getPropertieAsString(String key){
		try {
			if(!hasInit){
				init();
			}
			//本地处理的时候呼叫地址的最小改动 gp test
//			if(key.equals("PUSH_RMI_PUBLISH_URL")){
//				return "rmi://10.6.159.96:8889/rmiServer";
//			}
			return (String) agentConfigMap.get(key);
		} catch (IOException e) {
			log.error("cannot parse	agent.properties "+key+" ruturn an empty string  on force!");
		}
		return "";
	}
	
	public static int getPropertieAsInteger(String key){
		try {
			if(!hasInit){
				init();
			}
			return Integer.parseInt((String) agentConfigMap.get(key));
		} catch (IOException e) {
			log.error("无法得到	resources.properties 文件"+key+"return 1 on force");
		}
		return 1;
	}
	
	
	public static void main(String[] args) {
		int value = getPropertieAsInteger(DEL_DAYS_ENSURE);
		System.out.println(value);
	}

}
