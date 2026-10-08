package faulttolerance;

import common.CentralRegistryClient;
import common.RequestStore;

import java.util.List;

public class FaultToleranceDemo {

    public static void main(String[] args) {

        try {

            FaultToleranceService faultTolerance =
                    (FaultToleranceService)
                            CentralRegistryClient.lookup(
                                    "FaultTolerance"
                            );

            System.out.println();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "        CITYSYNC FAULT TOLERANCE"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println();

            System.out.println(
                    "Initial System Status:"
            );

            System.out.println(
                    faultTolerance.getStatus()
            );

            /*
             * Load the requests entered during
             * the initial CitizenClient step.
             */

            List<String> requests =
                    RequestStore.loadRequests();

            /*
             * Check whether requests are available.
             */

            if (requests.isEmpty()) {

                System.out.println();

                System.out.println(
                        "No requests found."
                );

                System.out.println(
                        "Please run CitizenClient first "
                                + "to enter the emergency requests."
                );

                return;
            }

            System.out.println();

            System.out.println(
                    "Using "
                            + requests.size()
                            + " requests from the initial application."
            );

            System.out.println();

            /*
             * Process the same requests using
             * the Fault Tolerance module.
             */

            for (int i = 0;
                 i < requests.size();
                 i++) {

                String request =
                        requests.get(i);

                System.out.println(
                        "------------------------------------------"
                );

                System.out.println(
                        "Request " + (i + 1)
                );

                System.out.println();

                System.out.println(
                        "Sending request: "
                                + request
                );

                try {

                    /*
                     * Existing fault-tolerance logic
                     * is called here.
                     */

                    String result =
                            faultTolerance.processRequest(
                                    request
                            );

                    System.out.println();

                    System.out.println(
                            "Result: "
                                    + result
                    );

                } catch (Exception e) {

                    System.out.println();

                    System.out.println(
                            "Request could not be processed."
                    );
                }
            }

            System.out.println();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "       FINAL SYSTEM STATUS"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    faultTolerance.getStatus()
            );

        } catch (Exception e) {

            System.err.println();

            System.err.println(
                    "Fault tolerance demonstration failed."
            );

            e.printStackTrace();
        }
    }
}