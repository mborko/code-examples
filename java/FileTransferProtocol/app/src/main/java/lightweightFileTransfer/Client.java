package lightweightFileTransfer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private final static int EXIT_SUCCESS=0;
	private final static int EXIT_FAILURE=1;
	
	private final static boolean DEBUG=true;
	
	private BufferedReader stdIn=null;
	
	private String serverHost;
	private int serverPort;
	
	/**
	 * constructor 
	 * @param serverHost
	 * @param serverPort
	 */
	public Client(String serverHost, int serverPort) {
		this.serverHost=serverHost;
		this.serverPort=serverPort;
	}
	
	/**
	 * reads a single line from standard input 
	 * @return the line that has been read
	 * @throws IOException if any I/O error occurs
	 */
	private String readLine() throws IOException {
		if(stdIn==null) {
			stdIn=new BufferedReader
				(new InputStreamReader(System.in));
		}
		return stdIn.readLine();
	}

	/**
	 * extracts the status code from the status text received from the
	 * server
	 * @param text status text from server
	 * @return status code
	 * @throws IllegalArgumentException if there's no space in the status
	 * text or the status code can't be converted to an int
	 */
	private int getStatusCode(String text) throws
	       		IllegalArgumentException {
		if(text.indexOf(' ')==-1) {
			if(DEBUG) {
				System.err.println("Missing space in status "+
						"text received by server.");
			}
			throw new IllegalArgumentException();
		}
		String numberString=text.substring(0, text.indexOf(' '));
		
		try {
			return Integer.valueOf(numberString);
		}
		catch(NumberFormatException ie) {
			System.err.println("Status code received by server is "+
					"not an integer.");
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * converts the user input to a request that can be sent to the server 
	 * @param input user input
	 * @return request for the server
	 */
	private String processRequest(String input) {
		String commandName;
		String argument=null;
		
		/* split into command name and it's argument */
		if(input.indexOf(' ')==-1) {
			commandName=input;
		}
		else {
			commandName=input.substring(0,input.indexOf(' '));
			argument=input.substring(input.indexOf(' ')+1);
		}
		
		/* which command do we have? */
		if(commandName.equals("ls")) {
			argument=(argument==null)?"/":argument;
			return "LS\n"+argument+"\n";
		}
		if(commandName.equals("quit")) {
			return "QUIT\n";
		}
		if(commandName.equals("get")) {
			argument=(argument==null)?"/":argument;
			return "GET\n"+argument+"\n";
		}
		
		/***
		 * we can be sure the server doesn't know about this
		 * command ;)
		 */
		return "ugga\n";
	}
	
	/**
	 * processes an answer from the server
	 * @param in BufferedReader where to read from
	 * @return true if server wants to terminate connection
	 * @throws IOException if any I/O error occurs 
	 * @throws NumberFormatException if the number of files reported from
	 * the server in case of applying LS on a directory is not an integer
	 * @throws IllegalArgumentException if an unknown status code is
	 * received from the server
	 */
	private boolean processServerAnswer(BufferedReader in) throws
			IOException, NumberFormatException,
			IllegalArgumentException {
		String answer, input;
		
		answer=in.readLine();
		
		/* check status code */
		switch(getStatusCode(answer)) {
			case 100: /* welcome message */
				return true;
				
			case 107: /* server says good-bye */ 
				System.out.println();
				return true;
			
			case 108: /* will receive file length */
				System.out.println(in.readLine());
				return false;
				
			case 109: /* will receive file list */
				input=in.readLine();
				int fileCount=0;
				
				fileCount=Integer.valueOf(input);
				for(int a=0;a<fileCount;a++) {
					System.out.println(in.readLine());
				}
				
				return false;
				
			case 110: /* will receive file */
				boolean newLine=false;
				FileWriter fw=new FileWriter("download.info");
				
				while(!(input=in.readLine()).equals(":")) {
					if(newLine) {
						fw.write("\n");
						fw.flush();
					}
					fw.write(input.replaceAll("::",":"));
					fw.flush();
					newLine=true;
				}
				fw.close();
				
				return false;
			
			case 200: /* unknown command */
			case 212: /* not found */
			case 213: /* access denied */
			case 214: /* can't get directory */
				System.out.println(answer);
				return false;
				
			default:
				if(DEBUG) {
					System.err.println
						("Unknown status code "+
						 "received from server.");
				}
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * does the main work
	 * @return EXIT_SUCCESS or EXIT_FAILURE, depending on if an error occurs
	 * or not
	 */
	public int run() {
		Socket socket;
		PrintWriter out;
		BufferedReader in;
		
		int returnValue=EXIT_SUCCESS;
		
		/* initialization */
		try {
			socket=new Socket(serverHost, serverPort);
			out=new PrintWriter(socket.getOutputStream(),true);
	        in=new BufferedReader
	        	(new InputStreamReader(socket.getInputStream()));
		}
		catch (UnknownHostException e) {
			if(DEBUG) {
				System.err.println
					("Unknown host "+serverHost+".");
			}
			return EXIT_FAILURE;
		}
		catch (IOException e) {
			if(DEBUG) {
				System.err.println("I/O error while trying "+
						"to do initialization stuff.");
			}
			return EXIT_FAILURE;
		}
		
		/* initialization successfully completed */
		System.out.println("Connection established.");
		
		/* do the work */
		try {
			processServerAnswer(in);

		        while(true) {
		    		System.out.print(">: ");
				String input=readLine();

				out.print(processRequest(input));
		        	out.flush();
	    		
				if(processServerAnswer(in)) {
					break;
				}
	        	}
		}
		catch (Exception e) {
			System.err.println("Communication error occurred.");
			returnValue=EXIT_FAILURE;
		}
        
		/* tidy up */
		try {
			in.close();
			out.close();
			socket.close();
		}
		catch (IOException e) {
			if(DEBUG) {
				System.err.println
					("I/O error while tidying up.");
			}
			return EXIT_FAILURE;
		}
		
		return returnValue;
	}
	
	/**
	 * main method - process arguments and call run()
	 * @param args
	 */
	public static void main(String[] args) {
		String host;
		int port=0;
		
		/* process command line arguments */
		if(args.length!=2) {
			if(DEBUG) {
				System.err.println("Invalid number of "+
						"command line arguments.");
			}
			System.exit(EXIT_FAILURE);
		}
		
		host=args[0];
		try {
			port=Integer.valueOf(args[1]);
		}
		catch(NumberFormatException ie) {
			if(DEBUG) {
				System.err.println
					("Port number is not an integer.");
			}
			System.exit(EXIT_FAILURE);
		}
		
		/* call run() to do the work */
		Client client=new Client(host, port);
		System.exit(client.run());
	}
}
