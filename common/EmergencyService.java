package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EmergencyService extends Remote {

    String reportIncident(String location, String incidentType)
            throws RemoteException;

    String dispatchAmbulance(String location)
            throws RemoteException;

    String dispatchFireTruck(String location)
            throws RemoteException;
}