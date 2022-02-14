package com.bubbles.tanks.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.bubbles.tanks.entities.GameEntity;
import com.bubbles.tanks.entities.GameEntityDecorator;

public class PlayerDecorator extends GameEntityDecorator {
	
	private static final long serialVersionUID = -290548923808201778L;
	
	private InetAddress ipAddress;
	private int port;
	private String username;
	
	public PlayerDecorator(GameEntity entity, String username, InetAddress ipAddress, int port) {
		super(entity);
		this.username = username;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public PlayerDecorator(GameEntity entity, String username) {
		super(entity);
		this.username = username;
		try {
			this.ipAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.port = 0;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}
