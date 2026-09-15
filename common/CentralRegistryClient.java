package common;

import java.rmi.Naming;
import java.rmi.Remote;

public class CentralRegistryClient {

    private static RemoteRegistryService registry;

    private static RemoteRegistryService getRegistry()
            throws Exception {

        if (registry == null) {

            String host =
                    System.getenv().getOrDefault(
                            "RMI_REGISTRY_HOST",
                            "localhost"
                    );

            registry =
                    (RemoteRegistryService)
                            Naming.lookup(
                                    "rmi://"
                                            + host
                                            + ":1099/CentralRegistry"
                            );
        }

        return registry;
    }

    public static void register(
            String name,
            Remote object
    ) throws Exception {

        getRegistry().register(
                name,
                object
        );
    }

    public static Remote lookup(
            String name
    ) throws Exception {

        return getRegistry().lookup(
                name
        );
    }
}