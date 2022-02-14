package com.bubbles.tanks;

import java.util.ArrayList;
import java.util.HashMap;

import com.bubbles.tanks.Game.GameTypes;
import com.bubbles.tanks.actionsets.ActionHandler;
import com.bubbles.tanks.entities.GameEntity;
import com.bubbles.tanks.entities.SimpleGameEntity;
import com.bubbles.tanks.entities.attributes.TankDecorator;
import com.bubbles.tanks.net.PlayerDecorator;

import processing.core.PApplet;
import processing.core.PVector;

public class RenderLoop extends PApplet {

	// Needs Settings file ...

	private final static int WIDTH = 960;
	private final static int HEIGHT = 540;

	private HashMap<Character, Boolean> trackedKeys = new HashMap<>();
	private HashMap<Integer, Boolean> trackedMouseKeys = new HashMap<>();
	
	private static ActionHandler player = new ActionHandler();

	// Input Arrays for Keys pressed and mouse input values
	private ArrayList<Character> pressedKeys = new ArrayList<>(); // chars a-z
	private ArrayList<Integer> pressedMouseKeys = new ArrayList<>(); // LEFT RIGHT CENTER
	private PVector mousePos = new PVector(); // X and Y Pos
	
	private static Game game;
	
	

	public static ActionHandler getGameInput() {
		return player;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { RenderLoop.class.getName() });
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void setup() {
		frameRate(60);
		background(100);
		imageMode(CENTER);
		game = new Game(this);
		game.setupGame(GameTypes.ONLINE);
	}

	public void draw() {
		processInput();	
		player.setMousePos(mousePos);
		player.setPressedKeys(pressedKeys);
		player.setPressedMouseKeys(pressedMouseKeys);
		// Render Scene Based on Input
		background(0);
		//System.out.println(player.getPressedKeys());
		//currentScene.renderEntities();
		game.runGame();
	}

	private void processInput() {
		mousePos.set(mouseX, mouseY);
		pressedKeys.clear();
		pressedMouseKeys.clear();
		for (Character k : trackedKeys.keySet()) {
			if (trackedKeys.get(k))
				pressedKeys.add(k);
		}
		for (Integer k : trackedMouseKeys.keySet()) {
			if (trackedMouseKeys.get(k))
				pressedMouseKeys.add(k);
		}
	}

	public void keyTyped() {
		Character newKey = Character.toLowerCase(key);
		trackedKeys.put(newKey, true);
	}

	public void keyReleased() {
		Character newKey = Character.toLowerCase(key);
		trackedKeys.put(newKey, false);
	}

	public void mousePressed() {
		trackedMouseKeys.put(mouseButton, true);
	}

	public void mouseReleased() {
		trackedMouseKeys.put(mouseButton, false);
	}

	public void keyPressed() {
		if (key == ESC) {
			key = 0;
			if (game.getGameType().equals(GameTypes.SERVER))
			for (GameEntity e : Game.getSocketServer().getConnectedPlayers()) {
				if (e instanceof PlayerDecorator) {
					System.out.println(((PlayerDecorator)e).getUsername() + " " + 
										((PlayerDecorator)e).getIpAddress().getHostAddress() + " " +
										((PlayerDecorator)e).getPort());
				}
			}
			if (game.getGameType().equals(GameTypes.ONLINE))
				for (GameEntity e : game.getCurrentScene()) {
					if (e instanceof PlayerDecorator) {
						System.out.println(((PlayerDecorator)e).getUsername() + " " + 
											((PlayerDecorator)e).getIpAddress().getHostAddress() + " " +
											((PlayerDecorator)e).getPort());
					}
				}
		}
	}

}
