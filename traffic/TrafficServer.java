package traffic;

import common.CentralRegistryClient;
import common.ClockService;
import common.ElectionNodeImpl;

public class TrafficServer {

    public static void main(String[] args) {

        try {

            TrafficImpl traffic =
                    new TrafficImpl();

        ElectionNodeImpl electionNode =
                new ElectionNodeImpl(3, "Traffic");

        CentralRegistryClient.register(
                "TrafficElection",
                electionNode
        );
            /*
             * Register normal Traffic service.
             */
            CentralRegistryClient.register(
                    "TrafficServer",
                    traffic
            );

            /*
             * Register the same object for
             * clock synchronization.
             */
            ClockService clockService = traffic;

            CentralRegistryClient.register(
                    "TrafficClock",
                    clockService
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "        TRAFFIC SERVER STARTED"
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "Registered as: TrafficServer"
            );

            System.out.println(
                    "Clock service: TrafficClock"
            );

            System.out.println(
                    "Waiting for traffic requests..."
            );

            System.out.println(
                    "============================================"
            );

            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "Traffic Server Error:"
            );

            e.printStackTrace();
        }
    }
}