package replication;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ReplicationExperimentMain {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> replicaPorts = Arrays.asList(8001, 8002, 8003);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CITYSYNC EXPERIMENT 5: REPLICATION & CONSISTENCY ===");
        
        // Step 1: Start Replica Nodes in Background
        for (int port : replicaPorts) {
            new Thread(new ReplicaServer(port)).start();
        }
        Thread.sleep(1000); // Allow server sockets to initialize

        ReplicationManager primaryManager = new ReplicationManager(replicaPorts);

        while (true) {
            System.out.println("\n--- REPLICATION MENU ---");
            System.out.println("1. Send Data Update (Normal Write)");
            System.out.println("2. Simulate Stale Update (Test Last-Write-Wins Consistency)");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline after choice

            if (choice == 3) {
                System.out.println("Exiting CitySync Replication Module.");
                System.exit(0);
            }

            System.out.print("Enter Resource ID (e.g., HOSPITAL_ZONE_1, TRAFFIC_SIGNAL_4): ");
            String resourceId = scanner.nextLine();

            System.out.print("Enter Status Value (e.g., Beds: 15, Congested): ");
            String statusValue = scanner.nextLine();

            long timestamp;
            if (choice == 1) {
                timestamp = System.currentTimeMillis();
                System.out.println("\n[Primary Node] Dispatching live update with timestamp: " + timestamp);
            } else {
                System.out.print("Enter past timestamp modifier in seconds (e.g., 10 to simulate 10s old update): ");
                long secondsOld = scanner.nextLong();
                scanner.nextLine(); // FIX: Consume leftover newline after reading long
                
                timestamp = System.currentTimeMillis() - (secondsOld * 1000);
                System.out.println("\n[Primary Node] Dispatching STALE update with simulated past timestamp: " + timestamp);
            }

            CityResource update = new CityResource(resourceId, statusValue, timestamp);
            primaryManager.replicateToAll(update);

            Thread.sleep(1000); // Pause briefly for clean console output
        }
    }
}