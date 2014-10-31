//package com.travelsky.task;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import com.travelsky.domain.Config;
//import com.travelsky.framework.ManagerFactory;
//import com.travelsky.task.delinvalidtask.DelInvalidTask;
//import com.travelsky.task.downloadtask.DownLoadTask;
//import com.travelsky.task.pushtask.PushTask;
//
//public class TaskManager {
//	
//	private static final Log log = LogFactory.getLog(TaskManager.class);
//	//����ɾ������Ļ������
//	public static final int RUN_DEL_SERVER_ID=1;
//	
//	//�Ƿ��Ѿ��ܹ�
//	public static int shoppingTaskhadRan = 0;
//	
//	//�����ļ��Ľ���
//	public static DownLoadTask downloadtask;
//	//�ļ����͵Ľ���
//	public static PushTask pushtask;
//	//��������ɾ���Ľ���
//	public static DelInvalidTask deltask;
//	
//	//ִ���ļ����ص�����
//	public static void doDownLoadTask(){
//		int isExecute = ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.DOWNLOAD_TASK_RUN);
//		if(isExecute==1){
//			if(downloadtask==null){
//				downloadtask=new DownLoadTask();
//			}
//			downloadtask.doDownload();
//		}else{
//			log.info("download task is not allowed  please see db config with key   ALL+ DOWNLOAD_TASK_RUN !");
//		}
//	}
//	
//	public static void stopDownloadTask(){
//		if(downloadtask!=null){
//			downloadtask.stop();
//		}
//	}
//	
//	public static void doPushTask(){
//		PushTask.doPushTask();
//	}
//	
//	public static void stopPushTask(){
//		PushTask.stop();
//	}
//
//	public static void doDelTask(){
//		if(deltask==null){
//			deltask=new DelInvalidTask();
//		}
//		deltask.doDelTask();
//	}
//	
//	public static void stopDelTask(){
//		if(deltask!=null){
//			deltask.stop();
//		}
//	}
//}
