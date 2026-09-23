package faulttolerance;

import common.CentralRegistryClient;

public class FaultToleranceServer {

    public static void main(String[] args) {

        try {

            FaultToleranceImpl faultTolerance =
                    new FaultToleranceImpl();

            CentralRegistryClient.register(
                    "FaultTolerance",
                    faultTolerance
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "       FAULT TOLERANCE SERVICE"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "Registered as: FaultTolerance"
            );

            System.out.println(
                    "Monitoring: Worker1, Worker2, Worker3"
            );

            System.out.println(
                    "Failure handling: Worker Failover"
            );

            System.out.println(
                    "=========================================="
            );

            while (true) {

                Thread.sleep(10000);
            }

        } catch (Exception e) {

            System.err.println(
                    "Fault Tolerance server error"
            );

            e.printStackTrace();
        }
    }
}