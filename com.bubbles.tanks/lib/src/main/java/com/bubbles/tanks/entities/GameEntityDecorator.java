package com.bubbles.tanks.entities;

import java.io.Serializable;
import java.util.ArrayList;

import com.bubbles.tanks.actionsets.ActionHandler;

import processing.core.PImage;
import processing.core.PVector;

public abstract class GameEntityDecorator implements GameEntity, Serializable{
	
	private static final long serialVersionUID = 535450514587922451L;
	
	private final GameEntity entity;
	
	public GameEntityDecorator(GameEntity entity) {
		this.entity = entity;
	}
	
	public void setupEntity() {
		this.entity.setupEntity();
	}
	
	public void addEntity(GameEntity entity) {
		this.entity.addEntity(entity);
	}
	
	public ArrayList<GameEntity> getEntities() {
		return entity.getEntities();
	}
	
	public void setGameInput(ActionHandler player) {
		entity.setGameInput(player);
	}
	public ActionHandler getGameInput() {
		return entity.getGameInput();
	}
	public void updatePhysics() {
		entity.updatePhysics();
	}
	public void updateActions() {
		entity.updateActions();
	}
	public PVector getPos() {
		return entity.getPos();
	}
	public void setPos(PVector pos) {
		entity.setPos(pos);
	}
	public PVector getSize() {
		return entity.getSize();
	}
	public void setSize(PVector size) {
		entity.setSize(size);
	}
	public PVector getDir() {
		return entity.getDir();
	}
	public void setDir(PVector dir) {
		entity.setDir(dir);
	}
	public PVector getVel() {
		return entity.getVel();
	}
	public void setVel(PVector vel) {
		entity.setVel(vel);
	}
	public PVector getAcc() {
		return entity.getAcc();
	}
	public void setAcc(PVector acc) {
		entity.setAcc(acc);
	}
	public void addForce(PVector force) {
		entity.addForce(force);
	}
	public PImage getImg() {
		return entity.getImg();
	}
	public void setImg(PImage img) {
		entity.setImg(img);
	}

}
