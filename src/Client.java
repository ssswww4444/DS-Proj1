import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
	
	// IP and port
	private static String ip = "localhost";
	private static int port = 3000;
	
	public static void main(String args[]) throws IOException {
		
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
			
			// Send the input string to the server by writing to the socket output stream
			out.write(inputStr + "\n");
			out.flush();
			System.out.println("Message sent");
			
			// Receive the reply from the server by reading from the socket input stream
			String received = in.readLine(); // This method blocks until there
												// is something to read from the
												// input stream
			System.out.println("Message received: " + received);
		}
		
		scanner.close();
		
		// close streams
		in.close();
		out.close();
		
		// close connection
		clientSocket.close();
	}
		
	
}
