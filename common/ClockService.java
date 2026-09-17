package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClockService extends Remote {

    long getPhysicalTime()
            throws RemoteException;

    void adjustClock(long adjustment)
            throws RemoteException;

    long getLamportTime()
            throws RemoteException;

    long getTime() throws RemoteException;
}