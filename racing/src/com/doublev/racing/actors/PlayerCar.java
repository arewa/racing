package com.doublev.racing.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.model.Cell;

public class PlayerCar extends Actor {
	private Cell cell = new Cell(0, 0);
	private SpriteBatch spriteBatch;
	private Texture texture;
	private Sprite sprite;

	public PlayerCar() {
		spriteBatch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("data/red_car.png"));
        sprite = new Sprite(texture);
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		spriteBatch.setProjectionMatrix(batch.getProjectionMatrix());
		spriteBatch.setTransformMatrix(batch.getTransformMatrix());
		
		spriteBatch.begin();
		
		sprite.setPosition(cell.i * Constants.CAR_SIZE, cell.j * Constants.CAR_SIZE);
		sprite.draw(spriteBatch);
		
		spriteBatch.end();

		batch.begin();
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
	}
}
