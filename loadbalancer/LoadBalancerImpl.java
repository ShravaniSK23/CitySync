package loadbalancer;

import common.CentralRegistryClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancerImpl
        extends UnicastRemoteObject
        implements LoadBalancerService {

    private int currentIndex = 0;

    public LoadBalancerImpl()
            throws RemoteException {

        super();
    }

    /*
     * Finds workers that are actually reachable.
     */
    private synchronized List<String> getAvailableWorkers() {

        List<String> workers = new ArrayList<>();

        String[] possibleWorkers = {
                "Worker1",
                "Worker2",
                "Worker3"
        };

        for (String workerName : possibleWorkers) {

            try {

                WorkerService worker =
                        (WorkerService)
                                CentralRegistryClient.lookup(
                                        workerName
                                );

                /*
                 * Actually contact the worker.
                 * This detects stale RMI registrations.
                 */
                worker.getWorkerName();

                workers.add(workerName);

                System.out.println(
                        "[Load Balancer] "
                                + workerName
                                + " is AVAILABLE"
                );

            } catch (Exception e) {

                System.out.println(
                        "[Load Balancer] "
                                + workerName
                                + " is UNAVAILABLE"
                );
            }
        }

        return workers;
    }

    /*
     * Round-Robin selection.
     */
    @Override
    public synchronized String getNextWorker()
            throws RemoteException {

        List<String> workers =
                getAvailableWorkers();

        if (workers.isEmpty()) {

            throw new RemoteException(
                    "No workers are currently available."
            );
        }

        /*
         * Prevent index from going outside
         * the current available-worker list.
         */
        if (currentIndex >= workers.size()) {
            currentIndex = 0;
        }

        String selectedWorker =
                workers.get(currentIndex);

        currentIndex =
                (currentIndex + 1)
                        % workers.size();

        System.out.println(
                "[Load Balancer] Selected: "
                        + selectedWorker
        );

        return selectedWorker;
    }

    @Override
    public String processRequest(String request)
            throws RemoteException {

        try {

            String workerName =
                    getNextWorker();

            WorkerService worker =
                    (WorkerService)
                            CentralRegistryClient.lookup(
                                    workerName
                            );

            System.out.println(
                    "[Load Balancer] Routing request: "
                            + request
                            + " → "
                            + workerName
            );

            return worker.processRequest(request);

        } catch (Exception e) {

            throw new RemoteException(
                    "Failed to process request",
                    e
            );
        }
    }

    @Override
    public String getAlgorithm()
            throws RemoteException {

        return "Round Robin";
    }
}