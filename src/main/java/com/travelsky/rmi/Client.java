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
 * 此类应该为一个实例[由于不可能多个服务器同时运行此jar故简单配置]
 * @author gongp
 */
public class Client {
	

	public static List<Set> uniqueSetList=null;
	//记录器用于记录并获取remote
	public static int counter=0;
	public static Agent agent = null;
	private static final Log log = LogFactory.getLog(Client.class);
	private static List<MsiLogRemote> serverList=null;
	private static int serverSize=0;
	//一次能处理的阈值 gp 2014-2-28 13:25:13
	public static int thresholdsize=0;
	private static int times=0;
	
	public static void main(String[] args) {
		doAnalysisAndPush();
	}
	

	/**
	 * 初始化(静态代码块)
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
	 * 将数据填充到要推送的对象
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
	 * 默认有一个过滤规则,当文件的数量没有达到想要的数量的时候返回的数据为null
	 * 
	 * 	
	 */
//	public static void doAnalysisAndPushOld(){
//				int sizeCounter=0;
//				//只要是当前的这次的操作中数量没有大于阈值,并且当前的下载仓库还能够提供数据那么继续下一次循环
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
		//首先判断当前正在读取的文件有没有被读完(读完<可能性非常小>)
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
	 * 获取要推送的远程地址
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
