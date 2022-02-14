package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {
	
	Scanner sc;
	
	public GameClient(String ADDRESS, int PORT, Scanner _sc) {
			sc = _sc;
			try {
				connect(ADDRESS,PORT);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				System.out.println("Cannot Connect: Unknown Host!");
			} catch (ClassNotFoundException e) {
				System.out.println("Received invalid data from server!");
			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				System.out.println("Cannot Connect: Refused");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	}
	
	private void connect(String address, int port) throws UnknownHostException, IOException, ClassNotFoundException {
		System.out.println("Connecting...");
		Socket client = new Socket(address, port);
		ObjectInputStream fromServer = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream toServer = new ObjectOutputStream(client.getOutputStream());
		System.out.println(client.toString());
		System.out.println("Connected!");
		//PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
		//BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		String clientInput = "";
		String serverResponse = "";
		while (!clientInput.equalsIgnoreCase("stop")) {
			clientInput = sc.nextLine();
			toServer.writeObject(clientInput);
			try {
				serverResponse = fromServer.readObject().toString();
				System.out.println("Server: " + serverResponse);
			} catch (SocketException e) {
				System.out.println("Disconnected - Server Closed");
				break;
			}
		}
		
		System.out.println("Connection Closing...");
		toServer.close();
		fromServer.close();
		client.close();
		System.out.println("Connection Closed!");
	}

}
