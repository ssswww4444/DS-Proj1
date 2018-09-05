
/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Client {

	// IP and port
	private static String ip;
	private static int port;

	// Socket
	private static Socket clientSocket;

	// Streams
	private static BufferedReader in;
	private static BufferedWriter out;

	// Frame
	private static ClientFrame clientFrame;

	/**
	 * Main method
	 */
	public static void main(String args[]) throws IOException {
		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}

		// Connect to server
		try {
			// register a new client socket connect to server at the port
			clientSocket = new Socket(ip, port);
		} catch (UnknownHostException | NoRouteToHostException e) {
			System.out.println("Unknown host (try other ip or port)");
			return;
		} catch (ConnectException e2) {
			System.out.println("Connection refused (no server at the port)");
			return;
		}

		// Get communication I/O streams associated with the socket
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		} catch (IOException e2) {
			System.out.println("Failed to get I/O streams");
			return;
		}

		// Setup the client GUI
		setupGUI();

	}

	/**
	 * Setup the client GUI
	 */
	private static void setupGUI() {
		// Create client frame
		clientFrame = new ClientFrame();

		// Add action listener to send button
		clientFrame.getSendButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent i) {
				handleSendButton();
			}
		});

		// Add listener to GUI window
		clientFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				closeSocket();
			}

		});
	}

	/**
	 * Close the client socket when lost connection to server
	 */
	private static void closeSocket() {
		try {
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void handleSendButton() {
		// disable button until get response
		clientFrame.getSendButton().setEnabled(false);

		// Get the current messages
		String type = (String) clientFrame.getActionBox().getSelectedItem();
		String word = clientFrame.getWordField().getText();
		String meaning = clientFrame.getMeaningField().getText();
		String msg = type + "," + word + "," + meaning;

		// Check message valid
		if (word.length() == 0) { // empty
			clientFrame.showMessage("ERROR: Word cannot be empty\n");
			return;
		} else if (type.equals("add") && (meaning.length() == 0)) {
			clientFrame.showMessage("ERROR: Meaning cannot be empty for 'add' action\n");
			return;
		}

		// Send the request to server and put response to text area
		clientFrame.showMessage(sendRequest(msg) + "\n");

		// enable button after get response
		clientFrame.getSendButton().setEnabled(true);
	}

	/** 
	 * Send request to server and get response
	 */
	private static String sendRequest(String msg) {
		try {
			// Send request to server
			out.write(msg + "\n");
			out.flush();

			// Receive the response from the server by reading from the socket input stream
			String response = in.readLine(); // This method blocks until there is something to read
												// from the input stream
			return "Server: " + response;
		} catch (IOException e) {
			// server closed, cannot send message
			return "Lost connection to server...";
		}
	}

	/** 
	 * Read command line arguments
	 */
	private static void readArgs(String[] args) throws Exception {
		// Check number of arguments
		if (args.length != 2) {
			throw new Exception("Invalid number of arguments (NEED: IP & PORT)");
		}

		try {
			// Store the arguments to corresponding variables
			ip = args[0];
			port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			throw new Exception("Invalid argument types (NEED: IP & PORT)");
		}
	}
}
