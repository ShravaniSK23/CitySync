package faulttolerance;

import common.CentralRegistryClient;

import java.util.Scanner;

public class FaultToleranceDemo {

    public static void main(String[] args) {

        Scanner scanner =
                new Scanner(System.in);

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

            System.out.print(
                    "Enter number of requests: "
            );

            int numberOfRequests =
                    scanner.nextInt();

            scanner.nextLine();

            System.out.println();

            for (int i = 1;
                 i <= numberOfRequests;
                 i++) {

                System.out.println(
                        "------------------------------------------"
                );

                System.out.println(
                        "Request " + i
                );

                System.out.print(
                        "Enter Crisis (Fire/Accident/Medical): "
                );

                String crisis =
                        scanner.nextLine();

                System.out.print(
                        "Enter Location: "
                );

                String location =
                        scanner.nextLine();

                String request =
                        crisis
                                + " - "
                                + location;

                System.out.println();

                System.out.println(
                        "Sending request: "
                                + request
                );

                try {

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

        } finally {

            scanner.close();
        }
    }
}