package com.travelsky.task.delinvalidtask;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



import com.travelsky.domain.Config;
import com.travelsky.framework.ManagerFactory;
import com.travelsky.framework.util.DateUtil;
import com.travelsky.framework.util.PropertiesUtil;
import com.travelsky.service.dbservice.IDBService;
import com.travelsky.task.BaseTask;

public class SchedulerDeleteTask extends BaseTask{

	private static final Log log = LogFactory.getLog(SchedulerDeleteTask.class);
	@Override
	public void doTask() {
		//每次直接从数据库里得到是否运行的值
		int isTaskRun=Integer.valueOf(ManagerFactory.getConfigController().getCurrentExactFieldFromDB(Config.DEL_TASK_RUN));
		if(isTaskRun==1){
			int length = PropertiesUtil.getPropertieAsInteger(PropertiesUtil.DEL_DAYS_ENSURE);
			int end = PropertiesUtil.getPropertieAsInteger(PropertiesUtil.DEL_DAYS_KEEP);
			List<String> dateList =DateUtil.getDatesBeforeList(end, length);
			IDBService dbService = ManagerFactory.getDBService();
			for(int i=0;i<dateList.size();i++){
				log.info("delete	:	"+dateList.get(i)+"'s  data  start");
				dbService.deleteByDate(dateList.get(i));
				log.info("delete:	"+dateList.get(i)+"'s data finish");
			}
		}else{
			log.info("DEL_IS_DEL_DATA's value is :"+isTaskRun+" delete task  not run");
		}
	}

}
