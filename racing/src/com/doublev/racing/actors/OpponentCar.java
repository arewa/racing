package com.doublev.racing.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.model.Cell;

public class OpponentCar extends Actor {
	private ShapeRenderer renderer;
	private Cell cell = new Cell(0, 0);

	public OpponentCar() {
		renderer = new ShapeRenderer();
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.translate(getX(), getY(), 0);
		renderer.setColor(Color.GREEN);

		renderer.begin(ShapeType.Filled);
		
		renderer.rect(cell.i * Constants.CAR_SIZE, cell.j * Constants.CAR_SIZE, Constants.CAR_SIZE, Constants.CAR_SIZE);

		renderer.end();

		batch.begin();
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
	}
}
