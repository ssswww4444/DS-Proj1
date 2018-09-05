
/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.net.*;
import java.util.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Server {

	// variables
	private static int port;
	private static String DICT_PATH;
	private static HashMap<String, String> dictionary;
	private static Scanner scan = new Scanner(System.in);
	private static ServerFrame frame;
	private static int clientNum;

	// flag to ask child threads to exit
	private static boolean exitServer;

	/**
	 * Main method
	 */
	public static void main(String args[]) throws IOException {
		clientNum = 0;
		exitServer = false;

		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}

		// Read the dictionary file to init dictionary
		readDictionaryFile();

		// register a new server socket to the port
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
		} catch (BindException e) {
			System.out.println("The port is already in use, try another port.");
			return;
		} catch (IllegalArgumentException e2) {
			System.out.println("The port num is out of range, try another port num.");
			return;
		}

		setupGUI();

		// Thread to serve clients
		Thread mainThread = new Thread(() -> serverMainThread(serverSocket));
		mainThread.start();
	}

	/**
	 * Setup Server GUI
	 */
	private static void setupGUI() {
		// Initialise GUI
		frame = new ServerFrame();

		frame.updateTable(dictionary);

		// Add window listener to GUI
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exitServer();
			}

		});
	}

	/**
	 * Exit the server if GUI closed
	 */
	public static void exitServer() {
		// Save current dictionary
		writeDictionaryFile();

		// close scanner
		scan.close();
		exitServer = true; // inform the serve threads to stop
	}

	/**
	 * Main thread of the server to serve the clients
	 */
	private static void serverMainThread(ServerSocket serverSocket) {
		// Keep waiting for connections from clients until server exit
		while (!exitServer) {
			// Connect and get client socket
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
				clientNum++;
				frame.updateClientNum(clientNum);

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
		// Get the file
		File f = new File(DICT_PATH);
		if (!f.exists()) {
			// no prior info of dict
			dictionary = new HashMap<String, String>();
			return;
		}

		try {
			// get file input stream
			FileInputStream fileIn = new FileInputStream(DICT_PATH);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			// read dictionary object as HashMap
			dictionary = (HashMap<String, String>) in.readObject();

			// close streams
			in.close();
			fileIn.close();
			
		} catch (IOException e) {
			// I/O error occurred
			dictionary = new HashMap<String, String>();
			return;
		} catch (ClassNotFoundException e2) {
			// Should never get here
			System.out.println(e2.getMessage());
		}
	}

	/**
	 * Write the updated dictionary data to the file
	 */
	private static void writeDictionaryFile() {
		try {
			// create a new file if not exist
			File f = new File(DICT_PATH);
			if (!f.exists()) {
				f.createNewFile();
			}
			
			// get file output stream
			FileOutputStream fileOut = new FileOutputStream(DICT_PATH);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			// write dictionary object
			out.writeObject(dictionary);

			// close streams
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// Should never get here
			System.out.println(e.getMessage());
		}
	}

	private static void readArgs(String[] args) throws Exception {
		// Check number of arguments
		if (args.length != 2) {
			throw new Exception("Invalid number of arguments (NEED: PORT & DICT_PATH)");
		}

		try {
			// Store the arguments to corresponding variables
			port = Integer.parseInt(args[0]);
			DICT_PATH = args[1];
		} catch (Exception e) {
			throw new Exception("ERROR: Invalid argument types (NEED: PORT & DICT_PATH");
		}
	}

	/**
	 * Create a new thread to serve each connected client
	 */
	private static void serveClient(Socket clientSocket) {
		try {
			// Get communication input/output streams associated with the client socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

			String clientMsg = null;

			// Keep reading the client's message
			while ((clientMsg = in.readLine()) != null) {
				out.write(accessDictionary(clientMsg) + "\n"); // send response to the client
				out.flush();
			}

			System.out.println("client socket closed...");

			// close streams and client socket
			in.close();
			out.close();
			clientSocket.close();
			clientNum--;
			frame.updateClientNum(clientNum);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Synchronized (exclusive) access to the dictionary (HashMap) for the threads
	 */
	private static synchronized String accessDictionary(String clientMsg) {
		String[] msg = clientMsg.split(",");
		String word = msg[1];

		// Three actions of the client
		switch (msg[0]) {
		case "add":
			String meaning = msg[2];
			if (dictionary.containsKey(word)) {
				return "ERROR: The word already exists in the dictionary";
			}
			dictionary.put(word, meaning);
			frame.updateTable(dictionary);
			return "Added word '" + word + "' successfully";
		case "remove":
			if (!dictionary.containsKey(word)) {
				return "ERROR: Cannot find the '" + word + "' in the dictionary";
			}
			dictionary.remove(word);
			frame.updateTable(dictionary);
			return "Removed word '" + word + "' successfully";
		case "search":
			if (!dictionary.containsKey(word)) {
				return "ERROR: Cannot find the '" + word + "' in the dictionary";
			}
			return "Meaning: " + dictionary.get(word); // return meaning
		}
		return "";
	}
}
