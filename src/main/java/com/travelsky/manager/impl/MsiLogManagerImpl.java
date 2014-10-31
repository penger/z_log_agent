package com.travelsky.manager.impl;

import java.util.List;

import com.travelsky.dao.IMsiLogDao;
import com.travelsky.dao.impl.MsiLogDaoImpl;
import com.travelsky.domain.MsiLog;
import com.travelsky.manager.IMsiLogManager;

public class MsiLogManagerImpl implements IMsiLogManager {
	
	private IMsiLogDao msiLogDao;

	@Override
	public boolean addList(List<MsiLog> list) {
		return  msiLogDao.addList(list);
	}

	public void setMsiLogDao(MsiLogDaoImpl msiLogDao) {
		this.msiLogDao = msiLogDao;
	}


}
