package com.doublev.racing.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.model.Cell;

public class Walls extends Actor {
	private ShapeRenderer renderer;
	private List<Cell> walls = new ArrayList<Cell>();

	public Walls() {
		renderer = new ShapeRenderer();
	}

	public void setWalls(List<Cell> walls) {
		this.walls = walls;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.translate(getX(), getY(), 0);
		renderer.setColor(Color.BLUE);

		renderer.begin(ShapeType.Filled);
		
		for (Cell cell : walls) {
			renderer.rect(cell.i * Constants.CAR_SIZE, cell.j * Constants.CAR_SIZE, Constants.CAR_SIZE, Constants.CAR_SIZE);
		}

		renderer.end();

		batch.begin();
	}
}
