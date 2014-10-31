package com.travelsky.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.agent.Agent;
import com.travelsky.framework.util.PropertiesUtil;
import com.travelsky.service.rmi.MsiLogRemote;
/**
 * ����Ӧ��Ϊһ��ʵ��[���ڲ����ܶ��������ͬʱ���д�jar�ʼ�����]
 * @author gongp
 */
public class Client {
	

	public static List<Set> uniqueSetList=null;
	//��¼�����ڼ�¼����ȡremote
	public static int counter=0;
	public static Agent agent = null;
	private static final Log log = LogFactory.getLog(Client.class);
	private static List<MsiLogRemote> serverList=null;
	private static int serverSize=0;
	//һ���ܴ������ֵ gp 2014-2-28 13:25:13
	public static int thresholdsize=0;
	private static int times=0;
	
	public static void main(String[] args) {
		doAnalysisAndPush();
	}
	

	/**
	 * ��ʼ��(��̬�����)
	 * @throws NotBoundException
	 * @throws IOException
	 */
	static{
			agent=new Agent();
			String rmiurls = PropertiesUtil.getPropertieAsString(PropertiesUtil.PUSH_RMI_PUBLISH_URL);
			String[] urls = rmiurls.split(";");
			serverSize=urls.length;
			log.info("there are :"+serverSize+" listening to this client < "+rmiurls+" >");
			serverList=new ArrayList<MsiLogRemote>();
			for(String url:urls){
				MsiLogRemote remote=null;
				try {
					remote= (MsiLogRemote) Naming.lookup(url);
					serverList.add(remote);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
			log.info("client init client lookup for "+rmiurls+" this should only be recorded once ");
			thresholdsize=PropertiesUtil.getPropertieAsInteger(PropertiesUtil.THRESHOLD_OF_ONCE_OPT);
			int onceCommit = PropertiesUtil.getPropertieAsInteger(PropertiesUtil.PUSH_MAX_SET_SIZE_BEFORE_SPLIT);
			times=thresholdsize/onceCommit;

	}
	/**
	 * ��������䵽Ҫ���͵Ķ���
	 */
	private static  List<Set> fillSetList(){
		List<Set> list = agent.provideData();
		if(list==null||list.isEmpty()){
			return null;
		}else{
			return list;
		}
	}
	
	
	
	/**
	 * Ĭ����һ�����˹���,���ļ�������û�дﵽ��Ҫ��������ʱ�򷵻ص�����Ϊnull
	 * 
	 * 	
	 */
//	public static void doAnalysisAndPushOld(){
//				int sizeCounter=0;
//				//ֻҪ�ǵ�ǰ����εĲ���������û�д�����ֵ,���ҵ�ǰ�����زֿ⻹�ܹ��ṩ������ô������һ��ѭ��
//				while((Agent.cursor!=-1&&(uniqueSetList=fillSetList())!=null)||((uniqueSetList=fillSetList())!=null&&times<sizeCounter)){
//					log.info("This operation will send "+uniqueSetList.size()+" have send "+sizeCounter+" already");
//					sizeCounter+=uniqueSetList.size();
//					try{
//						log.info("uniqueSetList size is :"+uniqueSetList.size()+"       prepare to sending data to server !");
//						Iterator<Set> iterator = uniqueSetList.iterator();
//						while(iterator.hasNext()){
//							Set tinyset = iterator.next();
//							MsiLogRemote current = getRemoteServer();
//							System.out.println(current+"  "+tinyset.size());
//							current.AnalysisSet(tinyset);
//						}
//						log.info("send finished and succeed");
//					} catch (RemoteException e) {
//						log.error("send error course : RemoteException");
//						e.printStackTrace();
//					} catch (IOException e) {
//						log.error("send error course: IOException");
//						e.printStackTrace();
//					}
//				}
//				
//	}
	
	public static void doAnalysisAndPush(){
		int count=0;
		//�����жϵ�ǰ���ڶ�ȡ���ļ���û�б�����(����<�����Էǳ�С>)
		while(Agent.cursor!=-1||count<times){
			count++;
			log.info("count is :"+count+"cursor is :"+Agent.cursor);
			uniqueSetList=fillSetList();
			if(uniqueSetList==null){
				log.info("enter the loop times is :"+count+" and there isn't any file in folder ");
				break;
			}
			log.info("uniqueSetList size has reached threshold  prepare to send data to server ! "+count+" during this period");
			Iterator<Set> iterator = uniqueSetList.iterator();
			while(iterator.hasNext()){
				Set tinyset = iterator.next();
				MsiLogRemote current = getRemoteServer();
				try {
					current.AnalysisSet(tinyset);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			log.info("send finished and succeed");
		}
	}
	


	/**
	 * ��ȡҪ���͵�Զ�̵�ַ
	 * @return
	 */
	private static MsiLogRemote getRemoteServer() {
		MsiLogRemote current=null;
		if(counter==serverSize){
			counter=0;
		}
		if(serverList!=null&&serverList.size()>0){
			current = serverList.get(counter);
			counter++;
		}
		return current;
	}

}
