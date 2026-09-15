package common;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ElectionNodeImpl
        extends UnicastRemoteObject
        implements ElectionService {

    private final int nodeId;
    private final String nodeName;

    private volatile int coordinatorId = -1;
    private volatile String coordinatorName = "None";

    /*
     * Node IDs
     *
     * Emergency = 1
     * Hospital  = 2
     * Traffic   = 3
     * Weather   = 4
     */
    private static final int[] NODE_IDS = {
            1, 2, 3, 4
    };

    private static final String[] NODE_NAMES = {
            "Emergency",
            "Hospital",
            "Traffic",
            "Weather"
    };

    public ElectionNodeImpl(
            int nodeId,
            String nodeName
    ) throws RemoteException {

        super();

        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }

    // =========================================================
    // BASIC INFORMATION
    // =========================================================

    @Override
    public boolean isAlive()
            throws RemoteException {

        return true;
    }

    @Override
    public int getNodeId()
            throws RemoteException {

        return nodeId;
    }

    @Override
    public String getNodeName()
            throws RemoteException {

        return nodeName;
    }

    @Override
    public synchronized int getCoordinatorId()
            throws RemoteException {

        return coordinatorId;
    }

    @Override
    public synchronized String getCoordinatorName()
            throws RemoteException {

        return coordinatorName;
    }

    private synchronized void setCoordinator(
            int id,
            String name
    ) {

        coordinatorId = id;
        coordinatorName = name;
    }

    // =========================================================
    // ===================== BULLY ALGORITHM ===================
    // =========================================================

    @Override
    public void startBullyElection()
            throws RemoteException {

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "       BULLY ELECTION STARTED"
        );
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Election starter : "
                        + nodeName
                        + " (ID "
                        + nodeId
                        + ")"
        );

        System.out.println();

        boolean higherNodeAlive = false;

        /*
         * Contact every node having a higher ID.
         */
        for (int id : NODE_IDS) {

            if (id <= nodeId) {
                continue;
            }

            String higherNodeName =
                    getNodeNameById(id);

            try {

                ElectionService higherNode =
                        lookupNode(higherNodeName);

                System.out.println(
                        "["
                                + nodeName
                                + "] -> "
                                + higherNodeName
                                + " : ELECTION"
                );

                /*
                 * If the call succeeds, the higher node
                 * is alive.
                 */
                higherNode.bullyOk(nodeId);

                higherNodeAlive = true;

            } catch (Exception e) {

                /*
                 * Higher node is unavailable.
                 */
                System.out.println(
                        "["
                                + nodeName
                                + "] -> "
                                + higherNodeName
                                + " : NO RESPONSE"
                );
            }
        }

        /*
         * If no higher node is alive, this node becomes
         * the coordinator.
         */
        if (!higherNodeAlive) {

            becomeBullyCoordinator();

        } else {

            System.out.println();

            System.out.println(
                    "["
                            + nodeName
                            + "] Higher-ID active node found."
            );

            System.out.println(
                    "["
                            + nodeName
                            + "] Waiting for coordinator..."
            );
        }
    }

    @Override
    public void bullyElection(
            int initiatorId
    ) throws RemoteException {

        /*
         * Start a new election asynchronously.
         */
        new Thread(() -> {

            try {

                startBullyElection();

            } catch (Exception e) {

                System.out.println(
                        "["
                                + nodeName
                                + "] Bully election failed: "
                                + e.getMessage()
                );
            }

        }).start();
    }

    @Override
    public void bullyOk(
            int fromId
    ) throws RemoteException {

        System.out.println(
                "["
                        + nodeName
                        + "] received OK from node ID "
                        + fromId
        );

        /*
         * A higher node that receives an election message
         * starts its own election.
         */
        new Thread(() -> {

            try {

                startBullyElection();

            } catch (Exception e) {

                System.out.println(
                        "["
                                + nodeName
                                + "] Could not continue Bully election."
                );
            }

        }).start();
    }

    private void becomeBullyCoordinator() {

        setCoordinator(
                nodeId,
                nodeName
        );

        System.out.println();
        System.out.println(
                "**********************************************"
        );

        System.out.println(
                "*** "
                        + nodeName
                        + " BECOMES MASTER / COORDINATOR ***"
        );

        System.out.println(
                "*** Coordinator ID: "
                        + nodeId
                        + " ***"
        );

        System.out.println(
                "**********************************************"
        );

        /*
         * Inform all active nodes.
         */
        for (int id : NODE_IDS) {

            String name =
                    getNodeNameById(id);

            try {

                ElectionService node =
                        lookupNode(name);

                node.bullyCoordinator(
                        nodeId,
                        nodeName
                );

            } catch (Exception e) {

                System.out.println(
                        "["
                                + nodeName
                                + "] Could not notify "
                                + name
                                + " (node unavailable)"
                );
            }
        }

        System.out.println();
    }

    @Override
    public void bullyCoordinator(
            int newCoordinatorId,
            String newCoordinatorName
    ) throws RemoteException {

        setCoordinator(
                newCoordinatorId,
                newCoordinatorName
        );

        System.out.println(
                "["
                        + nodeName
                        + "] Coordinator updated to: "
                        + newCoordinatorName
                        + " (ID "
                        + newCoordinatorId
                        + ")"
        );
    }

    // =========================================================
    // ====================== RING ALGORITHM ==================
    // =========================================================

    @Override
    public void startRingElection()
            throws RemoteException {

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "        RING ELECTION STARTED"
        );
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Election starter : "
                        + nodeName
                        + " (ID "
                        + nodeId
                        + ")"
        );

        System.out.println();

        /*
         * Find the next ACTIVE node.
         */
        int nextId =
                findNextAvailableNodeAfter(nodeId);

        /*
         * No other node is alive.
         */
        if (nextId == -1) {

            setCoordinator(
                    nodeId,
                    nodeName
            );

            System.out.println(
                    "["
                            + nodeName
                            + "] is the only active node."
            );

            System.out.println(
                    "MASTER / COORDINATOR: "
                            + nodeName
            );

            return;
        }

        String nextName =
                getNodeNameById(nextId);

        try {

            ElectionService nextNode =
                    lookupNode(nextName);

            System.out.println(
                    "["
                            + nodeName
                            + "] -> "
                            + nextName
                            + " : ELECTION"
            );

            /*
             * Start the ring message.
             *
             * The current node's ID is initially
             * the highest ID seen.
             */
            nextNode.ringElection(
                    nodeId,
                    nodeId,
                    nodeName
            );

        } catch (Exception e) {

            /*
             * The next node may have gone down after
             * we checked it.
             *
             * Skip it and continue around the ring.
             */
            System.out.println(
                    "["
                            + nodeName
                            + "] "
                            + nextName
                            + " is unavailable. Skipping..."
            );

            forwardRingElection(
                    nodeId,
                    nodeId,
                    nodeName,
                    nextId
            );
        }
    }

    @Override
    public void ringElection(
            int initiatorId,
            int highestId,
            String path
    ) throws RemoteException {

        /*
         * This node has received the election message.
         */

        int currentHighest =
                Math.max(
                        highestId,
                        nodeId
                );

        String currentPath = path;

        /*
         * Add this node to the path.
         */
        if (!path.endsWith(nodeName)) {

            currentPath =
                    path
                            + " -> "
                            + nodeName;
        }

        System.out.println(
                "["
                        + nodeName
                        + "] received RING message. "
                        + "Highest ID = "
                        + currentHighest
        );

        /*
         * If the message has returned to the initiator,
         * the ring traversal is complete.
         */
        if (
                nodeId == initiatorId
                        && !path.equals(nodeName)
        ) {

            String winnerName =
                    getNodeNameById(currentHighest);

            System.out.println();

            System.out.println(
                    "----------------------------------------------"
            );

            System.out.println(
                    "Ring election completed."
            );

            System.out.println(
                    "Election path:"
            );

            System.out.println(
                    currentPath
            );

            System.out.println(
                    "Highest ACTIVE ID: "
                            + currentHighest
            );

            System.out.println(
                    "----------------------------------------------"
            );

            System.out.println();

            System.out.println(
                    "**********************************************"
            );

            System.out.println(
                    "*** "
                            + winnerName
                            + " BECOMES MASTER / COORDINATOR ***"
            );

            System.out.println(
                    "*** Coordinator ID: "
                            + currentHighest
                            + " ***"
            );

            System.out.println(
                    "**********************************************"
            );

            System.out.println();

            /*
             * Announce the winner to all ACTIVE nodes.
             */
            announceRingCoordinator(
                    currentHighest,
                    winnerName
            );

            return;
        }

        /*
         * Find the next ACTIVE node.
         */
        int nextId =
                findNextAvailableNodeAfter(nodeId);

        /*
         * No other active node.
         */
        if (nextId == -1) {

            announceRingCoordinator(
                    currentHighest,
                    getNodeNameById(currentHighest)
            );

            return;
        }

        String nextName =
                getNodeNameById(nextId);

        try {

            ElectionService nextNode =
                    lookupNode(nextName);

            System.out.println(
                    "["
                            + nodeName
                            + "] -> "
                            + nextName
                            + " : ELECTION"
            );

            nextNode.ringElection(
                    initiatorId,
                    currentHighest,
                    currentPath
            );

        } catch (Exception e) {

            /*
             * Node failed between discovery and invocation.
             */
            System.out.println(
                    "["
                            + nodeName
                            + "] "
                            + nextName
                            + " is unavailable. Skipping..."
            );

            forwardRingElection(
                    initiatorId,
                    currentHighest,
                    currentPath,
                    nextId
            );
        }
    }

    /*
     * Skip a failed node and find the next active node.
     */
    private void forwardRingElection(
            int initiatorId,
            int highestId,
            String path,
            int failedNodeId
    ) {

        int nextId =
                findNextAvailableNodeAfter(
                        failedNodeId
                );

        /*
         * No active node remains.
         */
        if (nextId == -1) {

            announceRingCoordinator(
                    highestId,
                    getNodeNameById(highestId)
            );

            return;
        }

        String nextName =
                getNodeNameById(nextId);

        try {

            ElectionService nextNode =
                    lookupNode(nextName);

            System.out.println(
                    "["
                            + nodeName
                            + "] -> "
                            + nextName
                            + " : ELECTION"
            );

            nextNode.ringElection(
                    initiatorId,
                    highestId,
                    path
            );

        } catch (Exception e) {

            /*
             * That node also failed.
             *
             * Continue searching from there.
             */
            System.out.println(
                    "["
                            + nodeName
                            + "] "
                            + nextName
                            + " is also unavailable. Skipping..."
            );

            forwardRingElection(
                    initiatorId,
                    highestId,
                    path,
                    nextId
            );
        }
    }

    @Override
    public void ringCoordinator(
            int newCoordinatorId,
            String newCoordinatorName
    ) throws RemoteException {

        setCoordinator(
                newCoordinatorId,
                newCoordinatorName
        );

        System.out.println(
                "["
                        + nodeName
                        + "] Coordinator updated to: "
                        + newCoordinatorName
                        + " (ID "
                        + newCoordinatorId
                        + ")"
        );
    }

    private void announceRingCoordinator(
            int winnerId,
            String winnerName
    ) {

        setCoordinator(
                winnerId,
                winnerName
        );

        System.out.println();

        System.out.println(
                "Announcing Ring coordinator: "
                        + winnerName
                        + " (ID "
                        + winnerId
                        + ")"
        );

        /*
         * Notify every node that is currently reachable.
         */
        for (int id : NODE_IDS) {

            String name =
                    getNodeNameById(id);

            try {

                ElectionService node =
                        lookupNode(name);

                node.ringCoordinator(
                        winnerId,
                        winnerName
                );

            } catch (Exception e) {

                System.out.println(
                        "["
                                + nodeName
                                + "] Could not notify "
                                + name
                                + " (node unavailable)"
                );
            }
        }
    }

    // =========================================================
    // ====================== UTILITIES ========================
    // =========================================================

    private ElectionService lookupNode(
            String name
    ) throws Exception {

        return (ElectionService)
                CentralRegistryClient.lookup(
                        name + "Election"
                );
    }

    private String getNodeNameById(
            int id
    ) {

        for (
                int i = 0;
                i < NODE_IDS.length;
                i++
        ) {

            if (NODE_IDS[i] == id) {

                return NODE_NAMES[i];
            }
        }

        return "Unknown";
    }

    /*
     * Finds the next ACTIVE node in the logical ring.
     *
     * Ring:
     *
     * Emergency -> Hospital
     * Hospital  -> Traffic
     * Traffic   -> Weather
     * Weather   -> Emergency
     *
     * If a node is down, it is skipped.
     */
    private int findNextAvailableNodeAfter(
        int currentId
) {

    int currentIndex = -1;

    for (
            int i = 0;
            i < NODE_IDS.length;
            i++
    ) {

        if (NODE_IDS[i] == currentId) {

            currentIndex = i;

            break;
        }
    }

    if (currentIndex == -1) {
        return -1;
    }

    for (
            int step = 1;
            step < NODE_IDS.length;
            step++
    ) {

        int candidateIndex =
                (currentIndex + step)
                        % NODE_IDS.length;

        int candidateId =
                NODE_IDS[candidateIndex];

        String candidateName =
                getNodeNameById(candidateId);

        try {

            ElectionService candidate =
                    lookupNode(candidateName);

            // IMPORTANT:
            // lookup alone is not enough.
            // Verify that the server is actually alive.
            if (candidate.isAlive()) {

                return candidateId;
            }

        } catch (Exception e) {

            System.out.println(
                    "[Election] "
                            + candidateName
                            + " is DOWN. Skipping."
            );
        }
    }

    return -1;
}
}