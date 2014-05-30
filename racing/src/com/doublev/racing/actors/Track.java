package com.doublev.racing.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.model.World;

public class Track extends Actor {

	private ShapeRenderer renderer;
	private World world;

	public Track(World world) {
		renderer = new ShapeRenderer();
		this.world = world;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.translate(getX(), getY(), 0);
		renderer.setColor(Color.GREEN);

		renderer.begin(ShapeType.Line);
		
		float maxWidth = world.trackWidth * Constants.MAP_CELL_SIZE;
		float maxHeight = world.trackHeight * Constants.MAP_CELL_SIZE;

		for (float u = 0.0f; u <= maxWidth; u += Constants.MAP_CELL_SIZE) {
			Vector3 from = new Vector3(u, 0.0f, 0.0f);
			Vector3 to = new Vector3(u, maxHeight, 0.0f);
			renderer.line(from, to);
		}

		for (float u = 0.0f; u <= maxHeight; u += Constants.MAP_CELL_SIZE) {
			Vector3 from = new Vector3(0.0f, u, 0.0f);
			Vector3 to = new Vector3(maxWidth, u, 0.0f);
			renderer.line(from, to);
		}

		renderer.end();

		batch.begin();
	}
}
