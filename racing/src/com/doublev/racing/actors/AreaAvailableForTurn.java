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

public class AreaAvailableForTurn extends Actor {
	private ShapeRenderer renderer;
	private List<Cell> avaiableTurns = new ArrayList<Cell>();

	public AreaAvailableForTurn() {
		renderer = new ShapeRenderer();
	}
	
	public void setAvaiableTurns(List<Cell> avaiableTurns) {
		this.avaiableTurns = avaiableTurns;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.translate(getX(), getY(), 0);
		renderer.setColor(Color.LIGHT_GRAY);

		renderer.begin(ShapeType.Filled);
		
		for (Cell cell : avaiableTurns) {
			renderer.rect(cell.i * Constants.CAR_SIZE, cell.j * Constants.CAR_SIZE, Constants.CAR_SIZE, Constants.CAR_SIZE);
		}

		renderer.end();

		batch.begin();
	}
}
