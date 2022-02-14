package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import bubbles.tank.TankBody;
import client.GameClient;
import processing.core.PApplet;
import processing.core.PVector;
import server.GameServer;

public class Main extends PApplet {

	final static int PORT = 50099;
	final static String ADDRESS = "127.0.0.1";

	final static int WIDTH = 1920;
	final static int HEIGHT = 1080;

	private HashMap<Character, Boolean> pressedKeys = new HashMap<Character, Boolean>();
	private boolean mouseClicked = false;
	private PVector mousePos = new PVector();
	private ArrayList<Character> keyList = new ArrayList<Character>();

	private TankBody tank;

	public static void main(String[] args) {
		PApplet.main(new String[] { Main.class.getName() });
		System.out.println("DONE");
	}

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	public void setup() {
		frameRate(60);
		background(100);
		tank = new TankBody();
		background(100);
	}

	public void draw() {
		background(100);
		circle(300, 300, 50);
		keyList.clear();
		for (Character k : pressedKeys.keySet()) {
			if (pressedKeys.get(k))
				keyList.add(k);
		}
		mousePos.set(mouseX - tank.getPos().x, mouseY - tank.getPos().y);
		render(tank);
		tank.update(mousePos, keyList, mouseClicked);
	}

//	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		process(sc);
//		sc.close();
//		
//		
//	}

	public void keyTyped() {
		pressedKeys.put(key, true);
	}

	public void keyReleased() {
		pressedKeys.put(key, false);
	}

	public void mousePressed() {
		mouseClicked = (mouseButton == 37);
	}

	public void mouseReleased() {
		mouseClicked = false;
	}

	public void render(TankBody tank) {
		push();

		translate(tank.getPos().x, tank.getPos().y);

		push();
		rectMode(CENTER);
		translate(0, 0);
		rotate((float) (tank.getBodyDir().heading() + Math.PI / 2.0));
		noStroke();
		fill(100, 0, 0);
		rect(0, 0, 100, 150); // body tank
		fill(20, 255, 255);
		rect(0, -65, 100, 20); // front window
		stroke(0);
		strokeWeight(3);
		fill(0, 100, 0);
		rect(0 - 65, 0, 30, 170); // wheel left
		rect(0 + 65, 0, 30, 170); // wheel right

		pop();

		push();

		rectMode(CENTER);
		translate(0, 0);
		rotate((float) (tank.getBarrelDir().heading() + Math.PI / 2.0));
		rect(0, 0 - 45, 12, 110); // barrel
		rect(0, 0, 50, 50); // barrel body
		pop();

		pop();
	}

	private static void process(Scanner sc) {
		String input = "";
		GameServer server = new GameServer(1, PORT);
		System.out.println("Enter \"server\" for SERVER");
		System.out.println("Enter \"client\" for CLIENT");
		System.out.println("Enter \"end\" to close the MENU");
		while (!input.equalsIgnoreCase("end")) {
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
