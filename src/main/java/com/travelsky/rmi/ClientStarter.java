package com.travelsky.rmi;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.pub.util.scheduling.applications.SchedulerCommander;

public class ClientStarter {

	private static final Log log= LogFactory.getLog(ClientStarter.class);
	public static void main(String[] args) {
		log.info("scheduler command start at:"+new Date());
		SchedulerCommander.getInstance().execute();
	}

}
