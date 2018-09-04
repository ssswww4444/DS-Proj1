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
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame {
	// Graphic configuration
	private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static GraphicsDevice gs = ge.getDefaultScreenDevice();
	private static GraphicsConfiguration gc = gs.getDefaultConfiguration();

	/** Constructor **/
	ServerFrame() {
		super(gc);
		setFrame();
	}

	private void setFrame() {
		this.setTitle("Server GUI");
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

		// Add the client label
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(clientLabel, c);

		// Add the clientNum label
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(clientNumLabel, c);

		// Add the table
		JScrollPane tableSP = new JScrollPane(wordTable);
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.gridy = 1;
		mainPanel.add(tableSP, c);
	}

	public void updateTable(HashMap<String, String> dictionary) {
		DefaultTableModel model = new DefaultTableModel(
				new String[] { "Word", "Meaning" }, 0);
		
		for (String word: dictionary.keySet()) {
			String meaning = dictionary.get(word);
			model.addRow(new Object[] {word, meaning});
		}
		wordTable.setModel(model);
	}

	public void updateClientNum(int clientNum) {
		clientNumLabel.setText(Integer.toString(clientNum));
	}

	// Panels
	private JPanel mainPanel = new JPanel(new GridBagLayout());

	// UI Components
	private JTable wordTable = new JTable();
	private JLabel clientLabel = new JLabel("Number of clients connected: ");
	private JLabel clientNumLabel = new JLabel("0");

}
