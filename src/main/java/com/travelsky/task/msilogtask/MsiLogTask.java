package com.travelsky.task.msilogtask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.framework.CacheMsiLogSet;

/**
 *��task����Ҫ��������,ֻ����Ϊһ����¼����̨�Զ�ά������
 * @author gongp
 */
public class MsiLogTask {
	
	private static Log log =LogFactory.getLog(MsiLogTask.class);
	//�ϴε�ִ��ʱ��
	public static long lastfinishtime=0l;
	private static ExecutorService pool=null;
	private static MsiLogThread msiLogThread=null;
	
	public static boolean isFinished=false;
	/**
	 * ��ʼ����ִ��
	 */
	public static void doMsiLogTask(){
		CacheMsiLogSet instance = CacheMsiLogSet.getInstance();
		if(pool==null){
			log.info("MsiLog threadpool init");
			pool = Executors.newFixedThreadPool(1);
			msiLogThread=new MsiLogThread();
		}
		
		msiLogThread.setList(CacheMsiLogSet.opLogDataVector);
		Future<Boolean> future = pool.submit(msiLogThread);
//		pool.shutdown();

		//�ȴ��߳�ִ�н��
		try {
			future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		isFinished=true;
		lastfinishtime=System.currentTimeMillis();
	}

}
