
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

	/** Main method **/
	public static void main(String args[]) throws IOException {

		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Connect to server
		try {
			connectToServer();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host, try another ip & port");
			return;
		} catch (IOException e2) {
			e2.printStackTrace();
			return;
		}

		// Create client frame
		clientFrame = new ClientFrame();
		
		// Add action listener to send button
		clientFrame.getSendButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent i) {
				handleSendButton();
			}
		});
		
		clientFrame.addWindowListener(new WindowAdapter() {

	        @Override
	        public void windowClosing(WindowEvent arg0) {
	        	closeSocket();
	        }

	    });

	}
	
	private static void closeSocket() {
		try {
			// close connection and streams
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static void handleSendButton() {
		// Get the current messages
		String type = (String) clientFrame.getActionBox().getSelectedItem();
		String word = clientFrame.getWordField().getText();
		String meaning = clientFrame.getMeaningField().getText();
		String msg = type + "," + word + "," + meaning;

		// Check message valid
		if (word.length() == 0) { // empty
			clientFrame.getResponseArea().setText(clientFrame.getResponseArea().getText() + "ERROR: Word cannot be empty\n");
			return;
		} else if (type.equals("add") && (meaning.length() == 0)) {
			clientFrame.getResponseArea().setText(clientFrame.getResponseArea().getText() + "ERROR: Meaning cannot be empty for 'add' action\n");
			return;
		}

		// Send the request to server and put response to text area
		clientFrame.getResponseArea().setText(clientFrame.getResponseArea().getText() + sendRequest(msg) + "\n");
	}

	/** Send request to server **/
	private static String sendRequest(String msg) {
		try {
			// Send request to server
			out.write(msg + "\n");
			out.flush();

			// Receive the reply from the server by reading from the socket input stream
			String response = in.readLine(); // This method blocks until there is something to read
												// from the input stream
			
			if (response == null) {  
				throw new IOException();  // lost connection when waiting for response
			}

			return "Server: " + response;
		} catch (UnsupportedEncodingException e) {
			// should never get here
			e.printStackTrace();
		} catch (IOException e) {
			// server closed, cannot send message
			return "Lost connection to server...";
		}
		return "";
	}
	
	
	/** Connect to the server **/
	private static void connectToServer() throws UnknownHostException, IOException {
		// register a new client socket connect to server at the port
		clientSocket = new Socket(ip, port);

		// Get communication input/output streams associated with the socket
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
	}

	/** Read command line arguments **/
	private static void readArgs(String[] args) throws Exception {
		// Check number of arguments
		if (args.length != 2) {
			throw new Exception("Invalid number of arguments (NEED: IP & PORT)");
		}

		// Store the arguments to corresponding variables
		ip = args[0];
		port = Integer.parseInt(args[1]);
	}
}
