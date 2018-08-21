
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
//	private static boolean closeServer = false;

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

		// Thread to serve clients
		Thread mainThread = new Thread(() -> serverMainThread(serverSocket));
		mainThread.start();

		// Check if exiting the server
		while (true) {
			String msg = null;
			while ((msg = scan.nextLine()) != null) {
				System.out.println("cmd message: " + msg);
				if (msg.equals("exit")) {
					writeDictionaryFile();
					System.out.println("Exit from the server...");
					// close scanner
					scan.close();
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

				// Start a new thread to serve the client
				// (lambda expression to implement runnable interface with a serveClient method)
				Thread t = new Thread(() -> serveClient(clientSocket)); // (lambda parameter -> lambda body)
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

				String clientMsg = null;

				System.out.println("reached here");

				// Keep reading the client's message
				while(!in.ready());
				System.out.println("ready!");
				while ((clientMsg = in.readLine()) != null) {   // ******** Stuck at here ***********
					System.out.println("Message from client " + clientMsg);
					out.write(accessDictionary(clientMsg) + "\n"); // get response to the client
					out.flush();
					System.out.println("Response sent");
				}

			} catch (SocketException e) {
				// client socket closed
			} finally {
				System.out.println("client socket closed...");
				// close streams and client socket
				in.close();
				out.close();
				clientSocket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Synchronized (exclusive) access to the dictionary (HashMap) for the threads
	 */
	private static String accessDictionary(String clientMsg) {
		String[] msg = clientMsg.split(",");
		String word = msg[1];

		// Three actions of the client
		switch (msg[0]) {
		case "add":
			String meaning = msg[2];
			if (dictionary.containsKey(word)) {
				return "Error: The word already exists in the dictionary";
			}
			dictionary.put(word, meaning);
			return "Added word '" + word + "' successfully";
		case "remove":
			if (!dictionary.containsKey(word)) {
				return "Error: The word '"+ word + "' does not exist in the dictionary";
			}
			dictionary.remove(word);
			return "Removed word '" + word + "' successfully";
		case "search":
			if (!dictionary.containsKey(word)) {
				return "Error: The word '"+ word + "' does not exist in the dictionary";
			}
			return "Meaning: " + dictionary.get(word); // return meaning
		}
		return "";
	}
}
