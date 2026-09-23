package loadbalancer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerService extends Remote {

    String getWorkerName() throws RemoteException;

    String processRequest(String request) throws RemoteException;

    int getRequestCount() throws RemoteException;
}