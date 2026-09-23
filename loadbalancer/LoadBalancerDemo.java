package loadbalancer;

import common.CentralRegistryClient;

import java.util.Scanner;

public class LoadBalancerDemo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

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

            System.out.println();
            System.out.print("Enter number of requests: ");

            int numberOfRequests = scanner.nextInt();
            scanner.nextLine();

            System.out.println();

            for (int i = 1; i <= numberOfRequests; i++) {

                System.out.println("------------------------------------------");
                System.out.println("Request " + i);

                System.out.print(
                        "Enter Crisis (Fire/Accident/Medical): "
                );

                String crisis = scanner.nextLine();

                System.out.print(
                        "Enter Location: "
                );

                String location = scanner.nextLine();

                String request =
                        crisis + " - " + location;

                String response =
                        loadBalancer.processRequest(
                                request
                        );

                System.out.println();

                System.out.println(
                        "Crisis  : " + crisis
                );

                System.out.println(
                        "Location: " + location
                );

                System.out.println(
                        "Result  : " + response
                );
            }

            System.out.println();
            System.out.println("==========================================");
            System.out.println("     ALL REQUESTS PROCESSED SUCCESSFULLY");
            System.out.println("==========================================");

        } catch (Exception e) {

            System.err.println();
            System.err.println("Load balancing failed.");
            e.printStackTrace();

        } finally {

            scanner.close();
        }
    }
}