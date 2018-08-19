
/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {

	private static int port; // port number (from args)
	private static String DICT_PATH; // path to the file containing initial dictionary data (from args)
	private static int counter = 0; // number of clients connected
	private static HashMap<String, String> dictionary; // the dictionary of the server
	private static Scanner scan = new Scanner(System.in);

	public static void main(String args[]) throws IOException {

		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Read the dictionary file to init dictionary
		readDictionaryFile();

		// register a new server socket to the port
		ServerSocket serverSocket = new ServerSocket(port);

		Thread mainThread = new Thread(() -> serverMainThread(serverSocket));
		mainThread.start();

		// Check if exiting the server
		while (true) {
			String msg = null;
			while ((msg = scan.nextLine()) != null) {
				System.out.println("message: " + msg);
				if (msg.equals("exit")) {
					System.out.println("Storing the dictionary file...");
					writeDictionaryFile();
					System.out.println("Exit from the server...");
					// should stop all threads
					return;
				}
			}
		}
	}

	/**
	 * Main thread of the server to serve the clients
	 */
	private static Object serverMainThread(ServerSocket serverSocket) {
		// Wait for connections from clients
		while (true) {
			// Connect and get client socket
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
				counter++;
				System.out.println("Client " + counter + ": Applying for connection!");

				// Start a new thread to serve the client (lambda expression to run serveClient
				// method)
				Thread t = new Thread(() -> serveClient(clientSocket));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Read dictionary data from the file
	 */
	@SuppressWarnings("unchecked")
	private static void readDictionaryFile() {
		try {
			// get file input stream
			FileInputStream fileIn = new FileInputStream(DICT_PATH);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			// read dictionary object
			try {
				dictionary = (HashMap<String, String>) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			// close streams
			in.close();
			fileIn.close();
		} catch (IOException e) {
			dictionary = new HashMap<String, String>();
		}
	}

	/**
	 * Write the updated dictionary data to the file
	 */
	private static void writeDictionaryFile() {
		try {
			// get file output stream
			FileOutputStream fileOut = new FileOutputStream(DICT_PATH);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			// write dictionary object
			out.writeObject(dictionary);

			// close streams
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readArgs(String[] args) throws Exception {
		// Check number of arguments
		if (args.length != 2) {
			throw new Exception("Invalid number of arguments");
		}

		// Store the arguments to corresponding variables
		port = Integer.parseInt(args[0]);
		DICT_PATH = args[1];
	}

	/**
	 * Create a new thread to serve each connected client
	 */
	private static void serveClient(Socket clientSocket) {
		try {
			// Get communication input/output streams associated with the client socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

			try {
				// Keep reading the client's message
				String clientMsg = null;

				// Keep reading the client's message
				while ((clientMsg = in.readLine()) != null) {
					System.out.println("Message from client  + " + clientMsg);
					out.write("Server Ack " + clientMsg + "\n");
					out.flush();
					System.out.println("Response sent");
				}
			} catch (SocketException e) {
				System.out.println("closed...");
			}

			// close streams
			in.close();
			out.close();

			// close client socket only, not close server socket
			clientSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
