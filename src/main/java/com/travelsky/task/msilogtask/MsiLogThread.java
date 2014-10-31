package com.travelsky.task.msilogtask;

import java.util.List;
import java.util.concurrent.Callable;

import com.travelsky.domain.MsiLog;
import com.travelsky.framework.ManagerFactory;
import com.travelsky.manager.IMsiLogManager;

public class MsiLogThread implements Callable<Boolean>{
	
	private List<MsiLog> list ;

	@Override
	public Boolean call() {
		IMsiLogManager msiLogManager = ManagerFactory.getMsiLogManager();
		return msiLogManager.addList(list);
		
	}

	public void setList(List<MsiLog> list) {
		this.list = list;
	}

	public List<MsiLog> getList() {
		return list;
	}
	
}
