package com.doublev.racing.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.doublev.racing.constants.Constants;

public class Assets {
	public static BitmapFont fontOrange;
	public static BitmapFont fontGreen;
	
	public static void load() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT1_TTF));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 15;
		param.characters = Constants.FONT1_CHARACTERS;
		fontOrange = generator.generateFont(param);
		fontOrange.setColor(Color.ORANGE);
		
		fontGreen = generator.generateFont(param);
		fontGreen.setColor(Color.GREEN);
		
        generator.dispose();
	}
}
