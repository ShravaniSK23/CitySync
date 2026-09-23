package loadbalancer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoadBalancerService extends Remote {

    String processRequest(String request)
            throws RemoteException;

    String getNextWorker()
            throws RemoteException;

    String getAlgorithm()
            throws RemoteException;
}