package com.doublev.racing.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.doublev.racing.helpers.Assets;

public class MainMenuScreen implements Screen {

	private Stage stage;
    private TextButton play, exit;
    private Table table;
    private LabelStyle labelStyle;
    private Game game;
    
    public MainMenuScreen(final Game game) {
    	
    	this.game = game;
    	
    	stage = new Stage(new ScreenViewport());
    	
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = Assets.fontGreen;

        labelStyle = new LabelStyle();
        labelStyle.font = Assets.fontGreen;
        table = new Table();
        table.setFillParent(true);

        play = new TextButton("Play", textButtonStyle);
        play.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    			Gdx.input.vibrate(20);
    			return true;
            };
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    			game.setScreen(new RaceScreen());
    			dispose();
            };
        });
        exit = new TextButton("Exit", textButtonStyle);
        exit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    			Gdx.input.vibrate(20);
    			return true;
            };
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            	Gdx.app.exit();
    			dispose();
            };
        });
        table.add(play);
        table.row();
        table.add(exit);
        stage.addActor(table);
 
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
		// TODO Auto-generated method stub
		
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
