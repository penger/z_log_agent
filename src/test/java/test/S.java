package test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S {

	private static final Log log = LogFactory.getLog(S.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
		pool.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				log.info("print once");
			}
		}, 0,5,TimeUnit.SECONDS);

	

	}

}
