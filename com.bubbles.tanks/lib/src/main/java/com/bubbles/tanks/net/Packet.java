package com.bubbles.tanks.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.bubbles.tanks.entities.GameEntity;

public class Packet {
	
	public static enum PacketTypes {
		INVALID(-1),
		LOGIN(00),
		DISCONNECT(01),
		UPDATE(02);

		private int packetId;
		
		PacketTypes(int packetId) {
			// TODO Auto-generated constructor stub
			this.packetId = packetId;
		}
		
		public int getId() {
			return packetId;
		}
		
	}
	
	private int packetId;
	private HashMap<Integer, GameEntity> packetObject = new HashMap<>();
	private GameEntity dataPacketObject;
	private byte[] data;
	
	private ByteArrayInputStream bain;
	private ObjectInputStream ois;
	private ByteArrayOutputStream baos;
	private ObjectOutputStream oos;
	
	public Packet(byte[] data) { //Receiving new Packet
		this.data = data;
		bain = new ByteArrayInputStream(data);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bain);
			packetObject = (HashMap<Integer, GameEntity>) ois.readObject();
			for (Integer id : packetObject.keySet())
					this.packetId = id;
			dataPacketObject = packetObject.get(this.packetId);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR IN PACKET RECEIVING");
		}
	}
	
	public Packet(PacketTypes packetType, GameEntity entity) { //Sending
		this.packetId = packetType.getId();
		this.dataPacketObject = entity;
		this.packetObject.put(this.packetId, entity);
		baos = new ByteArrayOutputStream();
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(packetObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR IN PACKET CREATION");
		}
		this.data = baos.toByteArray();
	}
	
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	public void writeData(GameServer server) {
		server.sendDataAll(getData());
	}
	
	public String readData(byte[] data) {
		String msg = new String(data).trim();
		return msg.substring(2);
	}

	public GameEntity getDataPacketObject() {
		return dataPacketObject;
	}

	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getId() == id) return p;
		}
		return PacketTypes.INVALID;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public PacketTypes getPacketId() {
		return Packet.lookupPacket(packetId);
	}

	public void setPacketId(byte packetId) {
		this.packetId = packetId;
	}

	public ByteArrayInputStream getBain() {
		return bain;
	}

	public void setBain(ByteArrayInputStream bain) {
		this.bain = bain;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ByteArrayOutputStream getBaos() {
		return baos;
	}

	public void setBaos(ByteArrayOutputStream baos) {
		this.baos = baos;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
}
