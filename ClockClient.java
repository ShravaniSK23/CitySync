import common.ClockService;

import java.rmi.Naming;

public class ClockClient {

    public static void main(String[] args) {

        try {

            System.out.println("============================================");
            System.out.println("       SMARTCITY CLOCK TEST CLIENT");
            System.out.println("============================================");

            ClockService emergencyClock =
                    (ClockService) Naming.lookup(
                            "rmi://localhost/EmergencyClock"
                    );

            ClockService hospitalClock =
                    (ClockService) Naming.lookup(
                            "rmi://localhost/HospitalClock"
                    );

            ClockService trafficClock =
                    (ClockService) Naming.lookup(
                            "rmi://localhost/TrafficClock"
                    );

            ClockService weatherClock =
                    (ClockService) Naming.lookup(
                            "rmi://localhost/WeatherClock"
                    );

            System.out.println();
            System.out.println("========== CLOCK VALUES ==========");

            System.out.println(
                    "Emergency Physical Clock: "
                            + emergencyClock.getPhysicalTime()
            );

            System.out.println(
                    "Emergency Lamport Clock: "
                            + emergencyClock.getLamportTime()
            );

            System.out.println();

            System.out.println(
                    "Hospital Physical Clock: "
                            + hospitalClock.getPhysicalTime()
            );

            System.out.println(
                    "Hospital Lamport Clock: "
                            + hospitalClock.getLamportTime()
            );

            System.out.println();

            System.out.println(
                    "Traffic Physical Clock: "
                            + trafficClock.getPhysicalTime()
            );

            System.out.println(
                    "Traffic Lamport Clock: "
                            + trafficClock.getLamportTime()
            );

            System.out.println();

            System.out.println(
                    "Weather Physical Clock: "
                            + weatherClock.getPhysicalTime()
            );

            System.out.println(
                    "Weather Lamport Clock: "
                            + weatherClock.getLamportTime()
            );

            System.out.println("============================================");

        } catch (Exception e) {

            System.out.println("Clock Client Error:");
            e.printStackTrace();
        }
    }
}