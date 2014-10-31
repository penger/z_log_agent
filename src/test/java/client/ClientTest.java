package client;

import interfaces.A;
import interfaces.AImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.travelsky.service.rmi.MsiLogRemote;

public class ClientTest {
	
	private static AImpl remote;
	
	public static void main(String[] args) {
		
		A remote = null;
		try {
			remote = (A) Naming.lookup("rmi://localhost:9999/rmiServer");
//			remote = (A) Naming.lookup("rmi://10.6.159.95:8891/rmiServer");
			System.out.println("客户端生成成功");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
		K k = new K();
		k.a=remote;
		pool.scheduleAtFixedRate(k, 10, 5, TimeUnit.SECONDS);
	}
}

class K implements Runnable{
	public A a=null;
	public void run() {
		try {
			a.sayHello("ni hao a");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
