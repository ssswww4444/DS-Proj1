
/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.net.*;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Client {

	// IP and port
	private static String ip;
	private static int port;

	private static GraphicsConfiguration gc;

	public static void main(String args[]) throws IOException {

		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Enable GUI for the client
		setGUI();

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

	/**
	 * Initialize the GUI for clients to add/remove/search words
	 */
	private static void setGUI() {
		// Create new frame
		JFrame frame = new JFrame(gc);

		// Set close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Size the frame
		frame.setSize(300, 300);
		frame.setLayout(new GridLayout(3, 1));
		frame.setLocation(500, 300);

		JPanel controlPanel = new JPanel(new GridBagLayout());
		JPanel textPanel = new JPanel(new FlowLayout());
		JPanel resultPanel = new JPanel(new FlowLayout());

		frame.add(controlPanel);
		frame.add(resultPanel);

		setControlPanel(controlPanel);
		setResultPanel(resultPanel);

		// Enable the GUI
		frame.setVisible(true);

	}

	private static void setResultPanel(JPanel resultPanel) {
		JLabel resultLabel = new JLabel("Result: ");
		resultPanel.add(resultLabel);
	}

	private static void setControlPanel(JPanel controlPanel) {
		// Constraints
		GridBagConstraints c = new GridBagConstraints();

		// Create the drop down list to select the actions
		String[] actions = new String[] { "add", "search", "remove" };
		JComboBox<String> actionBox = new JComboBox<String>(actions);
		actionBox.setSize(30, 10);
		c.gridx = 0;
		c.gridy = 0;
		controlPanel.add(actionBox, c);

		// Create the word label
		JLabel wordLabel = new JLabel("Word: ");
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 1;
		controlPanel.add(wordLabel, c);

		// Create the text field for word

		JTextField wordField = new JTextField("", 10);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		controlPanel.add(wordField, c);

		// Create the meaning label
		JLabel meaningLabel = new JLabel("Meaning: ");
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		controlPanel.add(meaningLabel, c);

		// Create the text field for meaning
		c.anchor = GridBagConstraints.LINE_START;
		JTextField meaningField = new JTextField("", 10);
		c.gridx = 1;
		c.gridy = 2;
		controlPanel.add(meaningField, c);

		// Create the button to send the request
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// send request
				String word = wordField.getText();
				System.out.println("Current Word: " + word);
				String meaning = meaningField.getText();
			}
		});
		c.gridx = 2;
		c.gridy = 0;
		controlPanel.add(sendButton, c);

	}

	private static void sendRequest() {
//		// get the selected item:
//		String selectedBook = (String) actionBox.getSelectedItem();
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
