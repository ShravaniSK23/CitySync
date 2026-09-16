package emergency;

import common.CentralRegistryClient;
import common.ClockService;
import common.ElectionNodeImpl;

public class EmergencyServer {

    public static void main(String[] args) {

        try {

            /*
             * Get server name from command line.
             */

            String serverName;

            if (args.length > 0) {
                serverName = args[0];
            } else {
                serverName = "EmergencyServer1";
            }

            EmergencyImpl emergency =
                    new EmergencyImpl(serverName);

            /*
             * Register unique Emergency Server.
             */

            CentralRegistryClient.register(
                    serverName,
                    emergency
            );

            /*
             * Register clock service
             */

            ClockService clockService =
                    emergency;

            CentralRegistryClient.register(
                    serverName + "Clock",
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
                    "Server Name: " + serverName
            );

            System.out.println(
                    "Registered as: " + serverName
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

            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "Emergency Server Error:"
            );

            e.printStackTrace();
        }
    }
}