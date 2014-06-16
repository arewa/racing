package com.doublev.racing.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class SmoothCameraDispatcher implements Runnable {
	
	private Camera camera;
	private Vector2 target;
	private final float DELTA = 0.5f;

	public SmoothCameraDispatcher(Camera camera, Vector2 target) {
		super();
		this.camera = camera;
		this.target = target;
	}

	@Override
	public void run() {
		float xDelta = (camera.position.x > target.x) ? -DELTA : DELTA;
		float yDelta = (camera.position.y > target.y) ? -DELTA : DELTA;
		
		Vector2 tempTarget = new Vector2(Math.abs(camera.position.x - target.x), Math.abs(camera.position.y - target.y));
		
		do {
			camera.position.x += xDelta;
			camera.position.y += yDelta;
			
			tempTarget.x -= DELTA;
			tempTarget.y -= DELTA;
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {}
			
		} while ((tempTarget.x > 0) && (tempTarget.y > 0));
	}

}
