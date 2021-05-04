package primes.ipc;

import java.io.IOException;
import java.net.*;

public class Client {
    private DatagramSocket socket;
    private InetAddress address;
    private Integer port;

    private byte[] buf;

    public Client(String host, Integer port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        this.port = port;
        address = InetAddress.getByName(host);
    }

    public Integer primeSearch(Integer msg) throws IOException {
        buf = msg.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        if (received.equals("n/a")) return null;
        return Integer.parseInt(received);
    }

    public void close() {
        socket.close();
    }

    public static void main(String args[]) {
        try {
            Client client = new Client("localhost", 1234);
            System.out.println(client.primeSearch(113));
        } catch (Exception e) {
            System.err.println("Houston, we have a problem!");
            e.printStackTrace();
        }
    }
}
