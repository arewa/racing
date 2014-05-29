package com.doublev.racing;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.doublev.racing.helpers.Assets;
import com.doublev.racing.screens.RaceScreen;

public class RacingGame extends Game {
	
	FPSLogger fps;

	@Override
	public void create() {
		
		Assets.load();
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		setScreen(new RaceScreen());
		
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
		fps.log();
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}
}
