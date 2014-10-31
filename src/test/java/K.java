import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.travelsky.task.downloadtask.SchedulerDownloadTask;


public class K {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
	SchedulerDownloadTask task = new SchedulerDownloadTask();
	task.doTask();
		
	}

}
