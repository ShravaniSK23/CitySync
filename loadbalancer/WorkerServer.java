package loadbalancer;

import common.CentralRegistryClient;

public class WorkerServer {

    public static void main(String[] args) {

        try {

            String workerName =
                    args.length > 0
                            ? args[0]
                            : "Worker";

            String registryName =
                    args.length > 1
                            ? args[1]
                            : workerName;

            WorkerImpl worker =
                    new WorkerImpl(workerName);

            CentralRegistryClient.register(
                    registryName,
                    worker
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "       " + workerName + " STARTED"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "Registered as: " + registryName
            );

            while (true) {
                Thread.sleep(10000);
            }

        } catch (Exception e) {

            System.err.println(
                    "[" + Thread.currentThread().getName()
                            + "] Worker server error"
            );

            e.printStackTrace();
        }
    }
}