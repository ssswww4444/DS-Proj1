/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {

	// Graphic configuration
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static GraphicsDevice gs = ge.getDefaultScreenDevice();
	private static GraphicsConfiguration gc = gs.getDefaultConfiguration();

	/** Constructor **/
	ClientFrame() {
		super(gc);

		setFrame();
	}

	/** Setting for frame and panels to frame **/
	private void setFrame() {

		this.setTitle("Client GUI");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set layout
		this.setLayout(new GridBagLayout());

		// Constraints
		GridBagConstraints c = new GridBagConstraints();

		// Main panel of the window
		c.gridx = 0;
		c.gridy = 0;
		this.add(mainPanel);
		setMainPanel();

		// Response panel of the window
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		this.add(responsePanel);
		setResponsePanel();

		// resize the frame
		this.pack();
		this.setResizable(false);

		// set frame location
		this.setLocation(gc.getBounds().width / 2 - this.getWidth() / 2,
				gc.getBounds().height / 2 - this.getHeight() / 2); // at center

		// Enable the GUI
		this.setVisible(true);
	}

	/** Add UI Components to main panel **/
	private void setMainPanel() {
		// Constraints
		GridBagConstraints c = new GridBagConstraints();

		// Add the action label
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(actionLabel, c);

		// Add the word label
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(wordLabel, c);

		// Add the text field for word
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(wordField, c);

		// Add the meaning label
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(meaningLabel, c);

		// Add the text field for meaning
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 2;
		mainPanel.add(meaningField, c);

		// Add the drop down list to select the actions
		c = new GridBagConstraints();
		actionBox.setSize(30, 10);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(actionBox, c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		mainPanel.add(sendButton, c);
	}

	/** Add UI Components to response panel **/
	private void setResponsePanel() {
		// Constraints
		GridBagConstraints c = new GridBagConstraints();

		// Create the response label
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 0;
		responsePanel.add(responseLabel, c);

		// Create the response text field
		JScrollPane scrollPane = new JScrollPane(responseArea);
		responseArea.setColumns(35);
		responseArea.setRows(25);
		responseArea.setEditable(false);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		responsePanel.add(scrollPane, c);
	}
	
	/**
	 * Show message in GUI
	 */
	public void showMessage(String msg) {
		responseArea.setText(responseArea.getText() + msg);
	}
	
	/** Getters **/
	public JButton getSendButton() {
		return sendButton;
	}
	public JComboBox<String> getActionBox() {
		return actionBox;
	}
	public JTextField getWordField() {
		return wordField;
	}
	public JTextField getMeaningField() {
		return meaningField;
	}
	public JTextArea getResponseArea() {
		return responseArea;
	}

	// Panels
	private JPanel mainPanel = new JPanel(new GridBagLayout());
	private JPanel responsePanel = new JPanel(new GridBagLayout());

	// UI Components
	private JTextField meaningField = new JTextField("", 20);
	private JLabel responseLabel = new JLabel("Response: ");
	private JLabel actionLabel = new JLabel("Action: ");
	private JLabel wordLabel = new JLabel("Word: ");
	private JTextField wordField = new JTextField("", 10);
	private JLabel meaningLabel = new JLabel("Meaning: ");
	private JTextArea responseArea = new JTextArea("");
	private JButton sendButton = new JButton("Send");
	private JComboBox<String> actionBox = new JComboBox<String>(new String[] { "add", "search", "remove" });

}
