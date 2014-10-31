package interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AImpl  extends UnicastRemoteObject implements A {

	public AImpl() throws RemoteException {
	}

	@Override
	public void sayHello(String s)throws RemoteException {
		System.err.println(s);
	}

}
