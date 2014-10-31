package com.travelsky.service.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface MsiLogRemote extends Remote {
	public void AnalysisSet(Set set) throws RemoteException;
}
