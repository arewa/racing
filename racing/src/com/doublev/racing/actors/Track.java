package com.doublev.racing.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.doublev.racing.constants.Constants;

public class Track extends Actor {

	private ShapeRenderer renderer;

	public Track() {
		renderer = new ShapeRenderer();
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.translate(getX(), getY(), 0);
		renderer.setColor(Color.GREEN);

		renderer.begin(ShapeType.Line);

		for (float u = 0.0f; u <= Constants.MAP_WIDTH; u += Constants.MAP_CELL_SIZE) {
			Vector3 from = new Vector3(u, 0.0f, 0.0f);
			Vector3 to = new Vector3(u, Constants.MAP_HEIGHT, 0.0f);
			renderer.line(from, to);
		}

		for (float u = 0.0f; u <= Constants.MAP_HEIGHT; u += Constants.MAP_CELL_SIZE) {
			Vector3 from = new Vector3(0.0f, u, 0.0f);
			Vector3 to = new Vector3(Constants.MAP_WIDTH, u, 0.0f);
			renderer.line(from, to);
		}

		renderer.end();

		batch.begin();
	}
}
