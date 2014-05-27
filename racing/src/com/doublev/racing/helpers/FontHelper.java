package com.doublev.racing.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.doublev.racing.constants.Constants;

public class FontHelper {
	
	private static BitmapFont font;
	
	public static void init(int fontSize) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT1_TTF));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 15;
		param.characters = Constants.FONT1_CHARACTERS;
		font = generator.generateFont(param);
        font.setColor(Color.WHITE);
        generator.dispose();
	}
	
	public static BitmapFont getFont() {
		return font;
	}
}
