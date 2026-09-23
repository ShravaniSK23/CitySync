package loadbalancer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WorkerImpl
        extends UnicastRemoteObject
        implements WorkerService {

    private final String workerName;
    private int requestCount = 0;

    public WorkerImpl(String workerName)
            throws RemoteException {

        super();
        this.workerName = workerName;
    }

    @Override
    public synchronized String getWorkerName()
            throws RemoteException {

        return workerName;
    }

    @Override
    public synchronized String processRequest(String request)
            throws RemoteException {

        requestCount++;

        System.out.println(
                "[" + workerName + "] Processing request: "
                        + request
                        + " | Total requests = "
                        + requestCount
        );

        return workerName
                + " processed request: "
                + request;
    }

    @Override
    public synchronized int getRequestCount()
            throws RemoteException {

        return requestCount;
    }
}