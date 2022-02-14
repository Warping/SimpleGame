package com.bubbles.tanks.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.bubbles.tanks.Game;
import com.bubbles.tanks.entities.GameEntity;
import com.bubbles.tanks.net.Packet.PacketTypes;

public class GameClient extends Thread {
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	private int port;
	
	public GameClient(Game game, String ipAddress, int port) {
		this.game = game;
		this.port = port;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException | UnknownHostException e) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("ERROR IN RUN LOOP GAME CLIENT");
				break;
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		Packet packet = new Packet(data);
		GameEntity dataEntity = packet.getDataPacketObject();
		PacketTypes type = packet.getPacketId();
		//System.out.print("SERVER : "+port+" > ");
		switch (type) {
			default:
				System.out.println("FUCKED!!! "+packet.getPacketId()+" "+packet.getDataPacketObject());
				break;
			case INVALID:
				System.out.println("INVALID "+packet.getPacketId()+" "+packet.getDataPacketObject());
				break;
			case LOGIN:
				System.out.println(((PlayerDecorator)dataEntity).getUsername()+" has logged in. (SERVER)");
				game.addOnlineClient(packet, address, port);
//				GameEntity playerEntity = new SimpleGameEntity(
//						new PVector(400, 400), 
//						new PVector(100,100)
//						);
//				playerEntity = new PlayerDecorator(playerEntity, ((Packet00Login)packet).getUsername(), address, port);
//				playerEntity = new TankDecorator(playerEntity);
//				playerEntity.setupEntity();
//				game.addEntityCurrentScene(playerEntity);
//				game.addEntityCurrentScene(playerEntity);
//				this.connectedPlayers.add(playerEntity);
				break;
			case UPDATE:
				//System.out.println(((PlayerDecorator)dataEntity).getUsername()+" wants to update");
				game.updateClient(packet, address, port);
				break;
			case DISCONNECT:
				break;
		}
		
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
