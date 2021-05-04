package simplechat.communication.socket.server;

import simplechat.communication.MessageProtocol;
import simplechat.server.SimpleChat;

import static java.util.logging.Level.*;
import static simplechat.communication.MessageProtocol.Commands.EXIT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * SimpleChatServer listens to incoming SimpleChatClients with the choosen communication protocol and initiates a UI.
 * <br>
 * Default settings for the main attributes will be: host="localhost" port=5050 and backlog=5
 */
public class SimpleChatServer extends Thread {

    private Integer port = 5050;
    private String host = "localhost";
    private final Integer backlog = 5;
    private ServerSocket serverSocket = null;

    private boolean listening = false;
    private SimpleChat server;

    private ConcurrentHashMap<ClientWorker, String> workerList = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();


    /**
     * Initializes host, port and callback for UserInterface interactions.
     *
     * @param host   String representation of hostname, on which the server should listen
     * @param port   Integer for the listening port
     * @param server UserInterface callback reference for user interactions
     */
    public SimpleChatServer(String host, Integer port, SimpleChat server) {
        if (host != null) this.host = host;
        if (port != null) this.port = port;
        this.server = server;
        SimpleChat.serverLogger.log(INFO, "Init: host=" + this.host + " port=" + this.port);
    }

    /**
     * Initiating the ServerSocket with already defined Parameters and starts accepting incoming
     * requests. If client connects to the ServerSocket a new ClientWorker will be created and passed
     * to the ExecutorService for immediate concurrent action.
     */
    public void run() {
        SimpleChat.serverLogger.log(INFO, "... starting Thread ...");

        try {
            serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(host));
            listening = true;
            SimpleChat.serverLogger.log(INFO, "ServerSocket is initialized!");

            while (listening) {
                ClientWorker worker = new ClientWorker(serverSocket.accept(), this);
                if (listening) {
                    workerList.put(worker, server.addClient(""));
                    executorService.execute(worker);
                }
            }
        } catch (IOException io) {
            SimpleChat.serverLogger.log(SEVERE, io.toString());
        }
    }

    /**
     * Callback method for client worker to inform server of new message arrival
     *
     * @param plainMessage MessageText sent to server without Client information
     * @param sender       {@link ClientWorker} which received the message
     */
    public void received(String plainMessage, ClientWorker sender) {
        String message = MessageProtocol.textMessage(plainMessage, workerList.get(sender));
        server.incomingMessage(message);
    }

    /**
     * Sending messages to clients through communication framework
     *
     * @param message MessageText with sender ChatName
     */
    public void send(String message) {
        workerList.forEach((k, v) -> k.send(message));
    }

    /**
     * Sending message to one client through communication framework
     *
     * @param message  MessageText with sender ChatName
     * @param receiver ChatName of receiving Client
     */
    public void send(String message, Object receiver) {
        try {
            getWorker(receiver).send(message);
        } catch (Exception e) {
            SimpleChat.serverLogger.log(SEVERE, e.toString());
        }
    }

    private ClientWorker getWorker(Object chatName) {
        return workerList.entrySet()
                .stream()
                .filter(entry -> chatName.equals(entry.getValue()))
                .map(ConcurrentHashMap.Entry::getKey)
                .findFirst().get();
    }

    /**
     * ClientWorker has the possibility to change the ChatName. This method asks the UI
     * to rename the Client and stores the returned Name in the ClientWorker-Collection
     *
     * @param chatName new Name of Client
     * @param worker   ClientWorker Thread which was initiating the renaming
     */
    void setName(String chatName, ClientWorker worker) {
        // server.renameClient checks if name is unique and returns the name which was set
        // send("User <" + workerList.get(worker) + "> changed name to <" + chatName + ">");
        workerList.replace(worker, server.renameClient(workerList.get(worker), chatName));
    }

    /**
     * Remove only this worker from the list,
     * shutdown the ClientWorker and also inform GUI about removal.
     *
     * @param worker ClientWorker which should be removed
     */
    void removeClient(ClientWorker worker) {
        if (workerList.containsKey(worker)) {
            server.removeClient(workerList.get(worker));
            worker.shutdown();
            workerList.remove(worker);
        }
    }

    /**
     * Gets the ClientWorker of the given chatName and calls the private Method {@link #removeClient(String)}
     * This method will remove the worker from the list shutdown the ClientWorker and also inform GUI about removal.
     *
     * @param chatName Client name which should be removed
     */
    public void removeClient(String chatName) {
        removeClient(getWorker(chatName));
    }

    /**
     * Clean shutdown of all connected Clients.<br>
     * ExecutorService will stop accepting new Thread inits.
     * After notifying all clients, ServerSocket will be closed and ExecutorService will try to shutdown all
     * active ClientWorker Threads.
     */
    public void shutdown() {
        SimpleChat.serverLogger.log(INFO, "Initiating shutdown ... ");
        listening = false;

        if (!serverSocket.isClosed())
            try (Socket shutdownSocket = new Socket(host, port)) {
                shutdownSocket.close();
                workerList.forEach((k, v) -> k.shutdown());
                serverSocket.close();
                executorService.shutdown();
            } catch (Exception e) {
                SimpleChat.serverLogger.log(SEVERE, e.toString());
            } finally {
                executorService.shutdownNow();
                this.interrupt();
            }
    }
}

/**
 * Thread for client socket connection.<br>
 * Every client has to be handled by an own Thread.
 */
class ClientWorker implements Runnable {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    private SimpleChatServer callback;
    private boolean listening = true;

    /**
     * Init of ClientWorker-Thread for socket intercommunication
     *
     * @param client   Socket got from ServerSocket.accept()
     * @param callback {@link simplechat.communication.socket.server.SimpleChatServer} reference
     * @throws IOException will be throwed if the init of Input- or OutputStream fails
     */
    ClientWorker(Socket client, SimpleChatServer callback) throws IOException {
        SimpleChat.serverLogger.log(INFO, "Client tries to connect ... " + client);

        this.client = client;
        client.setKeepAlive(true);

        this.in = new BufferedReader(
                new InputStreamReader(
                        client.getInputStream(),
                        StandardCharsets.UTF_8));
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.callback = callback;

        SimpleChat.serverLogger.log(INFO, "ClientWorker was initialized! clientSocket=" + client.toString()
                + " in=" + in.toString() + " out=" + out.toString());
    }

    /**
     * MessageHandler for incoming Messages on Client Socket
     * <br>
     * The InputSocket will be read synchronous through readLine()
     * Incoming messages first will be checked if they start with any Commands, which will be executed properly.
     * Otherwise text messages will be delegated to the {@link SimpleChatServer#received(String, ClientWorker)} method.
     */
    @Override
    public void run() {
        String currentMessage;
        SimpleChat.serverLogger.log(INFO, "ClientWorker was started!");
        try {
            while ((currentMessage = in.readLine()) != null && listening) {
                SimpleChat.serverLogger.log(INFO, "Received from client: " + currentMessage);

                if (currentMessage.startsWith("!")) {
                    try {
                        switch (MessageProtocol.getCommand(currentMessage.split(" ")[0])) {
                            case EXIT:
                                listening = false;
                                callback.removeClient(this);
                                break;
                            case CHATNAME:
                                String name = currentMessage.split(" ")[1];
                                callback.setName(name, this);
                                break;
                            default:
                                // throw MalformedCommandException
                        }
                    } catch (IllegalArgumentException iae) {
                        SimpleChat.serverLogger.log(SEVERE, iae.toString());
                    }
                } else callback.received(currentMessage, this);
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Stream closed")) {
            } else SimpleChat.serverLogger.log(SEVERE, e.toString());
        } finally {
            if (!client.isClosed()) shutdown();
        }
    }

    /**
     * Clean shutdown of ClientWorker
     * <br>
     * If listening was still true, we are sending a {@link MessageProtocol.Commands#EXIT} to the client.
     * Finally we are closing all open resources.
     */
    void shutdown() {
        SimpleChat.serverLogger.log(INFO, "Shutting down ClientWorker ... listening=" + listening);
        if (listening) {
            listening = false;
            send(MessageProtocol.getMessage(EXIT));
        }
        try {
            in.close();
            out.close();
            client.close();
        } catch (IOException io) {
            SimpleChat.serverLogger.log(SEVERE, io.toString());
        }
    }

    /**
     * Sending message through Socket OutputStream {@link #out}
     *
     * @param message MessageText for Client
     */
    void send(String message) {
        try {
            if (client.isConnected()) {
                SimpleChat.serverLogger.log(INFO, "Sending message to client: " + message);
                out.println(message);
                out.flush();
            }
        } catch (Exception e) {
            SimpleChat.serverLogger.log(SEVERE, e.toString());
            shutdown();
        }
    }
}
