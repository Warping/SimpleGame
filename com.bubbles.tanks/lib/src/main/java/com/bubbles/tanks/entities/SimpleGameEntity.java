package com.bubbles.tanks.entities;

import java.io.Serializable;
import java.util.ArrayList;

import com.bubbles.tanks.actionsets.ActionHandler;

import processing.core.PImage;
import processing.core.PVector;

public class SimpleGameEntity implements GameEntity, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4891508500821283783L;
	private PVector pos;
	private PVector dir;
	private PVector vel;
	private PVector acc;
	private PVector size;
	private PImage img;
	
	private ArrayList<GameEntity> entities = new ArrayList<>();
	
	private ActionHandler gameInput;
	
	public SimpleGameEntity(ActionHandler gameInput) {
		this();
		this.setGameInput(gameInput);
	}
	public SimpleGameEntity(ActionHandler gameInput, PVector pos) {
		this(gameInput);
		this.pos = pos;
	}
	public SimpleGameEntity(ActionHandler gameInput, PVector pos, PVector size) {
		this(gameInput,pos);
		this.size = size;
	}
	public SimpleGameEntity(ActionHandler gameInput, PVector pos, PVector size, PVector dir, PVector vel, PVector acc) {
		this(gameInput,pos,size);
		this.dir = dir;
		this.vel = vel;
		this.acc = acc;
	}
	
	public SimpleGameEntity(PVector pos) {
		this();
		this.pos = pos;
	}
	public SimpleGameEntity(PVector pos, PVector size) {
		this(pos);
		this.size = size;
	}
	public SimpleGameEntity(PVector pos, PVector size, PVector dir, PVector vel, PVector acc) {
		this(pos,size);
		this.dir = dir;
		this.vel = vel;
		this.acc = acc;
	}
	
	public SimpleGameEntity() { //ONLY CALL FOR NON LOCAL ENTITY
		this.setGameInput(new ActionHandler());
		this.setPos(new PVector(0,0));
		this.setAcc(new PVector(0,0));
		this.setVel(new PVector(0,0));
		this.setDir(new PVector(1,0));
		this.setSize(new PVector(0,0));
	}
	
	public void setupEntity() {
		
	}
	
	public ActionHandler getGameInput() {
		return gameInput;
	}
	public void addEntity(GameEntity entity) {
		entities.add(entity);
	}
	
	public ArrayList<GameEntity> getEntities() {
		return this.entities;
	}
	
	public void setGameInput(ActionHandler gameInput) {
		this.gameInput = gameInput;
	}
	public PVector getDir() {
		return dir;
	}
	public PVector getVel() {
		return vel;
	}
	public PVector getAcc() {
		return acc;
	}
	public PVector getSize() {
		return size;
	}
	public void setSize(PVector size) {
		this.size = size;
	}
	public PVector getPos() {
		return pos;
	}
	public void setPos(PVector pos) {
		this.pos = pos;
	}
	public PImage getImg() {
		return img;
	}
	public void setImg(PImage img) {
		this.img = img;
	}
	@Override
	public void updatePhysics() {
		this.vel.add(acc);
		this.pos.add(vel);
		if (vel.mag() < 0.5) {
			vel.set(0,0);
		}
		this.acc.set(0,0);
		for (GameEntity component : this.getEntities()) {
			
		}
	}
	public void addForce(PVector force) {
		this.acc.add(force);
	}
	@Override
	public void setDir(PVector dir) {
		this.dir = dir;		
	}
	@Override
	public void setVel(PVector vel) {
		this.vel = vel;
	}
	@Override
	public void setAcc(PVector acc) {
		this.acc = acc;
	}
	@Override
	public void updateActions() {
		
	}
	
}
