package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Cannot Connect!");
			}

	}
	
	private void connect(String address, int port) throws UnknownHostException, IOException {
		System.out.println("Connecting...");
		Socket client = new Socket(address, port);
		System.out.println("Connected!");
		PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		String clientInput = "";
		String serverResponse = "";
		while (!clientInput.equalsIgnoreCase("stop")) {
			clientInput = sc.nextLine();
			toServer.println(clientInput);
			try {
				serverResponse = fromServer.readLine();
			} catch (SocketException e) {
				System.out.println("Disconnected");
				break;
			}
			System.out.println("Server: " + serverResponse);
		}
		
		System.out.println("Connection Closing...");
		toServer.close();
		fromServer.close();
		client.close();
		System.out.println("Connection Closed!");
	}

}
