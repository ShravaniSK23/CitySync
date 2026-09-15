package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteRegistryService extends Remote {

    void register(
            String name,
            Remote object
    ) throws RemoteException;

    Remote lookup(
            String name
    ) throws RemoteException;
}