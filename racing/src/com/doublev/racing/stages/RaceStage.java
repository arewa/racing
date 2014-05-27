package com.doublev.racing.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	private Label fpsLabel;
	
	private RaceData raceData;
	private PlayerCar playerCar;
	private OpponentCar opponentCar;
	private AreaAvailableForTurn areaAvailableForTurn;
	private Walls walls;

	public RaceStage(ScreenViewport screenViewport) {
		
		super(screenViewport);
		
		raceData = new RaceData();
		raceData.init(10, 32);
		raceData.updatePlayerPosition(new Cell(6, 6));
		raceData.updateEnemyPosition(new Cell(1, 1));
		
		track = new Track();
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = FontHelper.getFont();
		
		fpsLabel = new Label("", labelStyle);
		fpsLabel.setPosition(getViewport().getViewportWidth() - 60, 10);
		
		speedLabel = new Label("", labelStyle);
		speedLabel.setPosition(10, 10);
		
		playerCar = new PlayerCar();
		playerCar.setCell(raceData.playerPosition);
		
		opponentCar = new OpponentCar();
		opponentCar.setCell(raceData.enemyPosition);
		
		areaAvailableForTurn = new AreaAvailableForTurn();
		walls = new Walls();
		walls.setWalls(raceData.walls);
		
		addActor(track);
		addActor(walls);
		addActor(areaAvailableForTurn);
		addActor(playerCar);
		addActor(opponentCar);
		addActor(fpsLabel);
		addActor(speedLabel);
		
		addListener(new ClickListener() {

			@Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				
				Vector2 point = new Vector2(x, y);
				//screenToStageCoordinates(point);
				
				Cell turn = new Cell((int)(point.x / Constants.MAP_CELL_SIZE), (int)(point.y / Constants.MAP_CELL_SIZE));
				
				if (raceData.isTurnAvaiable(turn)) {
					raceData.resetPlayerPosition();
					raceData.updatePlayerPosition(turn);
				}
    			return true;
            };
            
            
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
		
		raceData.update();
		
		areaAvailableForTurn.setAvaiableTurns(raceData.availableTurns);
		playerCar.setCell(raceData.playerPosition);
		
		speedLabel.setText(new StringBuilder().append("speed: ").append(raceData.playerSpeed).toString());
		fpsLabel.setText(new StringBuilder().append("fps: ").append(Gdx.graphics.getFramesPerSecond()).toString());
	}
}
