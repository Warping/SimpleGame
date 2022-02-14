package bubbles.tank;

import java.io.Serializable;
import java.util.ArrayList;

import processing.core.PVector;

public class TankBody implements Serializable {

	private static final long serialVersionUID = 2105623285196910875L;
	
	final float ACCELERATION = 1.2F;
	final float DECCELERATION = 1.2F;
	
	final float TOP_SPEED = 9.0F;
	
	final float SPEED = 5.0F;
	final float BODY_ROT_SPEED = 0.08F;

	final float BARREL_ROT_SPEED = 0.18F;

	private PVector barrelDir;

	private PVector bodyDir;

	private PVector pos;
	
	private float velocity;

	public TankBody() {
		this(new PVector(400, 400));
	}

	TankBody(PVector _pos) {
		barrelDir = new PVector(0, 0);
		bodyDir = new PVector(0, 0);
		pos = _pos;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public PVector getPos() {
		return pos;
	}

	public PVector getBarrelDir() {
		return barrelDir;
	}

	public PVector getBodyDir() {
		return bodyDir;
	}

	public void update(PVector mousePos, ArrayList<Character> keyList, boolean mouseClicked) {
		float angle = 0;
		float _vel = 0;
		if (velocity != 0) {
			System.out.println(velocity);
		}
		if (keyList.contains('w') == keyList.contains('s')) {
			if (velocity > 0) {
				_vel = velocity - DECCELERATION;
				velocity = bound(_vel, 0, TOP_SPEED);
			} 
			else if (velocity < 0){
				_vel = velocity + DECCELERATION;
				velocity = bound(_vel, -TOP_SPEED, 0);
			}
		}
		else if (keyList.contains('w')) {
			_vel = velocity + ACCELERATION;
			velocity = bound(_vel, -TOP_SPEED, TOP_SPEED);
		}
			
		else if (keyList.contains('s')) {
			_vel = velocity - ACCELERATION;
			velocity = bound(_vel, -TOP_SPEED, TOP_SPEED);
		}
		

		if (keyList.contains('a'))
			angle -= BODY_ROT_SPEED;
		if (keyList.contains('d'))
			angle += BODY_ROT_SPEED;

		moveBarrel(mousePos);
		moveTank(velocity, angle);
		if (mouseClicked)
			shootProjectile(mousePos);
	}

	void moveTank(float velocity, float angle) {
		PVector movementVec = bodyDir.copy();
		movementVec.mult(velocity);
		pos.add(movementVec);
		bodyDir.set(PVector.fromAngle(angle + bodyDir.heading()));
	}

	void moveBarrel(PVector target) {
		PVector unitTarget = target.normalize();
		float x = lerp(barrelDir.x, unitTarget.x, BARREL_ROT_SPEED);
		float y = lerp(barrelDir.y, unitTarget.y, BARREL_ROT_SPEED);
		PVector newHeading = new PVector(x, y);
		newHeading.normalize();
		barrelDir.set(newHeading);
	}

	float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
	
	float bound(float num, float lower, float upper) {
		if (num > upper) return upper;
		else if (num < lower) return lower;
		else return num;
	}

	void shootProjectile(PVector targetPos) {
//		push();
//		translate(pos.x, pos.y);
//		stroke(100, 100, 255);
//		strokeWeight(5);
//		line(0, 0, targetPos.x, targetPos.y);
//		pop();
	}
}
