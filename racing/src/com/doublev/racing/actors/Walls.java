package com.doublev.racing.actors;

import java.util.ArrayList;
import java.util.List;

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

public class Walls extends Actor {
	private List<Cell> walls = new ArrayList<Cell>();
	private SpriteBatch spriteBatch;
	private Texture texture;
	private Sprite sprite;

	public Walls() {
		spriteBatch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("data/wall_blue.png"));
        sprite = new Sprite(texture);
	}

	public void setWalls(List<Cell> walls) {
		this.walls = walls;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();

		spriteBatch.setProjectionMatrix(batch.getProjectionMatrix());
		spriteBatch.setTransformMatrix(batch.getTransformMatrix());
		
		spriteBatch.begin();
		
		for (Cell cell : walls) {
			sprite.setPosition(cell.i * Constants.CAR_SIZE, cell.j * Constants.CAR_SIZE);
			sprite.draw(spriteBatch);
		}
		
		spriteBatch.end();

		batch.begin();
	}
	
	
}
