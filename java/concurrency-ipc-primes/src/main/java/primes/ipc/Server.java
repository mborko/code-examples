package primes.ipc;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Server {
    static ArrayList<Integer> primes;

    public static void main(String args[]) {

        try (BufferedReader br = new BufferedReader(new FileReader("primes.csv"))) {
            String line;
            SortedSet<Integer> temporary = new TreeSet<>();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) temporary.add(Integer.parseInt(value.replace(" ", "")));
            }
            primes = new ArrayList<>(temporary);
        } catch (FileNotFoundException fnfe) {
            System.err.println("File primes.csv not found!");
            System.exit(-1);
        } catch (IOException ioe) {
            System.err.println("I've got some really bad issues with your File primes.csv!");
            System.exit(-2);
        }
        System.out.println("Now I have knowledge of those primes:");
        System.out.println(primes.toString());

        PrimeSearcher ps = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));) {

            ps = new PrimeSearcher(Integer.parseInt(args[1]));
            ps.start();
            while (!reader.readLine().equals("exit")) ;
        } catch (IOException ioe) {
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("Please set the proper arguments!");
        } finally {
            if (ps != null) ps.shutdown();
        }
    }
}

class PrimeSearcher extends Thread {

    private DatagramSocket socket;
    private InetAddress address;
    private Integer port;
    private boolean running;
    private byte[] buf = new byte[256];

    public PrimeSearcher(int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket(this.port);
        address = InetAddress.getByName("localhost");
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("request " + received);
            String response = "n/a";
            try {
                Integer searchIndex = Server.primes.indexOf(Integer.parseInt(received));
                if (searchIndex >= 0 && searchIndex < Server.primes.size()-1)
                    response = Server.primes.get(searchIndex + 1).toString();
            } catch (Exception e) {}
            System.out.println("response " + response);
            buf = response.getBytes();

            packet = new DatagramPacket(buf, buf.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public void shutdown() {
        running = false;
        buf = "exit".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
