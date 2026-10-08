package loadbalancer;

import common.CentralRegistryClient;
import common.RequestStore;

import java.util.List;

public class LoadBalancerDemo {

    public static void main(String[] args) {

        try {

            LoadBalancerService loadBalancer =
                    (LoadBalancerService)
                            CentralRegistryClient.lookup(
                                    "LoadBalancer"
                            );

            System.out.println();
            System.out.println("==========================================");
            System.out.println("          CITYSYNC LOAD BALANCER");
            System.out.println("==========================================");

            System.out.println();
            System.out.println(
                    "Algorithm : "
                            + loadBalancer.getAlgorithm()
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
             * the Load Balancer.
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

                System.out.println(
                        "Input   : " + request
                );

                /*
                 * Send request to Load Balancer.
                 *
                 * Existing Round-Robin logic
                 * remains unchanged.
                 */

                String response =
                        loadBalancer.processRequest(
                                request
                        );

                System.out.println();

                System.out.println(
                        "Result  : " + response
                );
            }

            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "     ALL REQUESTS PROCESSED SUCCESSFULLY"
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.err.println();
            System.err.println(
                    "Load balancing failed."
            );

            e.printStackTrace();
        }
    }
}