package main;

import java.util.Scanner;

import client.GameClient;
import server.GameServer;

public class Main {
	
	final static int PORT = 50099;
	final static String ADDRESS = "127.0.0.1";

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		process(sc);
		sc.close();
		
	}
	
	private static void process(Scanner sc) {
		String input = "";
		GameServer server = null;
		System.out.println("Enter \"server\" for SERVER");
		System.out.println("Enter \"client\" for CLIENT");
		System.out.println("Enter \"end\" to close the MENU");
		while(!input.equalsIgnoreCase("end")) {
			input = sc.nextLine();
			if (input.equalsIgnoreCase("server")) {
				if (server!=null) {
					server.kill();
				}
				server = new GameServer(2, PORT);
			} else if (input.equalsIgnoreCase("client")) {
				new GameClient(ADDRESS, PORT, sc);
			}
		}
		if (server!=null) {
			server.kill();
		}
		System.out.println("Exited Menu!");
		
	}

}
