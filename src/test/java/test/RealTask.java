package test;

import java.util.Date;

public class RealTask extends BaseTask {

	@Override
	public void doTask() {
		System.out.println("show info of the task !");
		//–›√ﬂ10√Î
		try {
			System.out.println(Thread.currentThread().getId()+"sleepstart"+new Date());
			Thread.sleep(10000);
			System.out.println(Thread.currentThread().getId()+"sleepend"+new Date());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
