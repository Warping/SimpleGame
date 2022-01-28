package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerClientInstance implements Runnable {
	
	private ServerSocket server;
	private Socket client;
	//private BufferedReader fromClient;
	//private PrintWriter toClient;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private int clientID;
	
	private volatile Thread thread;
	
	public ServerClientInstance(ServerSocket server, int clientID) {
		this.server = server;
		this.clientID = clientID;
		System.out.println("Creating Instance " + clientID + "...");
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		while (!server.isClosed()) {
            try {
                processClientStream();
            } catch (SocketException e){
            	continue;
            } catch (Exception e) {
            	System.out.println("Exiting loop!");
            	e.printStackTrace();
            	break;
            }
        }
	}
	
	public void start(ServerSocket server, int clientID) {
		this.server = server;
		this.clientID = clientID;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		System.out.println("Client " + clientID + " Closing...");
		//thread.interrupt();
		try {
			//toClient.writeObject("Server: Disconnecting Clients...");
			server.close();
			client.close();
			toClient.close();
			fromClient.close();
			System.out.println("Client " + clientID + " Sockets Closed!");
		} catch (Exception e) {
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client " + clientID + " Closed!");
	}
	
	private void processClientStream() throws IOException, ClassNotFoundException {
		System.out.println("Awaiting Client Connection " + clientID + "...");
		client = server.accept();
		//fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		//toClient = new PrintWriter(client.getOutputStream(), true);
		toClient = new ObjectOutputStream(client.getOutputStream());
		fromClient = new ObjectInputStream(client.getInputStream());
		System.out.println("Client " + clientID + " Connected!");
		
		String clientInput = "";
		while (!clientInput.equalsIgnoreCase("stop")) {
			clientInput = (String) fromClient.readObject();
			System.out.println("Client " + clientID + ": " + clientInput);
			if (clientInput.equalsIgnoreCase("daddy")) {
				toClient.writeObject("YOU ARE CLIENT " + clientID);
			} else if (clientInput.equalsIgnoreCase("stop")) {
				toClient.writeObject("Ending Session With Client " + clientID);
			} else {
				toClient.writeObject("Sending Back Data On " + clientInput + " to Client " + clientID);
			}
		}
		fromClient.close();
		toClient.close();
		client.close();
		System.out.println("Client " + clientID + " has Left the Server");
	}
	
	

}
