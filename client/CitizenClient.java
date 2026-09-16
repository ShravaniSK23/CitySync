package client;

import common.CentralRegistryClient;
import common.EmergencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CitizenClient {

    // Stores one citizen emergency request
    static class EmergencyRequest {

        private final int citizenId;
        private final String location;
        private final String incidentType;

        public EmergencyRequest(
                int citizenId,
                String location,
                String incidentType) {

            this.citizenId = citizenId;
            this.location = location;
            this.incidentType = incidentType;
        }

        public int getCitizenId() {
            return citizenId;
        }

        public String getLocation() {
            return location;
        }

        public String getIncidentType() {
            return incidentType;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("============================================");
        System.out.println("       SMARTCITY NEXUS CITIZEN CLIENT");
        System.out.println("============================================");

        try {

            /*
             * Connect to Emergency Server
             */

            EmergencyService emergency =
                    (EmergencyService)
                            CentralRegistryClient.lookup(
                                    "EmergencyServer"
                            );

            /*
             * Take number of requests
             */

            int numberOfRequests = 0;

            while (numberOfRequests <= 0) {

                System.out.print(
                        "Enter number of emergency requests: "
                );

                try {

                    numberOfRequests =
                            Integer.parseInt(
                                    scanner.nextLine().trim()
                            );

                    if (numberOfRequests <= 0) {

                        System.out.println(
                                "Please enter a number greater than 0."
                        );
                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Invalid input. Please enter a number."
                    );
                }
            }

            /*
             * Store user requests
             */

            List<EmergencyRequest> requests =
                    new ArrayList<>();

            /*
             * Take input for every citizen
             */

            for (int i = 1; i <= numberOfRequests; i++) {

                System.out.println();
                System.out.println(
                        "--------------- Request "
                                + i
                                + " ---------------"
                );

                String location;

                while (true) {

                    System.out.print(
                            "Enter location: "
                    );

                    location =
                            scanner.nextLine().trim();

                    if (!location.isEmpty()) {
                        break;
                    }

                    System.out.println(
                            "Location cannot be empty."
                    );
                }

                String incidentType;

                while (true) {

                    System.out.print(
                            "Enter incident type "
                                    + "(Accident/Fire/Medical): "
                    );

                    incidentType =
                            scanner.nextLine().trim();

                    if (!incidentType.isEmpty()) {
                        break;
                    }

                    System.out.println(
                            "Incident type cannot be empty."
                    );
                }

                requests.add(
                        new EmergencyRequest(
                                i,
                                location,
                                incidentType
                        )
                );
            }

            /*
             * Display entered requests
             */

            System.out.println();
            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "        REQUESTS RECEIVED"
            );

            System.out.println(
                    "============================================"
            );

            for (EmergencyRequest request : requests) {

                System.out.println(
                        "[Citizen "
                                + request.getCitizenId()
                                + "] "
                                + request.getIncidentType()
                                + " at "
                                + request.getLocation()
                );
            }

            /*
             * Submit requests to Emergency Server
             *
             * Each request is sent through Java RMI.
             */

            System.out.println();
            System.out.println(
                    "Submitting emergency requests..."
            );

            for (EmergencyRequest request : requests) {

                System.out.println();

                System.out.println(
                        "[Citizen "
                                + request.getCitizenId()
                                + "] Reporting "
                                + request.getIncidentType()
                                + " at "
                                + request.getLocation()
                );

                String response =
                        emergency.reportIncident(
                                request.getLocation(),
                                request.getIncidentType()
                        );

                System.out.println(
                        "[Citizen "
                                + request.getCitizenId()
                                + "] Server Response: "
                                + response
                );
            }

            /*
             * All requests submitted
             */

            System.out.println();
            System.out.println(
                    "============================================"
            );

            System.out.println(
                    "All citizen requests have been submitted."
            );

            System.out.println(
                    "Emergency services are processing them."
            );

            System.out.println(
                    "============================================"
            );

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "[Citizen Client] Unable to communicate "
                            + "with Emergency Server."
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );

            e.printStackTrace();

        } finally {

            scanner.close();
        }
    }
}