package com.doublev.racing;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.debug.Grid;
import com.doublev.racing.model.Player;
import com.doublev.racing.model.Position;
import com.doublev.racing.model.RaceTrack;

public class RacingGame implements ApplicationListener, InputProcessor {

	private OrthographicCamera camera;
	private Grid grid;
	private Player player;
	
	private ShapeRenderer shapeRenderer;
	
	private SpriteBatch debugBatch;
	private BitmapFont debugFont;

	private int currentWidth, currentHeight;
	private Vector3 cameraTranslationSteps;

	private Rectangle glViewport;
	
	private RaceTrack raceTrack;

	@Override
	public void create() {
		grid = new Grid();
		player = new Player();

		Texture texture = new Texture(
				Gdx.files.internal(Constants.DEBUG_FONT_PNG), true);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		debugFont = new BitmapFont(
				Gdx.files.internal(Constants.DEBUG_FONT_FNT),
				new TextureRegion(texture), false);
		debugFont.setColor(Color.MAGENTA);
		debugBatch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();
		
		raceTrack = new RaceTrack();
		raceTrack.init(10, 32);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {

		GL10 gl = Gdx.graphics.getGL10();

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		gl.glViewport((int) glViewport.x, (int) glViewport.y,
				(int) glViewport.width, (int) glViewport.height);

		camera.update();
		camera.apply(gl);

		renderGrid();
		
		renderCar();

		debug();

	}

	@Override
	public void resize(int width, int height) {

		this.currentWidth = width;
		this.currentHeight = height;

		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		glViewport = new Rectangle(0, 0, width, height);
		
		cameraTranslationSteps = new Vector3(0, 0, 0);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		debugFont.dispose();
		debugBatch.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean keyDown(int keycode) {
		
		int translateStep = 32;

		if (keycode == Input.Keys.A) {
			camera.zoom += 0.02;
		}
		if (keycode == Input.Keys.Q) {
			camera.zoom -= 0.02;
		}
		if (keycode == Input.Keys.LEFT) {
			// if (camera.position.x > w / 2)
			camera.translate(-translateStep, 0, 0);
			cameraTranslationSteps.x -= translateStep;
		}
		if (keycode == Input.Keys.RIGHT) {
			// if (camera.position.x < (tmxMap.mapWidthInPixels -
			// HALF_CAMERA_WIDTH))
			camera.translate(translateStep, 0, 0);
			cameraTranslationSteps.x += translateStep;
		}
		if (keycode == Input.Keys.UP) {
			// if (camera.position.y > HALF_CAMERA_HEIGHT +
			// HALF_TOOLS_PANEL_HEIGHT)
			camera.translate(0, translateStep, 0);
			cameraTranslationSteps.y += translateStep;
		}
		if (keycode == Input.Keys.DOWN) {
			// if (camera.position.y < (tmxMap.mapHeightInPixels -
			// HALF_CAMERA_HEIGHT - HALF_TOOLS_PANEL_HEIGHT))
			camera.translate(0, -translateStep, 0);
			cameraTranslationSteps.y -= translateStep;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	private void renderCar() {
		player.updatePosition(new Position(1, 1));
		raceTrack.updatePlayerPosition(player.getPosition());
		
		player.getRenderSurface().render(GL10.GL_TRIANGLES);
	}
	
	private void renderGrid() {
		if (!Constants.IS_DEBUG) {
			return;
		}
//		Gdx.gl.glEnable(GL10.GL_SCISSOR_TEST);
//		Gdx.gl.glScissor(0, (int) Constants.BOTTOM_TOOLS_PANEL_HEIGHT, currentWidth, (int) (currentHeight - Constants.BOTTOM_TOOLS_PANEL_HEIGHT));
		
		grid.getRenderSurface().render(GL10.GL_LINES);
		
//		Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
	}

	private void debug() {
		if (Constants.IS_DEBUG) {
			debugBatch.setProjectionMatrix(camera.combined);
			debugBatch.flush();
			Gdx.gl10.glEnable(GL10.GL_ALPHA_TEST);
			Gdx.gl10.glAlphaFunc(GL10.GL_GREATER, 0.5f);
			
			StringBuffer m = new StringBuffer().append("FPS: ")
					.append(Gdx.graphics.getFramesPerSecond()).append(", w = ")
					.append(currentWidth).append(", h = ").append(currentHeight);

			TextBounds b = debugFont.getBounds(m.toString());
			Vector3 v = new Vector3(currentWidth - b.width - 5, currentHeight
					- b.height - 5, 0.0f);
			camera.unproject(v);

			debugBatch.setProjectionMatrix(camera.combined);
			debugBatch.begin();
			debugFont.draw(debugBatch, m.toString(), v.x, v.y);
			
			debugBatch.end();

			debugBatch.flush();
			Gdx.gl10.glDisable(GL10.GL_ALPHA_TEST);
		}
	}
}