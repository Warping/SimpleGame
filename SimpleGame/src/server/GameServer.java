package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class GameServer {

	private ServerSocket server;
	private ArrayList<ServerClientInstance> activeClientInstances;
	private int SLOTS;
	private int PORT;
	private boolean isAlive;

	public GameServer(int SLOTS, int PORT) {
		this.SLOTS = SLOTS;
		this.PORT = PORT;
		this.isAlive = false;

	}

	public void stop() {
		if (isAlive) {
			System.out.println("Stopping Server...");
			for (ServerClientInstance client : activeClientInstances) {
				client.stop();
			}
			isAlive = false;
		} else {
			System.out.println("Server Already Stopped!");
		}
		System.out.println("Server Stopped!");
	}

	private void start() {
		try {
			System.out.println("Creating Server with " + SLOTS + " Slots...");
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Server Cannot be created!");
			return;
		}
		activeClientInstances = new ArrayList<>();
		for (int i = 1; i <= SLOTS; i++) {
			activeClientInstances.add(new ServerClientInstance(server, i));
		}
		isAlive = true;
	}

	public void restart() {
		if (isAlive) {
			stop();
		}
		if (!isAlive) {
			start();
		}
	}

	public boolean isAlive() {
		return isAlive;
	}

}
