package com.doublev.racing.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.doublev.racing.actors.AreaAvailableForTurn;
import com.doublev.racing.actors.OpponentCar;
import com.doublev.racing.actors.PlayerCar;
import com.doublev.racing.actors.Track;
import com.doublev.racing.actors.Walls;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.helpers.FontHelper;
import com.doublev.racing.model.Cell;
import com.doublev.racing.model.RaceData;

public class RaceStage extends Stage {
	
	private Track track;
	private Label speedLabel;
	private Label opponentSpeedLabel;
	private Label fpsLabel;
	
	private RaceData raceData;
	private PlayerCar playerCar;
	private OpponentCar opponentCar;
	private AreaAvailableForTurn playerAreaAvailableForTurn;
	private AreaAvailableForTurn opponentAreaAvailableForTurn;
	
	private Walls walls;
	
	private Vector2 touchPoint;

	public RaceStage(ScreenViewport screenViewport) {
		
		super(screenViewport);
		
		raceData = new RaceData();
		raceData.init(Constants.TRACK1_FILE);
		raceData.update(raceData.playerPosition);
		
		track = new Track(raceData);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = FontHelper.getFont();
		
		fpsLabel = new Label("", labelStyle);
		fpsLabel.setPosition(getViewport().getViewportWidth() - 60, 10);
		
		speedLabel = new Label("", labelStyle);
		speedLabel.setPosition(5, 10);
		speedLabel.setText(new StringBuilder().append("speed: ").append(raceData.playerSpeed).toString());
		
		opponentSpeedLabel = new Label("", labelStyle);
		opponentSpeedLabel.setPosition(90, 10);
		opponentSpeedLabel.setText(new StringBuilder().append("opp speed: ").append(raceData.opponentSpeed).toString());
		
		playerCar = new PlayerCar();
		playerCar.setCell(raceData.playerPosition);
		playerAreaAvailableForTurn = new AreaAvailableForTurn(Color.LIGHT_GRAY);
		playerAreaAvailableForTurn.setAvailableTurns(raceData.availableTurns);
		
		opponentCar = new OpponentCar();
		opponentCar.setCell(raceData.opponentPosition);
		opponentAreaAvailableForTurn = new AreaAvailableForTurn(Color.TEAL);
		opponentAreaAvailableForTurn.setAvailableTurns(raceData.opponentAvailableTurns);
		
		opponentAreaAvailableForTurn.setVisible(false);
		
		walls = new Walls();
		walls.setWalls(raceData.walls);
		
		addActor(track);
		addActor(walls);
		
		addActor(opponentAreaAvailableForTurn);
		addActor(playerAreaAvailableForTurn);
		
		addActor(opponentCar);
		addActor(playerCar);
		
		addActor(fpsLabel);
		addActor(speedLabel);
		addActor(opponentSpeedLabel);
		
		addListener(new ClickListener() {

			@Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				touchPoint = new Vector2(x, y);
				
    			return true;
            }

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Cell turn = new Cell((int)(touchPoint.x / Constants.MAP_CELL_SIZE), (int)(touchPoint.y / Constants.MAP_CELL_SIZE));
				
				if (raceData.isTurnAvaiable(turn)) {
					raceData.update(turn);
					
					playerAreaAvailableForTurn.setAvailableTurns(raceData.availableTurns);
					playerCar.setCell(raceData.playerPosition);
					
					opponentAreaAvailableForTurn.setAvailableTurns(raceData.opponentAvailableTurns);
					opponentCar.setCell(raceData.opponentPosition);
					
					speedLabel.setText(new StringBuilder().append("speed: ").append(raceData.playerSpeed).toString());
					opponentSpeedLabel.setText(new StringBuilder().append("opp speed: ").append(raceData.opponentSpeed).toString());
				}
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				getCamera().translate(touchPoint.x - x, touchPoint.y - y, 0);
			}
            
		});
	}
	
	@Override
    public boolean keyDown(int keyCode) {
		
		int translateStep = 32;
		
		if (keyCode == Input.Keys.LEFT) {
			getCamera().translate(-translateStep, 0, 0);
		}
		
		if (keyCode == Input.Keys.RIGHT) {
			getCamera().translate(translateStep, 0, 0);
		}
		
		if (keyCode == Input.Keys.UP) {
			getCamera().translate(0, translateStep, 0);
		}
		
		if (keyCode == Input.Keys.DOWN) {
			getCamera().translate(0, -translateStep, 0);
		}

		return false;
    }


	@Override
	public void act(float delta) {
		super.act(delta);

		fpsLabel.setText(new StringBuilder().append("fps: ").append(Gdx.graphics.getFramesPerSecond()).toString());
	}
}
