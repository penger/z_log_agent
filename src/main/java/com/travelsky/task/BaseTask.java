package com.travelsky.task;

import com.travelsky.pub.util.scheduling.SchedulerTask;

public abstract class BaseTask extends SchedulerTask {

	protected boolean isNull(String value){
		return value==null||value.trim().length()==0;
	}
	
	protected static boolean running=false;
	
	@Override
	public void run() {
		try{
			doTask();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public abstract void doTask();
}
