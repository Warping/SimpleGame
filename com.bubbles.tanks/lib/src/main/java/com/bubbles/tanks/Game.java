package com.bubbles.tanks;

import java.net.InetAddress;

import com.bubbles.tanks.Game.GameTypes;
import com.bubbles.tanks.actionsets.ActionHandler;
import com.bubbles.tanks.entities.GameEntity;
import com.bubbles.tanks.entities.SimpleGameEntity;
import com.bubbles.tanks.entities.attributes.TankDecorator;
import com.bubbles.tanks.net.GameClient;
import com.bubbles.tanks.net.GameServer;
import com.bubbles.tanks.net.Packet;
import com.bubbles.tanks.net.Packet.PacketTypes;
import com.bubbles.tanks.net.PlayerDecorator;
import com.bubbles.tanks.scenes.GameScene;

import processing.core.PVector;

public class Game {
	
	private RenderLoop gameLoop;
	private GameScene currentScene;
	private GameTypes gameType;
	private GameEntity mainClientEntity;
	
	private static GameClient socketClient;
	private static GameServer socketServer;

	public GameScene getCurrentScene() {
		return currentScene;
	}
	
	public GameTypes getGameType() {
		return gameType;
	}

	public GameEntity getMainClientEntity() {
		return mainClientEntity;
	}

	public ActionHandler getGameInput() {
		return RenderLoop.getGameInput();
	}


	public static GameClient getSocketClient() {
		return socketClient;
	}

	public static GameServer getSocketServer() {
		return socketServer;
	}
	
	public Game(RenderLoop gameLoop) {
		this.gameLoop = gameLoop;
		GameScene.setGameLoop(gameLoop);
		currentScene = new GameScene();
	}

	public void setupGame(GameTypes gameType) {
		this.gameType = gameType;
		switch(gameType) {
		default:
		case LOCAL:
			this.addLocalClient("LocalPlayer");
			break;
		case ONLINE:
			mainClientEntity = this.addLocalClient("OnlinePlayer3"); //Creates Local Client
			this.setupClient("localhost", 2093); //Establish Client Socket
			this.requestOnlineClient(mainClientEntity); //Sends out login packet
			break;
		case SERVER:
			mainClientEntity = this.addLocalClient("HostPlayer");
			this.setupServer(2093); //Opens Server Socket on port
			socketServer.addHostConnection(mainClientEntity);
			break;
		}
	}
	public void runGame() {
		currentScene.renderEntities();
		if (getGameType().equals(GameTypes.ONLINE))
			requestUpdateClient(getMainClientEntity());
		else if (getGameType().equals(GameTypes.SERVER)) {
			Packet updatePacket = new Packet(PacketTypes.UPDATE, mainClientEntity);
			socketServer.relayPacket(updatePacket);
		}
		
	}
	
	private GameEntity addLocalClient(String username) {
		GameEntity localPlayer = new SimpleGameEntity(
				new PVector(600, 400),
				new PVector(100,100));
		localPlayer.setGameInput(getGameInput());
		localPlayer = new TankDecorator(localPlayer);
		localPlayer = new PlayerDecorator(localPlayer, username);
		localPlayer.setupEntity();
		currentScene.add(localPlayer);
		return localPlayer;
	}
	
	public GameEntity addOnlineClient(Packet loginPacket, InetAddress address, int port) {
		boolean alreadyConnected = false;
		for (GameEntity p : currentScene) {
			if (!(p instanceof PlayerDecorator))
				continue;
			if (((PlayerDecorator)p).getUsername()
					.equalsIgnoreCase(((PlayerDecorator)loginPacket.getDataPacketObject()).getUsername())) {
				alreadyConnected = true;
				break;
			}
		}
		if (alreadyConnected) return loginPacket.getDataPacketObject();
		GameEntity onlinePlayer = loginPacket.getDataPacketObject();
		((PlayerDecorator)onlinePlayer).setIpAddress(address);
		((PlayerDecorator)onlinePlayer).setPort(port);
//		GameEntity onlinePlayer = new SimpleGameEntity(
//				new PVector(400, 400),
//				new PVector(100,100));
//		if (address.getHostAddress().equalsIgnoreCase("127.0.0.1"))
//			onlinePlayer.setGameInput(getGameInput());
//		onlinePlayer = new TankDecorator(onlinePlayer);
//		onlinePlayer = new PlayerDecorator(onlinePlayer, 
//				((PlayerDecorator)loginPacket.getDataPacketObject()).getUsername(), address, port);
		//onlinePlayer.setupEntity();
		currentScene.add(onlinePlayer);
		return onlinePlayer;
	}
	
	public void updateClient(Packet packet, InetAddress address, int port) {
		boolean alreadyConnected = false;
		GameEntity requestingPlayer = packet.getDataPacketObject();
		for (GameEntity p : currentScene) {
			if (!(p instanceof PlayerDecorator))
				continue;
			if (((PlayerDecorator)requestingPlayer).getUsername()
					.equalsIgnoreCase(((PlayerDecorator)p).getUsername())) {
				p.setGameInput(requestingPlayer.getGameInput());
				p.setAcc(requestingPlayer.getAcc());
				p.setVel(requestingPlayer.getVel());
				p.setDir(requestingPlayer.getDir());
				p.setPos(requestingPlayer.getPos());
				if (getGameType().equals(GameTypes.SERVER))
					//packet.writeData(socketServer);
				alreadyConnected = true;
				break;
			}
		}
		if (!alreadyConnected) {
			packet = new Packet(PacketTypes.LOGIN, requestingPlayer);
			this.addOnlineClient(packet, address, port);
		}
	}
	
	public void setupClient(String ipAddress, int port) {
		socketClient = new GameClient(this, ipAddress, port);
		socketClient.start();
	}
	
	public void requestOnlineClient(GameEntity player) {
		Packet loginPacket = new Packet(PacketTypes.LOGIN, player);
		loginPacket.writeData(socketClient);
	}
	
	public void requestUpdateClient(GameEntity entity) {
		Packet updatePacket = new Packet(PacketTypes.UPDATE, entity);
		updatePacket.writeData(socketClient);
	}
	
	private void setupServer(int port) {
		socketServer = new GameServer(this, port);
		socketServer.start();
		//socketServer.addHostConnection(host);
	}
	
	public void addEntityCurrentScene(GameEntity entity) {
		currentScene.add(entity);
	}
	
	private void listServer() {
		for (GameEntity p : socketServer.getConnectedPlayers()) {
			if (p instanceof PlayerDecorator) {
				System.out.println(((PlayerDecorator)p).getUsername());
			}
		}
	}

	public static enum GameTypes {
		LOCAL(0),
		ONLINE(1),
		SERVER(2),
		INVALID(-1);
		
		private int gameId;
		
		GameTypes(int gameId) {
			// TODO Auto-generated constructor stub
			this.gameId = gameId;
		}
		
		public int getId() {
			return gameId;
		}
	}

}
