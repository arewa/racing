package com.doublev.racing.stages;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.doublev.racing.actors.AreaAvailableForTurn;
import com.doublev.racing.actors.OpponentCar;
import com.doublev.racing.actors.PlayerCar;
import com.doublev.racing.actors.Track;
import com.doublev.racing.actors.Walls;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.helpers.Assets;
import com.doublev.racing.model.Cell;
import com.doublev.racing.model.World;
import com.doublev.racing.model.WorldObserver;

public class RaceStage extends Stage {
	
	private Track track;
	
	public World world;
	private PlayerCar playerCar;
	private OpponentCar opponentCar;
	private AreaAvailableForTurn playerAreaAvailableForTurn;
	private AreaAvailableForTurn opponentAreaAvailableForTurn;
	
	private Walls walls;
	
	private Vector2 touchPoint;
	
	private OrthographicCamera guiCamera;
	private SpriteBatch batcher;

	public RaceStage(ScreenViewport screenViewport) {
		
		super(screenViewport);
		
		guiCamera = new OrthographicCamera(getViewport().getViewportWidth(), getViewport().getViewportHeight());
		guiCamera.position.set(getViewport().getViewportWidth() / 2, getViewport().getViewportHeight() / 2, 0);
		
		batcher = new SpriteBatch();
		
		world = new World();
		world.init(Constants.TRACK1_FILE);
		world.update(world.playerPosition);
		
		track = new Track(world);	
		
		playerCar = new PlayerCar();
		playerCar.setCell(world.playerPosition);
		playerAreaAvailableForTurn = new AreaAvailableForTurn(Color.LIGHT_GRAY);
		playerAreaAvailableForTurn.setAvailableTurns(world.availableTurns);
		
		opponentCar = new OpponentCar();
		opponentCar.setCell(world.opponentPosition);
		opponentAreaAvailableForTurn = new AreaAvailableForTurn(Color.TEAL);
		opponentAreaAvailableForTurn.setAvailableTurns(world.opponentAvailableTurns);
		opponentAreaAvailableForTurn.setVisible(false);
		
		walls = new Walls();
		walls.setWalls(world.walls);
		
		addActor(track);
		
		addActor(opponentAreaAvailableForTurn);
		addActor(playerAreaAvailableForTurn);
		
		addActor(walls);
		
		addActor(opponentCar);
		addActor(playerCar);
		
		addListener(new ClickListener() {

			@Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				touchPoint = new Vector2(x, y);
				
    			return true;
            }

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Cell turn = new Cell((int)(touchPoint.x / Constants.MAP_CELL_SIZE), (int)(touchPoint.y / Constants.MAP_CELL_SIZE));
				
				if (world.isTurnAvaiable(turn)) {
					world.update(turn);
					
					playerAreaAvailableForTurn.setAvailableTurns(world.availableTurns);
					playerCar.setCell(world.playerPosition);
					
					opponentAreaAvailableForTurn.setAvailableTurns(world.opponentAvailableTurns);
					opponentCar.setCell(world.opponentPosition);
					
					getCamera().position.lerp(new Vector3(x, y, 0), 0.3f);
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
	public void draw() {
		super.draw();
		
		guiCamera.update();
		batcher.setProjectionMatrix(guiCamera.combined);
		batcher.enableBlending();
		
		batcher.begin();
		Assets.fontOrange.draw(batcher, new StringBuilder().append("speed: ").append(world.playerSpeed), 5, 15);
		Assets.fontGreen.draw(batcher, new StringBuilder().append("speed: ").append(world.opponentSpeed), 80, 15);
		batcher.end();
	}
}
