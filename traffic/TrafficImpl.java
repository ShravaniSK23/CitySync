package traffic;

import common.TrafficService;
import common.ClockService;

import clock.PhysicalClock;
import clock.LamportClock;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TrafficImpl extends UnicastRemoteObject
        implements TrafficService, ClockService {

    private final PhysicalClock physicalClock =
            new PhysicalClock(-3000);

    private final LamportClock lamportClock =
            new LamportClock();

    public TrafficImpl() throws RemoteException {
        super();

        System.out.println(
                "[Traffic Server] Started."
        );
    }

    @Override
public long getTime() throws RemoteException {
    return physicalClock.getTime();
}

    @Override
    public synchronized String createGreenCorridor(
            String location,
            long lamportTimestamp
    ) throws RemoteException {

        long updatedLamportTime =
                lamportClock.receiveEvent(lamportTimestamp);

        System.out.println();
        System.out.println(
                "[LAMPORT] Traffic received event"
        );

        System.out.println(
                "[LAMPORT] Received timestamp = "
                        + lamportTimestamp
        );

        System.out.println(
                "[LAMPORT] Traffic clock updated to = "
                        + updatedLamportTime
        );

        System.out.println(
                "[Traffic Server] Creating green corridor at "
                        + location
        );

        return "Green corridor created at " + location;
    }

    @Override
    public String getTrafficStatus(String location)
            throws RemoteException {

        System.out.println(
                "[Traffic Server] Checking traffic at "
                        + location
        );

        return "Traffic at " + location + ": Heavy";
    }

    @Override
    public String updateRoadStatus(
            String location,
            String status
    ) throws RemoteException {

        System.out.println(
                "[Traffic Server] Road status updated at "
                        + location
                        + " -> "
                        + status
        );

        return "Road status updated successfully";
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
                "[Traffic Server] Physical clock adjusted by "
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