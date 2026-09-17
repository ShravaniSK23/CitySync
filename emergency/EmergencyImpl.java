package emergency;

import common.CentralRegistryClient;
import common.EmergencyService;
import common.HospitalService;
import common.TrafficService;
import common.WeatherService;
import common.ClockService;

import clock.PhysicalClock;
import clock.LamportClock;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmergencyImpl extends UnicastRemoteObject
        implements EmergencyService, ClockService {

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(5);

    private final PhysicalClock physicalClock =
            new PhysicalClock(0);

    private final LamportClock lamportClock =
            new LamportClock();

    private final String serverName;

public EmergencyImpl(String serverName)
        throws RemoteException {

    super();

    this.serverName = serverName;

    System.out.println(
            "[" + serverName + "] Thread pool created with 5 threads."
    );
}

@Override
public long getTime() throws RemoteException {
    return physicalClock.getTime();
}

    @Override
    public String reportIncident(
            String location,
            String incidentType)
            throws RemoteException {

        // Lamport local event
        long lamportTimestamp = lamportClock.localEvent();

        long requestId = System.currentTimeMillis();

        System.out.println();
        System.out.println("============================================");
        System.out.println("       LAMPORT CLOCK - EMERGENCY");
        System.out.println("============================================");

        System.out.println(
                "[Emergency Server] New request received"
        );

        System.out.println(
                "Request ID    : " + requestId
        );

        System.out.println(
                "Location      : " + location
        );

        System.out.println(
                "Incident      : " + incidentType
        );

        System.out.println(
                "[LAMPORT] Emergency local event = "
                        + lamportTimestamp
        );

        executorService.submit(() -> {

            long threadId =
                    Thread.currentThread().getId();

            String threadName =
                    Thread.currentThread().getName();

            System.out.println(
        "[" + serverName + "] "
                + "[Thread " + threadId + "] "
                + threadName
                + " started processing "
                + incidentType
                + " at "
                + location
);

            try {

                /*
                 * CENTRAL REGISTRY LOOKUPS
                 */

                HospitalService hospital =
                        (HospitalService)
                                CentralRegistryClient.lookup(
                                        "HospitalServer"
                                );

                TrafficService traffic =
                        (TrafficService)
                                CentralRegistryClient.lookup(
                                        "TrafficServer"
                                );

                WeatherService weather =
                        (WeatherService)
                                CentralRegistryClient.lookup(
                                        "WeatherServer"
                                );

                /*
                 * RPC CALL 1
                 * Emergency -> Hospital
                 */

                long hospitalSendTime =
                        lamportClock.sendEvent();

                System.out.println(
                        "[LAMPORT] Emergency -> Hospital"
                );

                System.out.println(
                        "[LAMPORT] Send timestamp = "
                                + hospitalSendTime
                );

                String hospitalResponse =
                        hospital.reserveBed(
                                location,
                                hospitalSendTime
                        );

                System.out.println(
                        "[Thread " + threadId + "] "
                                + "Hospital Response: "
                                + hospitalResponse
                );

                /*
                 * RPC CALL 2
                 * Emergency -> Traffic
                 */

                long trafficSendTime =
                        lamportClock.sendEvent();

                System.out.println(
                        "[LAMPORT] Emergency -> Traffic"
                );

                System.out.println(
                        "[LAMPORT] Send timestamp = "
                                + trafficSendTime
                );

                String trafficResponse =
                        traffic.createGreenCorridor(
                                location,
                                trafficSendTime
                        );

                System.out.println(
                        "[Thread " + threadId + "] "
                                + "Traffic Response: "
                                + trafficResponse
                );

                /*
                 * RPC CALL 3
                 * Emergency -> Weather
                 */

                long weatherSendTime =
                        lamportClock.sendEvent();

                System.out.println(
                        "[LAMPORT] Emergency -> Weather"
                );

                System.out.println(
                        "[LAMPORT] Send timestamp = "
                                + weatherSendTime
                );

                String weatherResponse =
                        weather.getWeather(
                                location,
                                weatherSendTime
                        );

                System.out.println(
                        "[Thread " + threadId + "] "
                                + "Weather Response: "
                                + weatherResponse
                );

                /*
                 * Local event:
                 * Dispatch ambulance
                 */

                long dispatchTime =
                        lamportClock.localEvent();

                System.out.println(
                        "[LAMPORT] Emergency ambulance "
                                + "dispatch event = "
                                + dispatchTime
                );

                String ambulanceResponse =
                        dispatchAmbulance(location);

                System.out.println(
                        "[Thread " + threadId + "] "
                                + ambulanceResponse
                );

                System.out.println(
                        "[Thread " + threadId + "] "
                                + "Request completed for "
                                + location
                );

                System.out.println(
                        "[LAMPORT] Emergency final clock = "
                                + lamportClock.getTime()
                );

                System.out.println(
                        "============================================"
                );

            } catch (Exception e) {

                System.out.println(
                        "[Thread " + threadId + "] "
                                + "Error processing request for "
                                + location
                );

                e.printStackTrace();
            }
        });

        return "Emergency request accepted and assigned to a worker thread.";
    }

    @Override
    public String dispatchAmbulance(String location)
            throws RemoteException {

        System.out.println(
                "[Emergency Server] Ambulance dispatched to "
                        + location
        );

        return "Ambulance dispatched successfully";
    }

    @Override
    public String dispatchFireTruck(String location)
            throws RemoteException {

        System.out.println(
                "[Emergency Server] Fire truck dispatched to "
                        + location
        );

        return "Fire truck dispatched successfully";
    }

    @Override
    public synchronized long getPhysicalTime()
            throws RemoteException {

        return physicalClock.getTime();
    }

    @Override
    public synchronized void adjustClock(long adjustment)
            throws RemoteException {

        physicalClock.adjust(adjustment);

        System.out.println(
                "[Emergency Server] Physical clock adjusted by "
                        + adjustment
                        + " ms"
        );
    }


    @Override
    public synchronized long getLamportTime()
            throws RemoteException {

        return lamportClock.getTime();
    }

    public void shutdown() {

        System.out.println(
                "[Emergency Server] Shutting down thread pool..."
        );

        executorService.shutdown();
    }
}