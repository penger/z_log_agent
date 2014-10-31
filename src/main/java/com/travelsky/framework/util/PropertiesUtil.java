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

	//��������
	//���͵ĵ�ַ
	public static final String PUSH_RMI_PUBLISH_URL="PUSH_RMI_PUBLISH_URL";
//	//����ǰ�����ļ�����Сֵ(Ϊ���ܳ������set���ų�����,������Խ��Խ��,ͬʱҲ��pushִ�е���С����)
//	public static final String PUSH_MIN_FILE_SIZE="PUSH_MIN_FILE_SIZE";
//	//����ǰ�����ļ������ֵ(��ֹset��������,������ԽСԽ��)
//	public static final String PUSH_MAX_FILE_SIZE="PUSH_MAX_FILE_SIZE";
	//�зֺ�set�����ֵ
	public static final String PUSH_MAX_SET_SIZE_AFTER_SPLIT="PUSH_MAX_SET_SIZE_AFTER_SPLIT";
	//�зֺ�set����Сֵ
	public static final String PUSH_MAX_SET_SIZE_BEFORE_SPLIT="PUSH_MAX_SET_SIZE_BEFORE_SPLIT";
	//�������ݵĳ�ʱʱ��
	public static final String PUSH_MAX_TIME_OUT="PUSH_MAX_TIME_OUT";
	//��ȡ���ļ��������
	public static final String PUSH_FILE_MAX_LINES="PUSH_FILE_MAX_LINES";
	//һ�ζ�ȡ�����ܴ���������ֵ
	public static final String THRESHOLD_OF_ONCE_OPT="THRESHOLD_OF_ONCE_OPT";
	
	
	
	//����ɾ��
	//�������������
	public static final String DEL_DAYS_KEEP="DEL_DAYS_KEEP";
	//����ǰҪɾ�����������
	public static final String DEL_DAYS_ENSURE="DEL_DAYS_ENSURE";
	
	//��������
	//��־���غ��ŵ�·��
	public static final String DOWNLOAD_LOG_PATH="DOWNLOAD_LOG_PATH";
	//һ����־���ص��������
	public static final String DOWNLOAD_MAX_LOG_SIZE_PER_TIME="DOWNLOAD_MAX_LOG_SIZE_PER_TIME";
	
	
	private static boolean hasInit = false;
	public static Map<String,String> agentConfigMap;
	
	//��̬����,ÿ����Ŀ������ʱ��ִ�д˷���
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
			//���ش����ʱ����е�ַ����С�Ķ� gp test
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
			log.error("�޷��õ�	resources.properties �ļ�"+key+"return 1 on force");
		}
		return 1;
	}
	
	
	public static void main(String[] args) {
		int value = getPropertieAsInteger(DEL_DAYS_ENSURE);
		System.out.println(value);
	}

}
