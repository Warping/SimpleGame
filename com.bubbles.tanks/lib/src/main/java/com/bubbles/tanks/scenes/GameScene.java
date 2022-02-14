package com.bubbles.tanks.scenes;

import java.util.ArrayList;

import com.bubbles.tanks.RenderLoop;
import com.bubbles.tanks.entities.GameEntity;

public class GameScene extends ArrayList<GameEntity>{
	
	private static final long serialVersionUID = 6350653949741837796L;

	private static RenderLoop gameLoop;
	
	public static void setGameLoop(RenderLoop loop) {
		gameLoop = loop;
	}
	
	public void renderEntities() {
		//gameLoop.image(entity.getImg(), entity.getPos().x, entity.getPos().y);
		for (int i = 0; i < this.size(); i++) {
			GameEntity entity = this.get(i);
			entity.updatePhysics();
			entity.updateActions();
			gameLoop.pushMatrix();
			gameLoop.fill(100);
			gameLoop.stroke(200,0,100);
			gameLoop.rectMode(RenderLoop.CENTER);
			gameLoop.translate(entity.getPos().x, entity.getPos().y);
			gameLoop.rotate(entity.getDir().heading());
			gameLoop.rect(0, 0, entity.getSize().x, entity.getSize().y);
			gameLoop.line(0, 0, entity.getSize().x, 0);
			gameLoop.popMatrix();
			for (GameEntity componentEntity : entity.getEntities()) {
				gameLoop.pushMatrix();
				gameLoop.fill(200);
				gameLoop.stroke(0,200,200);
				gameLoop.rectMode(RenderLoop.CENTER);
				gameLoop.translate(componentEntity.getPos().x, componentEntity.getPos().y);
				gameLoop.rotate(componentEntity.getDir().heading());
				gameLoop.rect(0, 0, componentEntity.getSize().x, componentEntity.getSize().y);
				gameLoop.line(0, 0, componentEntity.getSize().x, 0);
				gameLoop.popMatrix();
			}
		}
		
	}

}
