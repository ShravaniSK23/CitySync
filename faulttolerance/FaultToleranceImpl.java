package faulttolerance;

import common.CentralRegistryClient;
import loadbalancer.WorkerService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class FaultToleranceImpl
        extends UnicastRemoteObject
        implements FaultToleranceService {

    private int currentIndex = 0;

    private final String[] workers = {
            "Worker1",
            "Worker2",
            "Worker3"
    };

    public FaultToleranceImpl()
            throws RemoteException {
        super();
    }

    /*
     * Checks whether a worker is actually reachable.
     */
    private synchronized boolean isWorkerAvailable(
            String workerName) {

        try {

            WorkerService worker =
                    (WorkerService)
                            CentralRegistryClient.lookup(
                                    workerName
                            );

            /*
             * Actually contact the worker.
             * This prevents stale RMI registrations
             * from being treated as healthy.
             */
            worker.getWorkerName();

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    /*
     * Returns all currently available workers.
     */
    private synchronized List<String> getAvailableWorkers() {

        List<String> availableWorkers =
                new ArrayList<>();

        for (String workerName : workers) {

            if (isWorkerAvailable(workerName)) {

                availableWorkers.add(workerName);

                System.out.println(
                        "[Fault Tolerance] "
                                + workerName
                                + " : ACTIVE"
                );

            } else {

                System.out.println(
                        "[Fault Tolerance] "
                                + workerName
                                + " : FAILED"
                );
            }
        }

        return availableWorkers;
    }

    /*
     * Explicitly checks one worker.
     */
    @Override
    public synchronized String checkWorker(
            String workerName)
            throws RemoteException {

        if (isWorkerAvailable(workerName)) {

            return workerName
                    + " is ACTIVE";

        } else {

            return workerName
                    + " is FAILED";
        }
    }

    /*
     * Processes a request using an available worker.
     *
     * If the currently selected worker is unavailable,
     * another available worker is selected.
     */
    @Override
    public synchronized String processRequest(
            String request)
            throws RemoteException {

        List<String> availableWorkers =
                getAvailableWorkers();

        if (availableWorkers.isEmpty()) {

            throw new RemoteException(
                    "FAULT TOLERANCE FAILED: "
                            + "No workers are available."
            );
        }

        /*
         * Keep index within the currently available
         * worker list.
         */
        if (currentIndex >= availableWorkers.size()) {

            currentIndex = 0;
        }

        String selectedWorker =
                availableWorkers.get(currentIndex);

        currentIndex =
                (currentIndex + 1)
                        % availableWorkers.size();

        System.out.println();
        System.out.println(
                "[Fault Tolerance] Processing request"
        );

        System.out.println(
                "[Fault Tolerance] Selected backup/active worker: "
                        + selectedWorker
        );

        try {

            WorkerService worker =
                    (WorkerService)
                            CentralRegistryClient.lookup(
                                    selectedWorker
                            );

            String result =
                    worker.processRequest(
                            request
                    );

            return result;

        } catch (Exception e) {

            /*
             * The worker may have failed between
             * the health check and the actual request.
             *
             * Try the remaining workers.
             */
            System.out.println(
                    "[Fault Tolerance] "
                            + selectedWorker
                            + " failed during processing."
            );

            for (String workerName :
                    availableWorkers) {

                if (workerName.equals(
                        selectedWorker)) {

                    continue;
                }

                try {

                    WorkerService backupWorker =
                            (WorkerService)
                                    CentralRegistryClient.lookup(
                                            workerName
                                    );

                    System.out.println(
                            "[Fault Tolerance] "
                                    + "Failing over to "
                                    + workerName
                    );

                    return backupWorker.processRequest(
                            request
                    );

                } catch (Exception backupException) {

                    System.out.println(
                            "[Fault Tolerance] "
                                    + workerName
                                    + " also unavailable."
                    );
                }
            }

            throw new RemoteException(
                    "Request failed: "
                            + "no worker could process it."
            );
        }
    }

    /*
     * Returns a readable status of all workers.
     */
    @Override
    public synchronized String getStatus()
            throws RemoteException {

        StringBuilder status =
                new StringBuilder();

        status.append(
                "\n========== WORKER STATUS ==========\n"
        );

        for (String workerName : workers) {

            if (isWorkerAvailable(workerName)) {

                status.append(
                        workerName
                                + " : ACTIVE\n"
                );

            } else {

                status.append(
                        workerName
                                + " : FAILED\n"
                );
            }
        }

        status.append(
                "===================================\n"
        );

        return status.toString();
    }
}