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
//	//运行删除任务的机器编号
//	public static final int RUN_DEL_SERVER_ID=1;
//	
//	//是否已经跑过
//	public static int shoppingTaskhadRan = 0;
//	
//	//下载文件的进程
//	public static DownLoadTask downloadtask;
//	//文件推送的进程
//	public static PushTask pushtask;
//	//过期数据删除的进程
//	public static DelInvalidTask deltask;
//	
//	//执行文件下载的任务
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
