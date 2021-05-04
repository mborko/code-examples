package submission.simplechat.server;

/**
 * The mock client to test the user interface
 *
 * @author Kai Hoeher {@literal <khoeher@tgm.ac.at>}
 * @author Michael Borko {@literal <mborko@tgm.ac.at>}
 * @author Hans Brabenetz {@literal <hbrabenetz@tgm.ac.at>}
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MockClient {

    static int numberOfInsstances = 0;
    int instance = 0;
    int portNumber = 8888; //Integer.parseInt(args[0]);
    String server = "localhost";
    Socket clientSocket;
    PrintWriter ot;
    BufferedReader in;
    String inputLine = "";
    ArrayList<String> received;
    ArrayList<String> sent;

    public void send(String inMessage) {

        if(true){//clientSocket.isBound()){//isConnected()) {
            ot.println(inMessage);
            ot.flush();
            sent.add(inMessage);
            System.out.println("MocClient sent: " + inMessage);
        } else System.out.println(inMessage + " could not be sent.");

    }

    public MockClient(){
        instance = numberOfInsstances++;
        System.out.println("Hello from constructor. Instance "+ instance);

        received = new  ArrayList<>();
        sent = new  ArrayList<>();

        Thread clientThread = new Thread(()-> {
            //public void run() {
            try(Socket clientSocket = new Socket(server, portNumber)) {
                this.clientSocket = clientSocket;
                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream(),
                                StandardCharsets.UTF_8));
                ot = new PrintWriter(clientSocket.getOutputStream(), true);

                while ((inputLine = in.readLine()) != null) {

                    // new Thread(() -> received.add(inputLine));
                    received.add(inputLine);
                    System.out.println("MocClient Instance #" + instance + " receiveded: " + inputLine);
                }
            }
            catch(IOException e) {
                System.out.println("Hello from myIOException from ClientSocket. Ok when closing. ");
                e.printStackTrace();
            }
        });
        clientThread.start();

    }

}
