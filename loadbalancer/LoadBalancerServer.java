package loadbalancer;

import common.CentralRegistryClient;

public class LoadBalancerServer {

    public static void main(String[] args) {

        try {

            LoadBalancerImpl loadBalancer =
                    new LoadBalancerImpl();

            CentralRegistryClient.register(
                    "LoadBalancer",
                    loadBalancer
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "          LOAD BALANCER STARTED"
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "Algorithm: Round Robin"
            );

            System.out.println(
                    "Registered as: LoadBalancer"
            );

            System.out.println(
                    "Waiting for requests..."
            );

            System.out.println(
                    "============================================"
            );

            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "[Load Balancer] Error:"
            );

            e.printStackTrace();
        }
    }
}