package client;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigInteger;
import java.util.logging.Logger;

import compute.Compute;
import compute.Task;
import engine.ComputeEngine;

import static java.lang.System.exit;

public class ComputeTask {
    private static final Logger log = Logger.getLogger(ComputeTask.class.getName());

    public static void main(String args[]) {
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name);

            Task task = null;
            switch (args[1]) {
                case "Fibonacci":
                    task = (Fibonacci) new Fibonacci(Integer.parseInt(args[2]));
                    BigInteger fibonacci = (BigInteger) comp.executeTask(task);
                    log.info(""+fibonacci);
                    break;
                case "Pi":
                    task = (Pi) new Pi(Integer.parseInt(args[2]));
                    BigDecimal pi = (BigDecimal) comp.executeTask(task);
                    log.info(""+pi);
                    break;
                default:
                    log.info("Please declare your task properly!");
                    exit(1);
            }
        } catch (Exception e) {
            log.severe("ComputeClient exception:");
            e.printStackTrace();
        }
    }
}
