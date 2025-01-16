package engine;

import compute.Compute;
import compute.Loadbalancing;
import compute.Task;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class LoadbalancerEngine implements Loadbalancing, Compute {
    private static final Logger log = Logger.getLogger(ComputeEngine.class.getName());

    private Map<Integer, Compute> computingServers = new ConcurrentHashMap<>();
    private SortedSet<Integer> counter = new TreeSet<>();

    AtomicInteger index = new AtomicInteger();

    @Override
    public synchronized void register(Compute stub) {
        Integer serverIndex;

        try {
            //der letzte Index wird ausgelesen und um 1 erhöht
            serverIndex = counter.last() + 1;
        } catch (NoSuchElementException nsee) {
            serverIndex = 0;
            index.set(0);
        }
        try {
            //der neue Index wird zum counter hinzugefügt
            counter.add(serverIndex);
            //Serverindex + Stub werden gespeichert
            computingServers.put(serverIndex, stub);

        } catch (Exception e) {
            // TODO Werte wieder rückgängig machen, Rückgabewerte für Server
        }
        log.info("Added server[" + serverIndex + "]: " + stub.toString());
    }

    @Override
    public synchronized void unregister(Compute stub) {
        Integer serverIndex = null;
        try {
            //Index des übergebenen Stubs wird in der Map gesucht
            for (Map.Entry<Integer, Compute> entry : computingServers.entrySet()) {
                if (stub.equals(entry.getValue())) {
                    serverIndex = entry.getKey();
                    break;
                }
            }
            //und aus dem counter entfernt
            counter.remove(serverIndex);
            //server wird entfernt
            computingServers.remove(serverIndex, stub);
        } catch (NullPointerException npe) {
            log.severe("This ComputeServer was never connected to this Loadbalancer");
        }
        log.info("Removed server[" + serverIndex + "]: " + stub.toString());
    }

    public <T> T executeTask(Task<T> t) throws RemoteException {
        if (!computingServers.isEmpty())
            return computingServers.get(counter.iterator().next() + index.getAndIncrement() % computingServers.size()).executeTask(t);
        return null;
    }
}
