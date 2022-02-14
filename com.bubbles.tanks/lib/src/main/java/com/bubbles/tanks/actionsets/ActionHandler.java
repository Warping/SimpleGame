package com.bubbles.tanks.actionsets;

import java.io.Serializable;
import java.util.ArrayList;

import processing.core.PVector;

public class ActionHandler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4749800916228908874L;
	// Input Arrays for Keys pressed and mouse input values
	private ArrayList<Character> pressedKeys = new ArrayList<>(); // chars a-z
	private ArrayList<Integer> pressedMouseKeys = new ArrayList<>(); // LEFT RIGHT CENTER
	private PVector mousePos = new PVector(); // X and Y Pos

	public ArrayList<Character> getPressedKeys() {
		return pressedKeys;
	}

	public void setPressedKeys(ArrayList<Character> pressedKeys) {
		this.pressedKeys = pressedKeys;
	}

	public ArrayList<Integer> getPressedMouseKeys() {
		return pressedMouseKeys;
	}

	public void setPressedMouseKeys(ArrayList<Integer> pressedMouseKeys) {
		this.pressedMouseKeys = pressedMouseKeys;
	}

	public PVector getMousePos() {
		return mousePos;
	}

	public void setMousePos(PVector mousePos) {
		this.mousePos = mousePos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
