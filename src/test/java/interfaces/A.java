package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface A extends Remote{
	public void sayHello(String s)throws RemoteException;
}
