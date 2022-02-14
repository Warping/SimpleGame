package com.bubbles.tanks.entities;

import java.util.ArrayList;

import com.bubbles.tanks.actionsets.ActionHandler;

import processing.core.PImage;
import processing.core.PVector;

public interface GameEntity {
	
	public PVector getPos(); //Position
	public PVector getDir(); //Direction
	public PVector getVel(); //Velocity
	public PVector getAcc(); //Acceleration
	public PVector getSize(); //Size
	public PImage getImg(); //Sprite image
	
	public void setupEntity(); //General Setup function which runs before rendering
	
	public void updatePhysics(); //update movement
	public void updateActions(); //update actions
	
	public void addForce(PVector force); //add movement force
	
	public void setGameInput(ActionHandler player); //sets reference inputs
	public ActionHandler getGameInput();
	
	public void addEntity(GameEntity entity); //adds entity to entity render list
	public ArrayList<GameEntity> getEntities(); //gets Entity list to render
	
	public void setPos(PVector pos);
	public void setDir(PVector dir);
	public void setVel(PVector vel);
	public void setAcc(PVector acc);
	public void setSize(PVector size);
	public void setImg(PImage img);
}
