package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HospitalService extends Remote {

    String reserveBed(
            String location,
            long lamportTimestamp
    ) throws RemoteException;

    String getHospitalStatus()
            throws RemoteException;

    String releaseBed(String location)
            throws RemoteException;
}