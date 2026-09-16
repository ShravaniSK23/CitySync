package replication;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ReplicaServer implements Runnable {
    private int port;
    private ConcurrentHashMap<String, CityResource> localStore = new ConcurrentHashMap<>();

    public ReplicaServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Replica Server] Listening on port: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                CityResource incomingData = (CityResource) ois.readObject();

                // DATA CONSISTENCY MODEL: Last-Write-Wins (LWW)
                localStore.compute(incomingData.getResourceId(), (key, existingData) -> {
                    if (existingData == null || incomingData.getTimestamp() > existingData.getTimestamp()) {
                        System.out.println("  -> [Replica " + port + "] ACCEPTED Update: " 
                            + key + " = " + incomingData.getStatusValue() + " (Timestamp: " + incomingData.getTimestamp() + ")");
                        return incomingData;
                    } else {
                        System.out.println("  -> [Replica " + port + "] REJECTED Outdated Update for key: " + key);
                        return existingData;
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("[Replica " + port + "] Error: " + e.getMessage());
        }
    }
}