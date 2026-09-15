package emergency;

import common.CentralRegistryClient;
import common.ClockService;
import common.ElectionNodeImpl;

public class EmergencyServer {

    public static void main(String[] args) {

        try {

            EmergencyImpl emergency =
                    new EmergencyImpl();

            ElectionNodeImpl electionNode =
                    new ElectionNodeImpl(1, "Emergency");

            CentralRegistryClient.register(
                    "EmergencyElection", electionNode);

            // Register Emergency service
            CentralRegistryClient.register(
                    "EmergencyServer",
                    emergency
            );

            // Register clock service
            ClockService clockService = emergency;

            CentralRegistryClient.register(
                    "EmergencyClock",
                    clockService
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "       EMERGENCY SERVER STARTED"
            );

            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "Registered as: EmergencyServer"
            );

            System.out.println(
                    "Clock service: EmergencyClock"
            );

            System.out.println(
                    "Thread pool size: 5"
            );

            System.out.println(
                    "Waiting for emergency requests..."
            );

            System.out.println(
                    "============================================"
            );

            // Keep server alive
            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "Emergency Server Error:"
            );

            e.printStackTrace();
        }
    }
}