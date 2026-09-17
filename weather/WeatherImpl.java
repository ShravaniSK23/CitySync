package weather;

import common.WeatherService;
import common.ClockService;

import clock.PhysicalClock;
import clock.LamportClock;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WeatherImpl extends UnicastRemoteObject
        implements WeatherService, ClockService {

    private final PhysicalClock physicalClock =
            new PhysicalClock(8000);

    private final LamportClock lamportClock =
            new LamportClock();

    public WeatherImpl() throws RemoteException {
        super();

        System.out.println(
                "[Weather Server] Started."
        );
    }

    @Override
public long getTime() throws RemoteException {
    return physicalClock.getTime();
}

    @Override
    public synchronized String getWeather(
            String location,
            long lamportTimestamp
    ) throws RemoteException {

        long updatedLamportTime =
                lamportClock.receiveEvent(lamportTimestamp);

        System.out.println();
        System.out.println(
                "[LAMPORT] Weather received event"
        );

        System.out.println(
                "[LAMPORT] Received timestamp = "
                        + lamportTimestamp
        );

        System.out.println(
                "[LAMPORT] Weather clock updated to = "
                        + updatedLamportTime
        );

        System.out.println(
                "[Weather Server] Weather requested for "
                        + location
        );

        return "Weather at " + location + ": Heavy Rain";
    }

    @Override
    public String broadcastAlert(String message)
            throws RemoteException {

        System.out.println(
                "[Weather Server] ALERT: "
                        + message
        );

        return "Weather alert broadcast successfully";
    }

    @Override
    public String updateWeather(
            String location,
            String weather
    ) throws RemoteException {

        System.out.println(
                "[Weather Server] Weather updated at "
                        + location
                        + " -> "
                        + weather
        );

        return "Weather updated successfully";
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
                "[Weather Server] Physical clock adjusted by "
                        + adjustment
                        + " ms"
        );
    }

    @Override
    public synchronized long getLamportTime()
            throws RemoteException {

        return lamportClock.getTime();
    }
}