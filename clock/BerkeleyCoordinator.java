package clock;

import common.ClockService;
import common.CentralRegistryClient;

import java.util.LinkedHashMap;
import java.util.Map;

public class BerkeleyCoordinator {

    public static void main(String[] args) {

        try {

            System.out.println();
            System.out.println("==========================================");
            System.out.println("       BERKELEY CLOCK SYNCHRONIZATION");
            System.out.println("==========================================");

            /*
             * Connect to all SmartCity servers
             * through the Central Registry.
             */
            Map<String, ClockService> servers =
                    new LinkedHashMap<>();

            servers.put(
                    "Emergency",
                    (ClockService) CentralRegistryClient.lookup(
                            "EmergencyClock"
                    )
            );

            servers.put(
                    "Hospital",
                    (ClockService) CentralRegistryClient.lookup(
                            "HospitalClock"
                    )
            );

            servers.put(
                    "Traffic",
                    (ClockService) CentralRegistryClient.lookup(
                            "TrafficClock"
                    )
            );

            servers.put(
                    "Weather",
                    (ClockService) CentralRegistryClient.lookup(
                            "WeatherClock"
                    )
            );

            /*
             * STEP 1: GET CURRENT TIMES
             */

            System.out.println();
            System.out.println("BEFORE SYNCHRONIZATION");
            System.out.println("------------------------------------------");

            Map<String, Long> times =
                    new LinkedHashMap<>();

            long totalTime = 0;

            for (Map.Entry<String, ClockService> entry
                    : servers.entrySet()) {

                long time =
                        entry.getValue().getPhysicalTime();

                times.put(
                        entry.getKey(),
                        time
                );

                totalTime += time;

                System.out.println(
                        entry.getKey()
                                + " : "
                                + time
                );
            }

            /*
             * STEP 2: CALCULATE AVERAGE
             */

            long averageTime =
                    totalTime / times.size();

            System.out.println();
            System.out.println(
                    "Berkeley Average Time : "
                            + averageTime
            );

            /*
             * STEP 3: CALCULATE CORRECTIONS
             */

            System.out.println();
            System.out.println("CLOCK CORRECTIONS");
            System.out.println("------------------------------------------");

            Map<String, Long> corrections =
                    new LinkedHashMap<>();

            for (Map.Entry<String, Long> entry
                    : times.entrySet()) {

                long correction =
                        averageTime - entry.getValue();

                corrections.put(
                        entry.getKey(),
                        correction
                );

                System.out.println(
                        entry.getKey()
                                + " correction : "
                                + correction
                                + " ms"
                );
            }

            /*
             * STEP 4: APPLY CORRECTIONS
             */

            System.out.println();
            System.out.println("APPLYING CORRECTIONS");
            System.out.println("------------------------------------------");

            for (Map.Entry<String, ClockService> entry
                    : servers.entrySet()) {

                String serverName =
                        entry.getKey();

                long correction =
                        corrections.get(serverName);

                entry.getValue().adjustClock(
                        correction
                );

                System.out.println(
                        serverName
                                + " adjusted by "
                                + correction
                                + " ms"
                );
            }

            /*
             * STEP 5: CHECK SYNCHRONIZED TIMES
             */

            System.out.println();
            System.out.println("AFTER SYNCHRONIZATION");
            System.out.println("------------------------------------------");

            for (Map.Entry<String, ClockService> entry
                    : servers.entrySet()) {

                long synchronizedTime =
                        entry.getValue()
                                .getPhysicalTime();

                System.out.println(
                        entry.getKey()
                                + " : "
                                + synchronizedTime
                );
            }

            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "Berkeley synchronization completed!"
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Berkeley synchronization failed."
            );

            e.printStackTrace();
        }
    }
}