package clock;

import common.CentralRegistryClient;
import common.ClockService;

public class LamportCoordinator {

    private static ClockService lookup(
            String name
    ) throws Exception {

        return (ClockService)
                CentralRegistryClient.lookup(name);
    }

    private static void printClock(
            String name,
            ClockService clock
    ) {

        try {

            System.out.println(
                    String.format(
                            "%-10s : %d",
                            name,
                            clock.getLamportTime()
                    )
            );

        } catch (Exception e) {

            System.out.println(
                    String.format(
                            "%-10s : UNAVAILABLE",
                            name
                    )
            );
        }
    }

    public static void main(
            String[] args
    ) {

        try {

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "        CURRENT LAMPORT CLOCK VALUES"
            );
            System.out.println(
                    "=============================================="
            );

            ClockService emergency =
                    lookup("EmergencyClock");

            ClockService hospital =
                    lookup("HospitalClock");

            ClockService traffic =
                    lookup("TrafficClock");

            ClockService weather =
                    lookup("WeatherClock");

            System.out.println();

            printClock(
                    "Emergency",
                    emergency
            );

            printClock(
                    "Hospital",
                    hospital
            );

            printClock(
                    "Traffic",
                    traffic
            );

            printClock(
                    "Weather",
                    weather
            );

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    " These values are from the running CitySync"
            );
            System.out.println(
                    " servers after the actual client events."
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println();

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Lamport clock retrieval failed."
            );

            System.out.println(
                    "Make sure all CitySync servers are running."
            );

            System.out.println();

            e.printStackTrace();
        }
    }
}