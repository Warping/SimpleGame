package com.bubbles.tanks.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.bubbles.tanks.Game;
import com.bubbles.tanks.entities.GameEntity;
import com.bubbles.tanks.net.Packet.PacketTypes;

public class GameServer extends Thread {
	
	private DatagramSocket socket;
	private Game game;
	private List<GameEntity> connectedPlayers = new ArrayList<>();
	private GameEntity host;
	
	public GameServer(Game game, int port) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while(true) {
			byte[] data = new byte[6400];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ERROR IN RUN LOOP GAME SERVER");
				break;
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			String msg = new String(packet.getData());
//			System.out.println("Client ["+packet.getAddress().getHostAddress()+":"+packet.getPort()+"]> " + msg);
//			if (msg.trim().equalsIgnoreCase("ping")) {
//				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//			}
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		//String msg = new String(data).trim();
		
		//PacketTypes type = Packet.lookupPacket(msg.substring(0, 2));
		Packet packet = new Packet(data);
		GameEntity dataEntity = packet.getDataPacketObject();
		PacketTypes type = packet.getPacketId();
		//System.out.print(address.getHostAddress()+":"+port+" > ");
		switch (type) {
			default:
				System.out.println("NONE");
				break;
			case INVALID:
				System.out.println("INVALID "+packet.getPacketId()+" "+packet.getDataPacketObject());
				break;
			case LOGIN:
				System.out.println(((PlayerDecorator)dataEntity).getUsername()+" has logged in.");
				GameEntity player = game.addOnlineClient(packet, address, port);
				this.addConnection(player, packet, address, port);
				Packet loginRelayPacket;
				for (GameEntity connectedPlayer : connectedPlayers) {
					loginRelayPacket = new Packet(PacketTypes.LOGIN, connectedPlayer);
					this.relayPacket(loginRelayPacket);
				}
				//this.relayPacket(packet);
				//this.addConnection(playerEntity, ((Packet00Login)packet));
				//game.addEntityCurrentScene(playerEntity);
				//this.connectedPlayers.add(playerEntity);
				break;
			case UPDATE:
				game.updateClient(packet, address, port);
				this.relayPacket(packet);
				//System.out.println(((PlayerDecorator)dataEntity).getUsername()+" wants to update");
				//this.relayUpdatePacket(packet, address, port);
				break;
			case DISCONNECT:
				System.out.println("NONE");
				break;
		}
		
	}

	public void addConnection(GameEntity playerEntity, Packet packet, InetAddress address, int port) {
		boolean alreadyConnected = false;
		for (GameEntity p : connectedPlayers) {
			if(((PlayerDecorator) p).getUsername().equalsIgnoreCase(((PlayerDecorator) playerEntity).getUsername())) {
				((PlayerDecorator) p).setIpAddress(address);
				((PlayerDecorator) p).setPort(port);
				alreadyConnected = true;
			}
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(playerEntity);
		}
	}
	
	public void relayPacket(Packet packet) {
		GameEntity playerEntity = packet.getDataPacketObject();
		for (GameEntity p : connectedPlayers) {
			if(((PlayerDecorator) p).getUsername().equalsIgnoreCase(((PlayerDecorator) playerEntity).getUsername()))
				continue;
			if (((PlayerDecorator) p).getPort()==0) //DO NOT RELAY TO HOST
				continue;
			sendData(packet.getData(), ((PlayerDecorator) p).getIpAddress(), ((PlayerDecorator) p).getPort());
		}
	}
	
	public void addHostConnection(GameEntity host) {
		this.connectedPlayers.add(host);
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(ipAddress.getHostAddress());
		}
	}
	
	public void sendData(byte[] data, GameEntity player) {
		if (player instanceof PlayerDecorator) {
			sendData(data,((PlayerDecorator) player).getIpAddress(), ((PlayerDecorator) player).getPort());
		}
			
	}

	public void sendDataAll(byte[] data) {
		for (GameEntity playerEntity : connectedPlayers) {
			sendData(data, ((PlayerDecorator)playerEntity).getIpAddress(), ((PlayerDecorator)playerEntity).getPort());
		}	
	}

	public List<GameEntity> getConnectedPlayers() {
		return connectedPlayers;
	}

}
