package hospital;

import common.CentralRegistryClient;
import common.ClockService;
import common.ElectionNodeImpl;

public class HospitalServer {

    public static void main(String[] args) {

        try {

            HospitalImpl hospital =
                    new HospitalImpl();

        ElectionNodeImpl electionNode =
        new ElectionNodeImpl(2, "Hospital");

        CentralRegistryClient.register(
                "HospitalElection",
                electionNode
        );

            /*
             * Register normal Hospital service.
             */
            CentralRegistryClient.register(
                    "HospitalServer",
                    hospital
            );

            /*
             * Register the same object for
             * clock synchronization.
             */
            ClockService clockService = hospital;

            CentralRegistryClient.register(
                    "HospitalClock",
                    clockService
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "        HOSPITAL SERVER STARTED"
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "Registered as: HospitalServer"
            );

            System.out.println(
                    "Clock service: HospitalClock"
            );

            System.out.println(
                    "Waiting for hospital requests..."
            );

            System.out.println(
                    "============================================"
            );

            /*
             * Keep server alive.
             */
            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "Hospital Server Error:"
            );

            e.printStackTrace();
        }
    }
}