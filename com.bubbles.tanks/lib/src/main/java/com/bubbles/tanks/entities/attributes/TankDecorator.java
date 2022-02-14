package com.bubbles.tanks.entities.attributes;

import java.util.ArrayList;

import com.bubbles.tanks.entities.GameEntity;
import com.bubbles.tanks.entities.GameEntityDecorator;
import com.bubbles.tanks.entities.SimpleGameEntity;

import processing.core.PVector;

public class TankDecorator extends GameEntityDecorator {
	
	private static final long serialVersionUID = 9210925956624674090L;
	
	private static final float ACCELERATION = 2.0F; //Declare specific entity variables to use
	private static final float DAMPENING = 0.8F;
	
	private static final float BODY_ROT_SPEED = 0.08F;

	private static final float BARREL_ROT_SPEED = 0.18F;
	
	private GameEntity barrel; //Declare any component entities (barrel or other independently moving parts)

	public TankDecorator(GameEntity entity) { //Every Decorator Class will take a GameEntity as a parameter
		super(entity); //Call constructor for parent class
	}
	
	
	//Function is called before first rendering in a scene (Setup any component entity)
	public void setupEntity() {
		super.setupEntity();
		barrel = new SimpleGameEntity(this.getGameInput()); //Define any attached entities
		barrel.setPos(getPos()); //Position to render initial entity
		barrel.setSize(this.getSize().copy().mult(0.5F)); //Define Size
		this.addEntity(barrel); //Add new Entity to list of entities which makeup this entity
	}
	
	//Handles all non-physics related updates (handles sub entity behavior)
	public void updateActions() {
		barrel.setPos(this.getPos());
		super.updateActions();
	}
	
	//Handles all movement physics updates
	public void updatePhysics() {
		ArrayList<Character> keyList = this.getGameInput().getPressedKeys();
		ArrayList<Integer> mouseKeyList = this.getGameInput().getPressedMouseKeys();
		PVector mousePos = this.getGameInput().getMousePos().copy(); //Global Mouse Pos
		PVector targetDir = mousePos.copy();
		targetDir.sub(this.getPos()); //Vector pointing from center of tank to mouse
		
		handleMovement(keyList);
		handleBodyRotation(keyList);
		rotateBarrel(targetDir);
		
		//System.out.println(this.getAcc() + " " + this.getGameInput().getPressedKeys());
		super.updatePhysics();
	}
	
	private void handleMovement(ArrayList<Character> keyList) {
		PVector _acc = new PVector(0,0);
		PVector _vel = this.getVel().copy();
		PVector _dir = this.getDir().copy();
		if (keyList.contains('w') == keyList.contains('s')) { //Deceleration
			_acc = new PVector(0,0);
		} else if (keyList.contains('w')) { //Acceleration Forward
			_dir.mult(this.ACCELERATION);
			_acc.set(_dir);
		} else if (keyList.contains('s')) { //Acceleration Backward
			_dir.mult(-this.ACCELERATION);
			_acc.set(_dir);
		}
		this.addForce(_acc); //Add movement force
		_vel.mult(this.DAMPENING - 1);
		this.addForce(_vel); //Adds dampening force
	}
	
	private void handleBodyRotation(ArrayList<Character> keyList) {
		PVector _dir = this.getDir().copy();
		float _angle = _dir.heading();
		if (keyList.contains('a')) {
			_angle -= this.BODY_ROT_SPEED;
		}
		if (keyList.contains('d')) {
			_angle += this.BODY_ROT_SPEED;
		}
		_dir.set(PVector.fromAngle(_angle));
		this.setDir(_dir);
	}
	
	private void rotateBarrel(PVector targetDir) {
		PVector unitTarget = targetDir.copy().normalize();
		PVector newHeading = PVector.lerp(barrel.getDir(), unitTarget, this.BARREL_ROT_SPEED);
		newHeading.normalize();
		barrel.setDir(newHeading);
	}

}
