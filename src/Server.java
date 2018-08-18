import java.net.*;
import java.io.*;

public class Server {
	
	// Declare the port number
	private static int port = 3000;
		
	// Number of clients connected
	private static int counter = 0;
	
	public static void main(String args[]) throws IOException {
		// register a new server socket to the port
		ServerSocket serverSocket = new ServerSocket(port);
		
		// Wait for connections from clients
		while(true){
			// Connect and get client socket
			Socket clientSocket = serverSocket.accept();
			counter++;
			System.out.println("Client " + counter + ": Applying for connection!");
						
			// Start a new thread to serve the client (lambda expression to run serveClient method)
			Thread t = new Thread(() -> serveClient(clientSocket));
			t.start();
		}
	}

	private static void serveClient(Socket clientSocket) {
		try {
			// Get communication input/output streams associated with the client socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			
			try {
				// Keep reading the client's message
				String clientMsg = null;
				
				// Keep reading the client's message
				while((clientMsg = in.readLine()) != null) {
					System.out.println("Message from client  + " + clientMsg);
					out.write("Server Ack " + clientMsg + "\n");
					out.flush();
					System.out.println("Response sent");
				}}
				catch(SocketException e) {
					System.out.println("closed...");
			}
			
			// close streams
			in.close();
			out.close();
			
			// close client socket only, not close server socket
			clientSocket.close();
			
		} catch (IOException e) {
			// do nothing
		}
	}
		
	
}
