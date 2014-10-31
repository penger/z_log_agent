package com.travelsky.task.pushtask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.domain.Config;
import com.travelsky.framework.ManagerFactory;
import com.travelsky.rmi.Client;
import com.travelsky.task.BaseTask;

public class SchedulerPushTask extends BaseTask{

	private static Log log =LogFactory.getLog(SchedulerPushTask.class);
	@Override
	public void doTask() {
		//是不是运行 ,直接读取数据库
		int isPushData=Integer.valueOf(ManagerFactory.getConfigController().getCurrentExactFieldFromDB(Config.PUSH_TASK_RUN));
		if(isPushData==1){
			Client.doAnalysisAndPush();
		}else{
			log.info("PUSH_IS_PUSH_DATA's value is "+isPushData+" task not run");
		}
		
	
		
	}

}
