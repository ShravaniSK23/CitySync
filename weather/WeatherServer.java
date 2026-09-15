package weather;

import common.CentralRegistryClient;
import common.ClockService;
import common.ElectionNodeImpl;

public class WeatherServer {

    public static void main(String[] args) {

        try {

            WeatherImpl weather =
                    new WeatherImpl();

        ElectionNodeImpl electionNode =
                new ElectionNodeImpl(4, "Weather");

        CentralRegistryClient.register(
                "WeatherElection",
                electionNode
        );

            // Register Weather service
            CentralRegistryClient.register(
                    "WeatherServer",
                    weather
            );

            // Register clock service
            ClockService clockService = weather;

            CentralRegistryClient.register(
                    "WeatherClock",
                    clockService
            );

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "       WEATHER SERVER STARTED"
            );

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Registered as: WeatherServer"
            );

            System.out.println(
                    "Clock service: WeatherClock"
            );

            System.out.println(
                    "Waiting for weather requests..."
            );

            System.out.println(
                    "===================================="
            );

            // Keep server alive
            Thread.currentThread().join();

        } catch (Exception e) {

            System.out.println(
                    "Weather Server Error:"
            );

            e.printStackTrace();
        }
    }
}