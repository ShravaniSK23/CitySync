package hospital;

import common.ClockService;
import common.HospitalService;

import clock.PhysicalClock;
import clock.LamportClock;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HospitalImpl extends UnicastRemoteObject
        implements HospitalService, ClockService {

    private int availableBeds = 10;

    private final PhysicalClock physicalClock =
            new PhysicalClock(5000);

    private final LamportClock lamportClock =
            new LamportClock();

    public HospitalImpl() throws RemoteException {
        super();

        System.out.println(
                "[Hospital Server] Started."
        );
    }

    @Override
    public synchronized String reserveBed(
            String location,
            long lamportTimestamp
    ) throws RemoteException {

        long updatedLamportTime =
                lamportClock.receiveEvent(lamportTimestamp);

        System.out.println();
        System.out.println(
                "[LAMPORT] Hospital received event"
        );

        System.out.println(
                "[LAMPORT] Received timestamp = "
                        + lamportTimestamp
        );

        System.out.println(
                "[LAMPORT] Hospital clock updated to = "
                        + updatedLamportTime
        );

        if (availableBeds > 0) {

            availableBeds--;

            System.out.println(
                    "[Hospital Server] Bed reserved for patient from "
                            + location
            );

            System.out.println(
                    "[Hospital Server] Available beds: "
                            + availableBeds
            );

            return "Hospital bed reserved successfully";
        }

        return "No beds available";
    }

    @Override
    public String getHospitalStatus()
            throws RemoteException {

        return "Hospital A - Available Beds: "
                + availableBeds;
    }

    @Override
    public synchronized String releaseBed(String location)
            throws RemoteException {

        availableBeds++;

        System.out.println(
                "[Hospital Server] Bed released from "
                        + location
        );

        return "Hospital bed released successfully";
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
                "[Hospital Server] Physical clock adjusted by "
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