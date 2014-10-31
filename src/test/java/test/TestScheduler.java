package test;

import com.travelsky.pub.util.scheduling.applications.SchedulerCommander;
import com.travelsky.rmi.Client;

public class TestScheduler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args==null||args.length==0){
			SchedulerCommander.getInstance().execute();
		}else{
			pureTest();
		}
		
		
	}

	/*
	 * 
	 */
	private static void pureTest(){
		Client.doAnalysisAndPush();
	}
}
