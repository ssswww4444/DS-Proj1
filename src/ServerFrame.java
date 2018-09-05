/* 
 * COMP90105 Distributed Systems - Assignment 1
 * Name: Pei-Yun Sun
 * Student ID: 667816
 */

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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

	/**
	 * Constructor
	 */
	ServerFrame() {
		super(gc);
		setFrame();
	}
	
	/** 
	 * Setting for frame and panels to frame
	 */
	private void setFrame() {
		this.setTitle("Server GUI");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Main panel of the window
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(mainPanel);
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
	
	/** 
	 * Add UI Components to main panel
	 */
	private void setMainPanel() {
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// Add the table
		JScrollPane tableSP = new JScrollPane(wordTable);
		contentPanel.add(tableSP, BorderLayout.CENTER);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.add(clientLabel);
		northPanel.add(clientNumLabel);
	}

	public void updateTable(HashMap<String, String> dictionary) {
		DefaultTableModel model = new DefaultTableModel(
				new String[] { "Word", "Meaning" }, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		for (String word: dictionary.keySet()) {
			String meaning = dictionary.get(word);
			model.addRow(new Object[] {word, meaning});
		}
		wordTable.setModel(model);
		this.pack();
	}

	public void updateClientNum(int clientNum) {
		clientNumLabel.setText(Integer.toString(clientNum));
	}

	// Panels
	private JPanel mainPanel = new JPanel();

	// UI Components
	private JTable wordTable = new JTable();
	private JLabel clientLabel = new JLabel("Number of clients connected: ");
	private JLabel clientNumLabel = new JLabel("0");
	private final JPanel contentPanel = new JPanel();
	private final JPanel northPanel = new JPanel();

}
