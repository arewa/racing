package com.doublev.racing;

import com.badlogic.gdx.Game;
import com.doublev.racing.helpers.FontHelper;
import com.doublev.racing.screens.RaceScreen;

public class RacingGame extends Game {

	@Override
	public void create() {
		
		FontHelper.init(15);
		
		setScreen(new RaceScreen());
	}
	
	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
