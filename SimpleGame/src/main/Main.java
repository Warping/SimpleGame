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
		GameServer server = new GameServer(1, PORT);
		System.out.println("Enter \"server\" for SERVER");
		System.out.println("Enter \"client\" for CLIENT");
		System.out.println("Enter \"end\" to close the MENU");
		while(!input.equalsIgnoreCase("end")) {
			input = sc.nextLine();
			if (input.equalsIgnoreCase("server")) {
				server.restart();
			} else if (input.equalsIgnoreCase("client")) {
				new GameClient(ADDRESS, PORT, sc);
			}
		}
		server.stop();
		System.out.println("Exited Menu!");
		
	}

}
