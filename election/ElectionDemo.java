package election;

import common.CentralRegistryClient;
import common.ElectionService;

public class ElectionDemo {

    public static void main(String[] args) {

        if (args.length < 2) {

            System.out.println();
            System.out.println("Usage:");
            System.out.println(
                    "java election.ElectionDemo bully <NodeName>"
            );
            System.out.println(
                    "java election.ElectionDemo ring <NodeName>"
            );

            System.out.println();
            System.out.println("Available nodes:");
            System.out.println("Emergency");
            System.out.println("Hospital");
            System.out.println("Traffic");
            System.out.println("Weather");

            return;
        }

        String algorithm =
                args[0].toLowerCase();

        String initiatorName =
                args[1];

        try {

            /*
             * Look up the node that will start
             * the election.
             */
            ElectionService initiator =
                    (ElectionService)
                            CentralRegistryClient.lookup(
                                    initiatorName + "Election"
                            );

            int initiatorId =
                    initiator.getNodeId();

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "           LEADER ELECTION DEMO"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "Algorithm : "
                            + algorithm.toUpperCase()
            );

            System.out.println(
                    "Initiator  : "
                            + initiatorName
                            + " (ID "
                            + initiatorId
                            + ")"
            );

            System.out.println(
                    "----------------------------------------------"
            );

            /*
             * Start the selected election algorithm.
             */
            if (algorithm.equals("bully")) {

                System.out.println(
                        "Starting Bully Algorithm..."
                );

                initiator.startBullyElection();

            } else if (algorithm.equals("ring")) {

                System.out.println(
                        "Starting Ring Algorithm..."
                );

                initiator.startRingElection();

            } else {

                System.out.println();
                System.out.println(
                        "ERROR: Unknown election algorithm."
                );

                System.out.println(
                        "Use: bully or ring"
                );

                return;
            }

            /*
             * Give the distributed RMI nodes some time
             * to complete the election and propagate
             * the coordinator announcement.
             */
            Thread.sleep(2000);

            /*
             * Read the final coordinator from the
             * initiating node.
             */
            int coordinatorId =
                    initiator.getCoordinatorId();

            String coordinatorName =
                    initiator.getCoordinatorName();

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "              ELECTION RESULT"
            );
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "Algorithm        : "
                            + algorithm.toUpperCase()
            );

            System.out.println(
                    "Election Starter  : "
                            + initiatorName
                            + " (ID "
                            + initiatorId
                            + ")"
            );

            System.out.println(
                    "----------------------------------------------"
            );

            if (coordinatorId == -1) {

                System.out.println(
                        "MASTER / COORDINATOR: NOT DETERMINED"
                );

                System.out.println(
                        "The election may still be running."
                );

            } else {

                System.out.println();
                System.out.println(
                        "MASTER / COORDINATOR"
                );

                System.out.println(
                        "Name : "
                                + coordinatorName
                );

                System.out.println(
                        "ID   : "
                                + coordinatorId
                );

                System.out.println();

                System.out.println(
                        ">>> CURRENT MASTER: "
                                + coordinatorName
                                + " <<<"
                );
            }

            System.out.println(
                    "=============================================="
            );
            System.out.println();

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "Election demo failed."
            );

            System.out.println(
                    "Reason: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}