package clock;

import common.CentralRegistryClient;
import common.ClockService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BerkeleyCoordinator {

    // Convert epoch milliseconds into readable actual time
    private static String formatTime(long millis) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date(millis));
    }

    // Print both actual timestamp and readable time
    private static void printClock(
            String name,
            ClockService clock) throws Exception {

        long time = clock.getTime();

        System.out.println(
                String.format(
                        "%-10s : %d  (%s)",
                        name,
                        time,
                        formatTime(time)
                )
        );
    }

    public static void main(String[] args) {

        try {

            System.out.println();
            System.out.println("==========================================");
            System.out.println("       BERKELEY CLOCK SYNCHRONIZATION");
            System.out.println("==========================================");

            // Get all participating clocks
            ClockService emergency =
                    (ClockService) CentralRegistryClient.lookup(
                            "EmergencyClock");

            ClockService hospital =
                    (ClockService) CentralRegistryClient.lookup(
                            "HospitalClock");

            ClockService traffic =
                    (ClockService) CentralRegistryClient.lookup(
                            "TrafficClock");

            ClockService weather =
                    (ClockService) CentralRegistryClient.lookup(
                            "WeatherClock");


            // ------------------------------------------
            // BEFORE SYNCHRONIZATION
            // ------------------------------------------

            System.out.println();
            System.out.println("BEFORE SYNCHRONIZATION");
            System.out.println("------------------------------------------");

            long emergencyTime = emergency.getTime();
            long hospitalTime = hospital.getTime();
            long trafficTime = traffic.getTime();
            long weatherTime = weather.getTime();

            System.out.println(
                    String.format(
                            "Emergency : %d  (%s)",
                            emergencyTime,
                            formatTime(emergencyTime)
                    )
            );

            System.out.println(
                    String.format(
                            "Hospital  : %d  (%s)",
                            hospitalTime,
                            formatTime(hospitalTime)
                    )
            );

            System.out.println(
                    String.format(
                            "Traffic   : %d  (%s)",
                            trafficTime,
                            formatTime(trafficTime)
                    )
            );

            System.out.println(
                    String.format(
                            "Weather   : %d  (%s)",
                            weatherTime,
                            formatTime(weatherTime)
                    )
            );


            // ------------------------------------------
            // BERKELEY AVERAGE
            // ------------------------------------------

            long averageTime =
                    (emergencyTime
                            + hospitalTime
                            + trafficTime
                            + weatherTime) / 4;

            System.out.println();
            System.out.println(
                    "Berkeley Average Time : "
                            + averageTime
                            + " ("
                            + formatTime(averageTime)
                            + ")"
            );


            // ------------------------------------------
            // CALCULATE CORRECTIONS
            // ------------------------------------------

            long emergencyCorrection =
                    averageTime - emergencyTime;

            long hospitalCorrection =
                    averageTime - hospitalTime;

            long trafficCorrection =
                    averageTime - trafficTime;

            long weatherCorrection =
                    averageTime - weatherTime;


            System.out.println();
            System.out.println("CLOCK CORRECTIONS");
            System.out.println("------------------------------------------");

            System.out.println(
                    "Emergency correction : "
                            + emergencyCorrection
                            + " ms"
            );

            System.out.println(
                    "Hospital correction  : "
                            + hospitalCorrection
                            + " ms"
            );

            System.out.println(
                    "Traffic correction   : "
                            + trafficCorrection
                            + " ms"
            );

            System.out.println(
                    "Weather correction   : "
                            + weatherCorrection
                            + " ms"
            );


            // ------------------------------------------
            // APPLY CORRECTIONS
            // ------------------------------------------

            System.out.println();
            System.out.println("APPLYING CORRECTIONS");
            System.out.println("------------------------------------------");

            emergency.adjustClock(emergencyCorrection);
            System.out.println(
                    "Emergency adjusted by "
                            + emergencyCorrection
                            + " ms"
            );

            hospital.adjustClock(hospitalCorrection);
            System.out.println(
                    "Hospital adjusted by "
                            + hospitalCorrection
                            + " ms"
            );

            traffic.adjustClock(trafficCorrection);
            System.out.println(
                    "Traffic adjusted by "
                            + trafficCorrection
                            + " ms"
            );

            weather.adjustClock(weatherCorrection);
            System.out.println(
                    "Weather adjusted by "
                            + weatherCorrection
                            + " ms"
            );


            // ------------------------------------------
            // AFTER SYNCHRONIZATION
            // ------------------------------------------

            System.out.println();
            System.out.println("AFTER SYNCHRONIZATION");
            System.out.println("------------------------------------------");

            printClock("Emergency", emergency);
            printClock("Hospital", hospital);
            printClock("Traffic", traffic);
            printClock("Weather", weather);


            System.out.println();
            System.out.println("==========================================");
            System.out.println(
                    "Berkeley synchronization completed!"
            );
            System.out.println("==========================================");
            System.out.println();

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Berkeley synchronization failed."
            );

            System.out.println(
                    "Make sure all CitySync servers are running."
            );

            e.printStackTrace();
        }
    }
}