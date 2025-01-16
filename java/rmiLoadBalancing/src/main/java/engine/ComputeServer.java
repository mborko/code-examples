package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import compute.Compute;
import compute.Loadbalancing;
import compute.Task;

public class ComputeServer implements Compute {
    private static final Logger log = Logger.getLogger(ComputeServer.class.getName());

    public static void main(String[] args) {
        ComputeServer server = new ComputeServer();
        server.startServer();
    }

    public void startServer() {
        Loadbalancing loadbalancer = null;
        Compute stub = null;
        try {
            stub = (Compute) UnicastRemoteObject.exportObject(this, 0);

            // Server am Loadbalancer registrieren
            Registry registry = LocateRegistry.getRegistry("localhost");
            loadbalancer = (Loadbalancing) registry.lookup("Loadbalancer");
            loadbalancer.register(stub);

            // get an exit from System.in and unexport the stub to safely close the ComputeEngine
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!reader.readLine().equals("exit")) ;

        } catch (Exception e) {
            log.severe("ComputeEngine exception: ");
            e.printStackTrace();
        } finally {
            try {
                loadbalancer.unregister(stub);
            } catch (RemoteException e) {
                // Loadbalancer not accessible anymore
            }
            try {
                UnicastRemoteObject.unexportObject(this, false);
            } catch (NoSuchObjectException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T executeTask(Task<T> t) {
        log.info(this.toString() + " says: I'm ready to rumble ... " + t.toString());
        return t.execute();
    }
}
