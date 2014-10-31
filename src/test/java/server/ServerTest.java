package server;

import interfaces.A;
import interfaces.AImpl;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(8891);
			String url="rmi://10.6.159.95:8891/rmiServer";
//			String url="rmi://localhost:9999/rmiServer";
			A a =new AImpl();
			Naming.bind(url, a);
			System.out.println("服务端注册成功了");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}

	}

}
