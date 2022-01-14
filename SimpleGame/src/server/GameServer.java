package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class GameServer {
	
	private ServerSocket serverSocket;
	private ArrayList<Socket> activeClients = new ArrayList<>();
	private ArrayList<Thread> activeThreads = new ArrayList<>();
	
	public GameServer(int SLOTS, int PORT) {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e1) {}
		
		for (int i = 0; i < SLOTS; i++) {
			int socketID = i + 1;
			Thread currentThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int clientID = socketID;
					while (true) {
						try {
							newSocket(clientID);
						} catch (IOException e) {
							break;
						}	
					}
					System.out.println("Socket " + clientID + " Closed!");
				}	
			});
			currentThread.start();
			activeThreads.add(currentThread);
		}
	}
	
	public void kill() {
		try {
			serverSocket.close();
			System.out.println("Server Closing...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("CANT KILL SERVER!");
		}
		
		for (Socket client : activeClients) {
			try {
				client.close();
				System.out.println("Client Sockets Closing...");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("CANT KILL CLIENT!");
			}
		}
		
		for (Thread thread : activeThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("CANT KILL THREAD!");
			}
		}
		System.out.println("Server Closed!");
	}
	
	private void newSocket(int clientID) throws IOException {
		System.out.println("Waiting for Client in Slot " + clientID + "...");
		Socket client = serverSocket.accept();
		activeClients.add(client);
		System.out.println("New Client Accepted: Client " + clientID);
		processInput(client, clientID);
		client.close();
		activeClients.remove(client);
		
	}
	
	private void processInput(Socket client, int clientID) throws IOException {
		BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
		
		String clientInput = "";
		while (!clientInput.equalsIgnoreCase("stop") && !serverSocket.isClosed()) {
			try {
				clientInput = fromClient.readLine();
			} catch (SocketException e) {
				System.out.println("Client " + clientID + " has Disconnected");
				return;
			}
			System.out.println("Client " + clientID + ": " + clientInput);
			if (clientInput.equalsIgnoreCase("who")) {
				toClient.println("YOU ARE CLIENT " + clientID);
			} else if (clientInput.equalsIgnoreCase("stop")) {
				toClient.println("Ending Session With Client " + clientID);
			} else {
				toClient.println("Sending Back Data On " + clientInput + " to Client " + clientID);
			}
		}
		
		fromClient.close();
		toClient.close();
		System.out.println("Client " + clientID + " has Left the Server");
	}
	
	

}
