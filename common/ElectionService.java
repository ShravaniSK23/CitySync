package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ElectionService extends Remote {

    int getNodeId()
            throws RemoteException;

    String getNodeName()
            throws RemoteException;

    boolean isAlive() throws RemoteException;

    // -------------------------
    // Bully Algorithm
    // -------------------------

    void startBullyElection()
            throws RemoteException;

    void bullyElection(
            int initiatorId
    ) throws RemoteException;

    void bullyOk(
            int fromId
    ) throws RemoteException;

    void bullyCoordinator(
            int coordinatorId,
            String coordinatorName
    ) throws RemoteException;

    // -------------------------
    // Ring Algorithm
    // -------------------------

    void startRingElection()
            throws RemoteException;

    void ringElection(
            int initiatorId,
            int highestId,
            String path
    ) throws RemoteException;

    void ringCoordinator(
            int coordinatorId,
            String coordinatorName
    ) throws RemoteException;

    // -------------------------
    // Coordinator Information
    // -------------------------

    int getCoordinatorId()
            throws RemoteException;

    String getCoordinatorName()
            throws RemoteException;
}