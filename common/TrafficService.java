package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TrafficService extends Remote {

    String createGreenCorridor(
            String location,
            long lamportTimestamp
    ) throws RemoteException;

    String getTrafficStatus(String location)
            throws RemoteException;

    String updateRoadStatus(
            String location,
            String status
    ) throws RemoteException;
}