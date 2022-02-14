//package com.bubbles.tanks.net.packets;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.util.Arrays;
//import java.util.HashMap;
//
//import com.bubbles.tanks.entities.GameEntity;
//import com.bubbles.tanks.net.GameClient;
//import com.bubbles.tanks.net.GameServer;
//import com.bubbles.tanks.net.Packet;
//
//public class Packet02Update extends Packet{
//
//	private GameEntity entity;
//	byte[] data;
//	
//	private HashMap<Integer, GameEntity> objectToSend = new HashMap<>();
//	
//
//	public GameEntity getGameEntity() {
//		return entity;
//	}
//
//	public Packet02Update(byte[] data) { //Recieving
//		super(02);
//		//String msg = readData(data);
//		final ByteArrayInputStream bain = new ByteArrayInputStream(data);
//		ObjectInputStream ois;
//		try {
//			ois = new ObjectInputStream(bain);
//			this.objectToSend = (HashMap<Integer, GameEntity>) ois.readObject();
//			this.entity = objectToSend.get(02);
//		} catch (IOException | ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public Packet02Update(GameEntity entity) { //Sending
//		super(02);
//		this.entity = entity;
//		this.objectToSend.put(02, entity);
//		final ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
//		try {
//			final ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(objectToSend);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		this.data = baos.toByteArray();
//	}
//
//	@Override
//	public void writeData(GameClient client) {
//		client.sendData(getData());
//	}
//
//	@Override
//	public void writeData(GameServer server) {
//		server.sendDataAll(getData());
//	}
//
//	@Override
//	public byte[] getData() {
//		return data;
//	}
//
//}