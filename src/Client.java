/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {

	// IP and port
	private static String ip;
	private static int port;

	public static void main(String args[]) throws IOException {

		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// register a new client socket connect to server at the port
		Socket clientSocket = new Socket(ip, port);

		// Get communication input/output streams associated with the socket
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

		// Client message from command line
		Scanner scanner = new Scanner(System.in);
		String inputStr = null;

		// While the user input differs from "exit"
		while (!(inputStr = scanner.nextLine()).equals("exit")) {
			
			String[] msg = inputStr.split(",");
			
			
		}

		scanner.close();

		// close streams
		in.close();
		out.close();

		// close connection
		clientSocket.close();
	}

	private static void readArgs(String[] args) throws Exception {
		// Check number of arguments
		if (args.length != 2) {
			throw new Exception("Invalid number of arguments");
		}

		// Store the arguments to corresponding variables
		ip = args[0];
		port = Integer.parseInt(args[1]);
	}
	
	private static void addWord(Socket clientSocket, String[] msg) {
		
	}
	
	private static void searchWord(Socket clientSocket, String[] msg) {
		
	}
	
	private static void removeWord(Socket clientSocket, String[] msg) {
		
	}
	
	
	
	
	// Send the input string to the server by writing to the socket output stream
//				out.write(inputStr + "\n");
//				out.flush();
//				System.out.println("Message sent");
//
//				// Receive the reply from the server by reading from the socket input stream
//				String received = in.readLine(); // This method blocks until there
//													// is something to read from the
//													// input stream
//				System.out.println("Message received: " + received);

}
