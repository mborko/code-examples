package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.nio.Buffer;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Logger;

import compute.Compute;
import compute.Loadbalancing;
import compute.Task;

public class ComputeEngine implements Compute {
    Loadbalancing lbEngine;
    private static final Logger log = Logger.getLogger(ComputeEngine.class.getName());

    public ComputeEngine() {}

    public static void main(String[] args) {
        ComputeEngine engine = new ComputeEngine();
        engine.startEngine();
    }

    public void startEngine() {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            //System.out.println(registry.toString());
            lbEngine = new LoadbalancerEngine();

            String name = "Compute";
            Compute stub = (Compute) UnicastRemoteObject.exportObject(this, 0);
            registry.rebind(name, stub);
            log.info("ComputeEngine bound");

            String name1 = "Loadbalancer";
            Loadbalancing loadbalancer = (Loadbalancing) UnicastRemoteObject.exportObject(lbEngine, 0);
            registry.rebind(name1, loadbalancer);
            log.info("Loadbalancer bound");

            // get an exit from System.in and unexport the stub to safely close the ComputeEngine
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!reader.readLine().equals("exit")) ;

        } catch (Exception e) {
            log.severe("ComputeEngine exception:");
            e.printStackTrace();
        } finally {
            try {
                UnicastRemoteObject.unexportObject(this, false);
                UnicastRemoteObject.unexportObject(lbEngine, false);
            } catch (NoSuchObjectException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T executeTask(Task<T> t) throws RemoteException {
        // Liste an Server mittels RoundRobin aufrufen und Rückgabe an Client weiterleiten!
        return ((Compute) lbEngine).executeTask(t);
    }
}

