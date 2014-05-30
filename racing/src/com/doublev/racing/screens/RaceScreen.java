package com.doublev.racing.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.doublev.racing.model.World;
import com.doublev.racing.model.WorldObserver;
import com.doublev.racing.stages.RaceStage;

public class RaceScreen implements Screen {
	
	private RaceStage stage;
	
	public RaceScreen(final Game game) {
		
		stage = new RaceStage(new ScreenViewport());
		
		stage.world.addObserver(new WorldObserver() {
			@Override
			public void worldChanged(int state) {
				if (state == World.STATE_GAME_OVER) {
					game.setScreen(new GameOverScreen(game));
	    			dispose();
				}
			}
		});
		
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
