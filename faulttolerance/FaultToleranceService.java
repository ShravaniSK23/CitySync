package faulttolerance;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FaultToleranceService extends Remote {

    String checkWorker(String workerName)
            throws RemoteException;

    String processRequest(String request)
            throws RemoteException;

    String getStatus()
            throws RemoteException;
}