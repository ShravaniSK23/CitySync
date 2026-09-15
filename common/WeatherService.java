package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WeatherService extends Remote {

    String getWeather(
            String location,
            long lamportTimestamp
    ) throws RemoteException;

    String broadcastAlert(String message)
            throws RemoteException;

    String updateWeather(
            String location,
            String weather
    ) throws RemoteException;
}