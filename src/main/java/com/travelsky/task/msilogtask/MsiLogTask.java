package com.travelsky.task.msilogtask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelsky.framework.CacheMsiLogSet;

/**
 *此task不需要额外配置,只是作为一个记录器后台自动维护启动
 * @author gongp
 */
public class MsiLogTask {
	
	private static Log log =LogFactory.getLog(MsiLogTask.class);
	//上次的执行时间
	public static long lastfinishtime=0l;
	private static ExecutorService pool=null;
	private static MsiLogThread msiLogThread=null;
	
	public static boolean isFinished=false;
	/**
	 * 初始化并执行
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

		//等待线程执行结果
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
