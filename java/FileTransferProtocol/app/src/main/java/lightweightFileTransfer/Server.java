package lightweightFileTransfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
  private final static int EXIT_SUCCESS=0;
  private final static int EXIT_FAILURE=1;

  private final static boolean DEBUG=true;

  /* maxmimum length of packets to be received from UDP port */
  private final static int DATAGRAM_PACKET_LEN=256;

  private int tcpPort;
  private int udpPort;
  private String shutdownPassword;

  /* tells us if we shall still listen to new TCP connections */ 
  private boolean listening=true; 

  private ServerSocket serverSocket=null;

  /**
   * This class implements a thread listening to a specified UDP port if 
   * the shutdown password is sent. When this password is received, the
   * server will be forced to shut down gracefully.
   */
  class UDPListener extends Thread {
    int port;
    String password;
    Server server;
    boolean error=false;

    /**
     * constructor
     * @param port UDP port this thread shall listen to
     * @param password when this password is received, the server shall
     * be shut down
     * @param server instance of Server that will be notified about the
     * shutdown password being received
     */
    public UDPListener(int port, String password, Server server) {
      super("UDPListener");
      this.port=port;
      this.password=password;
      this.server=server;
    }

    /**
     * this method is for an instance of Server to be able to check if
     * any error has occurred while listening to incoming UDP data
     * @return boolean value if an error has occurred
     */
    public boolean errorOccured() {
      return error;
    }

    /**
     * run the thread ;)
     */
    public void run() {
      /* to some initialization stuff */
      DatagramSocket socket=null;

      try {
	socket=new DatagramSocket(port);
      }
      catch (SocketException e1) {
	if(DEBUG) {
	  System.err.println("Can't create UDP socket.");
	}
	error=true;
	return;
      }
      byte[] buf = new byte[DATAGRAM_PACKET_LEN];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);

      /* wait for input */
      String input="";
      while(!input.equals(password) && !error) {
	try {
	  socket.receive(packet);
	}
	catch (IOException e) {
	  if(DEBUG) {
	    System.err.println("Can't receive anything from UDP "+
		"port.");
	  }
	  error=true;
	}

	byte[] data=packet.getData();
	input="";
	for(int a=0;a<packet.getLength();a++) {
	  /* strip last character (\n) */
	  if(a<packet.getLength()-1 || data[a]!=10) {
	    input+=(char)data[a];
	  }
	}
      }

      /* tidy up */
      if(DEBUG) {
	System.err.println("Initiating graceful shutdown.");
      }

      socket.close();
      server.shutdown();
    }
  }

  /**
   * this class implements a thread that answers to a client's requests
   * over a single TCP connection 
   */
  class ServerThread extends Thread {
    Socket socket;
    boolean quit=false;

    /**
     * constructor
     * @param socket the socket this thread shall use
     */
    public ServerThread(Socket socket) {
      super("ServerThread");
      this.socket=socket;
    }

    /**
     * when receiving a GET or LS command, this method will construct the
     * pathname for the file referred; the client will be punished when
     * trying to execute a directory traversal attack
     * @param filename filename from the GET or LS command sent by the
     * client
     * @return filename if success; empty string when directory traversal
     * attack recognized 
     */
    private String getFilename(String filename) {
      if(filename.length()>1) {
	if(filename.equals("..")) {
	  return "";
	}
	if(filename.substring(0,3).equals("../")) {
	  return "";
	}
	if(filename.substring(filename.length()-3).equals("/..")) {
	  return "";
	}
	else if(filename.indexOf("/../")>-1) {
	  return "";
	}
      }
      return "./pub/"+filename;
    }

    /**
     * constructs the file list that will be sent to the client when a
     * LS command is received with a directory as argument
     * @param file instance of File referring to the directory to be
     * processed
     * @return file list in the format specified by the TGMSFTP protocol
     */
    private String getFilesList(File file) {
      if(file.canRead()) {
	String output;

	output="109 File list follows\n";
	String[] files=file.list();

	output+=files.length+"\n";
	for (String filename : files) {
	  output+=filename+"\n";
	}

	return output;
      }
      else {
	return "213 Access denied\n";
      }
    }

    /**
     * constructs an answer to a client's LS command
     * @param in instance of BufferedReader where we will read further
     * infomation from
     * @return the answer that is ought to be sent to the client
     */
    private String performLS(BufferedReader in) {
      String output;
      String filename="";

      try {
	filename=in.readLine();
      }
      catch (IOException e) {
	if(DEBUG) {
	  System.err.println("Can't read from socket.");
	}
	return "";
      }

      filename=getFilename(filename);
      File file=new File(filename);

      if(filename.equals("") || !file.exists()) {
	output="212 Not found\n";
      }
      else if(file.isDirectory()) {
	output=getFilesList(file);
      }
      else {
	output="108 Sending you file length\n"+file.length()+"\n";
      }

      return output;
    }

    /**
     * constructs the answer to a clien's GET command 
     * @param in instance of BufferedReader where we will read further
     * information from
     * @return the answer that is ought to be sent to the client
     */
    private String performGET(BufferedReader in) {
      String output;
      String filename="";

      try {
	filename=in.readLine();
      }
      catch (IOException e) {
	if(DEBUG) {
	  System.err.println("Can't read from socket.");
	}
	return "";
      }

      filename=getFilename(filename);
      File file=new File(filename);

      if(filename.equals("") || !file.exists()) {  
	output="212 Not found\n";
      }
      else if(file.isDirectory()) {
	output="214 You can't get a directory\n";
      }
      else {
	output="110 Sending you file\n";
	BufferedReader inputStream=null;
	try {
	  inputStream=new BufferedReader(new FileReader(filename));
	}
	catch (FileNotFoundException e) {
	  /**
	   * in this case, the file's there (we've checked that
	   * already), but due to its permissions, we may not read
	   * it
	   */
	  output="213 Access denied\n";
	}

	if(inputStream!=null) {
	  String input;
	  try {
	    while((input=inputStream.readLine())!=null) {
	      String temp="";
	      for(int a=0;a<input.length();a++) {
		if(input.charAt(a)==':') {
		  temp+="::";
		}
		else {
		  temp+=input.charAt(a);
		}
	      }
	      output+=temp+"\n";
	    }
	  }
	  catch (IOException e) {
	    if(DEBUG) {
	      System.err.println
		("Error closing file input stream.");
	    }
	  }

	  try {
	    inputStream.close();
	  }
	  catch (IOException e) {
	    if(DEBUG) {
	      System.err.println
		("Error closing file input stream.");
	    }
	  }

	  output+="\n:\n";
	}
      }

      return output;
    }

    /**
     * processes a client's command
     * @param input command received by client
     * @param in instance of BufferedReader where we will read further
     * information from
     * @return the answer that is ought to be sent to the client 
     */
    private String process(String input, BufferedReader in) {
      String output;

      if(input.equals("LS")) {
	output=performLS(in);
      }
      else if(input.equals("GET")) {
	output=performGET(in);
      }
      else if(input.equals("QUIT")) {
	output="107 Sayonara, take good care now\n";
	quit=true;
      }
      else {
	output="200 Command not recognised\n";
      }
      return output;
    }

    /**
     * do the work ;)
     */
    public void run() {
      /* do some initialization stuff */
      PrintWriter out=null;
      BufferedReader in=null;

      try {
	out=new PrintWriter(socket.getOutputStream(), true);
	in=new BufferedReader
	  (new InputStreamReader(socket.getInputStream()));
      }
      catch(IOException ioe) {
	if(DEBUG) {
	  System.err.println
	    ("Can't get socket's input and output stream.");
	}
      }

      if(in!=null && out!=null) {
	/* say hello */
	out.write("100 Welcome to the AMFT File Server\n");
	out.flush();

	String input,output;
	try {
	  /**
	   * wait for requests from the client and try to answer
	   * properly
	   */
	  while(!quit && (input=in.readLine())!=null) {
	    output=process(input, in);
	    out.write(output);
	    out.flush();
	  }

	}
	catch(IOException ioe) {
	  if(DEBUG) {
	    System.err.println
	      ("Error while performing socket operations.");
	  }
	}
      }

      /* tidy up */
      try {
	out.close();
	in.close();
	socket.close();
      }
      catch(IOException ioe) {
	if(DEBUG) {
	  System.err.println("Can't tidy up.");
	}
      }
    }
  }

  /**
   * constructor
   * @param tcpPort TCP port where we will listen for new inbound
   * connections
   * @param udpPort UDP port where we will listen for the shutdown
   * password
   * @param shutdownPassword password that is used to force the server
   * to a graceful shutdown
   */
  public Server(int tcpPort, int udpPort, String shutdownPassword) {
    this.tcpPort=tcpPort;
    this.udpPort=udpPort;
    this.shutdownPassword=shutdownPassword;
  }

  /**
   * do the work ;)
   * @return if an error has occurred or not
   */
  public boolean run() {
    /* initialization */
    boolean returnValue=true;

    try {
      serverSocket=new ServerSocket(tcpPort);
    }
    catch(IOException ioe) {
      if(DEBUG) {
	System.err.println("Can't listen to TCP port.");
      }
      returnValue=false;
    }

    UDPListener udpListener=null;
    /* remember all the threads we create */
    ArrayList<ServerThread> threads=new ArrayList<ServerThread>();

    if(serverSocket!=null) {
      udpListener=new UDPListener(udpPort, shutdownPassword, this);
      udpListener.start();

      if(udpListener.errorOccured()) {
	returnValue=false;
      }
      else {
	/* wait for inbound connections */
	while(listening) {
	  try {
	    ServerThread thread=new ServerThread
	      (serverSocket.accept());
	    thread.start();
	    threads.add(thread);
	  }
	  catch (IOException e) {
	    /**
	     * when listening==false, we know that the server is
	     * forced to shut down 
	     */ 
	    if(listening) {
	      if(DEBUG) {
		System.err.println
		  ("Error while listening to TCP port.");
	      }
	      returnValue=false;
	    }
	    break;
	  }
	}

	/**
	 * if listening==false, the socket has already been closed by
	 * shutdown()
	 */ 
	if(listening) {
	  try {
	    serverSocket.close();
	  }
	  catch (IOException ioe) {
	    if(DEBUG) {
	      System.err.println
		("Error while closing server socket.");
	    }
	    returnValue=false;
	  }
	}
      }
    }

    /* wait for UDP thread to terminate */
    if(udpListener!=null) {
      while(udpListener.isAlive()) {
	try {
	  udpListener.join();
	}
	catch (InterruptedException ie) {
	  /* do nothing */
	}
      }
    }

    /* wait for all TCP threads to terminate */
    for (ServerThread thread : threads) {
      while(thread.isAlive()) {
	try {
	  thread.join();
	}
	catch (InterruptedException ie) {
	  /* do nothing */
	}
      }
    }

    return returnValue;
  }

  /**
   * this method is called by the UDPListener thread when 
   *
   */
  private void shutdown() {
    listening=false;

    try {

      /**
       * close serverSocket to interrupt serverSocket.accept() in
       * run(); run() will receive an IOException, but due to
       * listening==false, it will know that this is not an error
       */
      serverSocket.close();
    }
    catch (IOException ioe) {
      if(DEBUG) {
	System.err.println("Error while closing server socket.");
      }
    }
  }

  /**
   * main method
   * @param args command line arguments
   */
  public static void main(String[] args) {
    /* check arguments */
    if(args.length!=3) {
      if(DEBUG) {
	System.err.println
	  ("Invalid number of command line arguments.");
      }
      System.exit(EXIT_FAILURE);
    }

    int tcpPort=0;
    int udpPort=0;
    String shutdownPassword=args[2];

    if(shutdownPassword.length()>DATAGRAM_PACKET_LEN) {
      if(DEBUG) {
	System.err.println("Shutdown password too long.");
      }
      System.exit(EXIT_FAILURE);
    }

    try {
      tcpPort=Integer.valueOf(args[0]);
    }
    catch(NumberFormatException nfe) {
      if(DEBUG) {
	System.err.println("Listening port is not a valid TCP port.");
      }
      System.exit(EXIT_FAILURE);
    }

    try {
      udpPort=Integer.valueOf(args[1]);
    }
    catch(NumberFormatException nfe) {
      if(DEBUG) {
	System.err.println("Shutdown port is not a valid UDP port.");
      }
      System.exit(EXIT_FAILURE);
    }

    /* let's do the work */
    Server server=new Server(tcpPort, udpPort, shutdownPassword);

    System.exit(server.run() ? EXIT_SUCCESS : EXIT_FAILURE);
  }
}
