package replication;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ReplicationManager {
    private List<Integer> replicaPorts;

    public ReplicationManager(List<Integer> replicaPorts) {
        this.replicaPorts = replicaPorts;
    }

    // Synchronous Primary-Backup Replication Strategy
    public boolean replicateToAll(CityResource data) {
        int successfulAcks = 0;

        for (int port : replicaPorts) {
            try (Socket socket = new Socket("localhost", port);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                
                oos.writeObject(data);
                successfulAcks++;
            } catch (Exception e) {
                System.err.println("[Primary Node] Could not reach replica at port " + port);
            }
        }

        // Returns true if majority quorum acknowledged the update
        boolean quorumReached = successfulAcks >= (replicaPorts.size() / 2 + 1);
        System.out.println("[Primary Node] Replication completed. ACKs: " 
            + successfulAcks + "/" + replicaPorts.size() + " | Quorum Met: " + quorumReached);
        
        return quorumReached;
    }
}