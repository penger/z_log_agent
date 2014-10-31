package com.travelsky.framework;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.Config;
import com.travelsky.domain.MsiLog;
import com.travelsky.task.msilogtask.MsiLogTask;

public class CacheMsiLogSet {
	
	private static int  maxlogsize;
	private static int  inserttimeout;
	
	private static CacheMsiLogSet set = null;
	
	//全局操作日志Vector
	public static Vector opLogDataVector=new Vector();  
	
	private static final Log log =LogFactory.getLog(CacheMsiLogSet.class);
	
	public static synchronized CacheMsiLogSet getInstance(){
		if(set==null){
			set = new CacheMsiLogSet();
			maxlogsize=ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.INSERT_MAX_SIZE_FOR_MSILOG);
			inserttimeout=ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.INSERT_MSILOG_TIME_OUT);
		}
		return set;
	}
	//add 事件作为触发的条件所以不用设定轮询的时间!
	public  void addMsiLog2Vector(MsiLog	 msiLog) {
		//先将数据放入
		opLogDataVector.add(msiLog);
		//如果数据量达到了固定数量那么执行任务
		if(opLogDataVector.size()>=maxlogsize-1){
			MsiLogTask.doMsiLogTask();
			opLogDataVector.removeAllElements();
			return ;
		}
		//距离上次执行的时间间隔到了最大间隔,那么执行任务
		if(MsiLogTask.lastfinishtime+inserttimeout>System.currentTimeMillis()){
			MsiLogTask.doMsiLogTask();
			opLogDataVector.removeAllElements();
		}
	}
	
}
