package loadbalancer;

import common.CentralRegistryClient;
import common.EmergencyService;
import common.LoadBalancerService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoadBalancerImpl
        extends UnicastRemoteObject
        implements LoadBalancerService {

    private final String[] emergencyServers = {
            "EmergencyServer1",
            "EmergencyServer2",
            "EmergencyServer3"
    };

    private int currentIndex = 0;
    private int requestCount = 0;

    public LoadBalancerImpl() throws RemoteException {
        super();

        System.out.println(
                "[Load Balancer] Started"
        );

        System.out.println(
                "[Load Balancer] Algorithm: Round Robin"
        );
    }

    /*
     * Round Robin selection
     */
    private synchronized EmergencyService getNextServer()
            throws RemoteException {

        requestCount++;

    String serverName =
            emergencyServers[currentIndex];

    System.out.println(
            "[Load Balancer] Request #" + requestCount
                    + " -> " + serverName
    );

    currentIndex =
            (currentIndex + 1)
                    % emergencyServers.length;

        try {

            EmergencyService server =
                    (EmergencyService)
                            CentralRegistryClient.lookup(
                                    serverName
                            );

            System.out.println(
                    "[Load Balancer] Selected: "
                            + serverName
            );

            return server;

        } catch (Exception e) {

            throw new RemoteException(
                    "Could not connect to "
                            + serverName,
                    e
            );
        }
    }

    @Override
    public String reportIncident(
            String location,
            String incidentType)
            throws RemoteException {

        System.out.println(
                "\n[Load Balancer] New request"
        );

        System.out.println(
                "Location: " + location
        );

        System.out.println(
                "Incident: " + incidentType
        );

        EmergencyService server =
                getNextServer();

        return server.reportIncident(
                location,
                incidentType
        );
    }

    @Override
    public String dispatchAmbulance(
            String location)
            throws RemoteException {

        System.out.println(
                "\n[Load Balancer] Ambulance request"
        );

        System.out.println(
                "Location: " + location
        );

        EmergencyService server =
                getNextServer();

        return server.dispatchAmbulance(
                location
        );
    }

    @Override
    public String dispatchFireTruck(
            String location)
            throws RemoteException {

        System.out.println(
                "\n[Load Balancer] Fire truck request"
        );

        System.out.println(
                "Location: " + location
        );

        EmergencyService server =
                getNextServer();

        return server.dispatchFireTruck(
                location
        );
    }
}