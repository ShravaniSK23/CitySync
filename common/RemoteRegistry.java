package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.concurrent.ConcurrentHashMap;

public class RemoteRegistry
        extends UnicastRemoteObject
        implements RemoteRegistryService {

    private final ConcurrentHashMap<String, Remote>
            services = new ConcurrentHashMap<>();

    public RemoteRegistry()
            throws RemoteException {

        super();
    }

    @Override
    public void register(
            String name,
            Remote object
    ) throws RemoteException {

        services.put(name, object);

        System.out.println(
                "[Central Registry] Registered: "
                        + name
        );
    }

    @Override
    public Remote lookup(
            String name
    ) throws RemoteException {

        Remote service = services.get(name);

        if (service == null) {

            throw new RemoteException(
                    "Service not found: " + name
            );
        }

        return service;
    }
}