package common;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteRegistryServer {

    public static void main(String[] args) {

        try {

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "      CENTRAL SMARTCITY REGISTRY"
            );

            System.out.println(
                    "============================================"
            );

            Registry registry =
                    LocateRegistry.createRegistry(1099);

            RemoteRegistry remoteRegistry =
                    new RemoteRegistry();

            registry.rebind(
                    "CentralRegistry",
                    remoteRegistry
            );

            System.out.println(
                    "[Central Registry] Running on port 1099"
            );

            System.out.println(
                    "[Central Registry] Waiting for services..."
            );

            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "[Central Registry] Error:"
            );

            e.printStackTrace();
        }
    }
}