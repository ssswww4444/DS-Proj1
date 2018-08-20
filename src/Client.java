
/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.net.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	
	// Socket
	private static Socket clientSocket;
	
	// Streams
	private static BufferedReader in;
	private static BufferedWriter out;
	
	// For GUI
	private static GraphicsConfiguration gc;

	public static void main(String args[]) throws IOException {

		// Read command line arguments
		try {
			readArgs(args);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// register a new client socket connect to server at the port
		clientSocket = new Socket(ip, port);
		
		// Get communication input/output streams associated with the socket
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
		out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
		
		// Enable GUI for the client
		setGUI();

//		// close connection and streams
//		in.close();
//		out.close();
//		clientSocket.close();
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
		frame.setLayout(new GridLayout(2, 1));
		frame.setLocation(500, 300);

		// Main panel of the window
		JPanel mainPanel = new JPanel(new GridBagLayout());
		frame.add(mainPanel);
		setMainPanel(mainPanel);
		
		// resize the frame
		frame.pack();
		frame.setResizable(false);

		// Enable the GUI
		frame.setVisible(true);

	}

	private static void setMainPanel(JPanel mainPanel) {
		// Constraints
		GridBagConstraints c = new GridBagConstraints();

		// Create the drop down list to select the actions
		String[] actions = new String[] { "add", "search", "remove" };
		JComboBox<String> actionBox = new JComboBox<String>(actions);
		actionBox.setSize(30, 10);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(actionBox, c);

		// Create the word label
		JLabel wordLabel = new JLabel("Word: ");
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(wordLabel, c);

		// Create the text field for word
		JTextField wordField = new JTextField("", 10);
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(wordField, c);

		// Create the meaning label
		JLabel meaningLabel = new JLabel("Meaning: ");
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(meaningLabel, c);

		// Create the text field for meaning
		JTextField meaningField = new JTextField("", 15);
		c = new GridBagConstraints();
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 2;
		mainPanel.add(meaningField, c);
		
		// Create the response label
		JLabel responseLabel = new JLabel("Response: ");
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add(responseLabel, c);
		
		// Create the response text field
		JTextField responseField = new JTextField("",20);
		responseField.setEditable(false);
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 3;
//		c.gridheight = 3;
		mainPanel.add(responseField, c);

		// Create the button to send the request
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent i) {
				// Get the current messages
				String type = (String) actionBox.getSelectedItem();
				String word = wordField.getText();
				String meaning = meaningField.getText();
				String msg = type + "," + word + "," + meaning;
				
				// Send the request to server
				sendRequest(msg);
				
				// Get response from server and put to text field
				responseField.setText(getResponse());
			}
		});
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(sendButton, c);
	}

	private static void sendRequest(String msg) {
		try {			
			// Send request to server
			out.write(msg);
			out.flush();
			System.out.println("message sent");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// server closed, cannot send message
			System.out.println("Lost connection to server...");
		}
	}
	
	private static String getResponse() {
		// Receive the reply from the server by reading from the socket input stream
		String response = null;
		try {
			response = in.readLine();  // This method blocks until there is something to read from the input stream
		} catch (IOException e) {
			// server closed, cannot send message
			System.out.println("Lost connection to server...");
		}
		return response;
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
}
