package com.doublev.racing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "racing";
		cfg.width = 320;
		cfg.height = 480;
		
		new LwjglApplication(new RacingGame(), cfg);
	}
}
