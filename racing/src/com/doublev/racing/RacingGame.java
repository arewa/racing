package com.doublev.racing;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.debug.Grid;

public class RacingGame implements ApplicationListener, InputProcessor {

	private OrthographicCamera camera;
	private Mesh grid;
	
	private Mesh bottomToolsPanel;
	
	// Car
	private Mesh myCar;
	private Mesh myCarSelectBox;
	private boolean isCarSelected = false;
	
	private ShapeRenderer shapeRenderer;
	
	private MeshBuilder meshBuilder = new MeshBuilder();
	
	private Matrix4 gridMatrix = new Matrix4();
	private SpriteBatch debugBatch;
	private BitmapFont debugFont;

	private int currentWidth, currentHeight;
	private int mouseMovedX, mouseMovedY;

	private Rectangle glViewport;

	@Override
	public void create() {
		grid = new Grid().create();
		grid.transform(gridMatrix);

		Texture texture = new Texture(
				Gdx.files.internal(Constants.DEBUG_FONT_PNG), true);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		debugFont = new BitmapFont(
				Gdx.files.internal(Constants.DEBUG_FONT_FNT),
				new TextureRegion(texture), false);
		debugFont.setColor(Color.MAGENTA);
		debugBatch = new SpriteBatch();
		
		shapeRenderer = new ShapeRenderer();

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
		
		renderMouseMarker();
		
		renderCarSelectBox();
		
		renderCar();
		
		renderBottomToolsPanel();

		StringBuffer m = new StringBuffer().append("FPS: ")
				.append(Gdx.graphics.getFramesPerSecond()).append(", w = ")
				.append(currentWidth).append(", h = ").append(currentHeight);

		debug(m.toString());

	}

	@Override
	public void resize(int width, int height) {

		this.currentWidth = width;
		this.currentHeight = height;

		camera = new OrthographicCamera(width, height);
		// camera.setToOrtho(true);
		camera.position.set(width / 2, height / 2, 0);
		glViewport = new Rectangle(0, 0, width, height);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		debugFont.dispose();
		debugBatch.dispose();
		grid.dispose();
		bottomToolsPanel.dispose();
		myCar.dispose();
		
		if (myCarSelectBox != null) {
			myCarSelectBox.dispose();
		}
		
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
		}
		if (keycode == Input.Keys.RIGHT) {
			// if (camera.position.x < (tmxMap.mapWidthInPixels -
			// HALF_CAMERA_WIDTH))
			camera.translate(translateStep, 0, 0);
		}
		if (keycode == Input.Keys.UP) {
			// if (camera.position.y > HALF_CAMERA_HEIGHT +
			// HALF_TOOLS_PANEL_HEIGHT)
			camera.translate(0, translateStep, 0);
		}
		if (keycode == Input.Keys.DOWN) {
			// if (camera.position.y < (tmxMap.mapHeightInPixels -
			// HALF_CAMERA_HEIGHT - HALF_TOOLS_PANEL_HEIGHT))
			camera.translate(0, -translateStep, 0);
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
		Vector3 touchPoint = new Vector3(x, y, 0);
		camera.unproject(touchPoint);
		
		BoundingBox carBoundingBox = myCar.calculateBoundingBox();
		
		if (carBoundingBox.contains(touchPoint)) {
			isCarSelected = true;
		} else {
			isCarSelected = false;
		}
		
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
		
		this.mouseMovedX = screenX;
		this.mouseMovedY = screenY;
		
		return false;
	}
	
	private void renderCar() {
		
		// Begin construct car
		meshBuilder.begin(VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Color, GL10.GL_TRIANGLES);
		meshBuilder.setColor(Color.ORANGE);
		
		float screenCenterX = currentWidth / 2;
		float screenCenterY = (currentHeight - Constants.BOTTOM_TOOLS_PANEL_HEIGHT) / 2;
		
		Vector3 corner1 = new Vector3(screenCenterX - Constants.HALF_CAR_SIZE, screenCenterY - Constants.HALF_CAR_SIZE, 0);
		Vector3 corner2 = new Vector3(screenCenterX - Constants.HALF_CAR_SIZE, screenCenterY + Constants.HALF_CAR_SIZE, 0);
		Vector3 corner3 = new Vector3(screenCenterX + Constants.HALF_CAR_SIZE, screenCenterY + Constants.HALF_CAR_SIZE, 0);
		Vector3 corner4 = new Vector3(screenCenterX + Constants.HALF_CAR_SIZE, screenCenterY - Constants.HALF_CAR_SIZE, 0);
		
		camera.unproject(corner1);
		camera.unproject(corner2);
		camera.unproject(corner3);
		camera.unproject(corner4);
		
		meshBuilder.rect(
				corner1, 
				corner2, 
				corner3,
				corner4, 
				null);
		myCar = meshBuilder.end();
		// End construct car
		
		myCar.render(GL10.GL_TRIANGLES);
	}
	
	private void renderMouseMarker() {
		int markerX = Math.round(this.mouseMovedX / Constants.MAP_CELL_STEP);
		int markerY = Math.round(this.mouseMovedY / Constants.MAP_CELL_STEP);
		
		Vector3 screenCoords = new Vector3(markerX * Constants.MAP_CELL_STEP, markerY * Constants.MAP_CELL_STEP, 0);
		camera.unproject(screenCoords);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		// Begin construct select marker
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.MAGENTA);
		shapeRenderer.circle(screenCoords.x, screenCoords.y, 5);
		shapeRenderer.end();
	}
	
	private void renderCarSelectBox() {
		if (!isCarSelected) {
			return;
		}
		
		float screenCenterX = currentWidth / 2;
		float screenCenterY = (currentHeight - Constants.BOTTOM_TOOLS_PANEL_HEIGHT) / 2;
		
		// Begin construct car select box
		meshBuilder.begin(VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Color, GL10.GL_TRIANGLES);
		meshBuilder.setColor(Color.RED);
		
		Vector3 corner1 = new Vector3(screenCenterX - Constants.HALF_CAR_SIZE - Constants.CAR_SELECT_BORDER_WEIGHT, screenCenterY - Constants.HALF_CAR_SIZE - Constants.CAR_SELECT_BORDER_WEIGHT, 0);
		Vector3 corner2 = new Vector3(screenCenterX - Constants.HALF_CAR_SIZE - Constants.CAR_SELECT_BORDER_WEIGHT , screenCenterY + Constants.HALF_CAR_SIZE + Constants.CAR_SELECT_BORDER_WEIGHT, 0);
		Vector3 corner3 = new Vector3(screenCenterX + Constants.HALF_CAR_SIZE + Constants.CAR_SELECT_BORDER_WEIGHT, screenCenterY + Constants.HALF_CAR_SIZE + Constants.CAR_SELECT_BORDER_WEIGHT, 0);
		Vector3 corner4 = new Vector3(screenCenterX + Constants.HALF_CAR_SIZE + Constants.CAR_SELECT_BORDER_WEIGHT, screenCenterY - Constants.HALF_CAR_SIZE - Constants.CAR_SELECT_BORDER_WEIGHT, 0);
		
		camera.unproject(corner1);
		camera.unproject(corner2);
		camera.unproject(corner3);
		camera.unproject(corner4);
		
		meshBuilder.rect(
				corner1, 
				corner2, 
				corner3,
				corner4, 
				null);
		
		myCarSelectBox = meshBuilder.end();
		// End construct car select box
		
		myCarSelectBox.render(GL10.GL_TRIANGLES);
	}
	
	private void renderBottomToolsPanel() {
		meshBuilder.begin(VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Color, GL10.GL_TRIANGLES);
		meshBuilder.setColor(Color.LIGHT_GRAY);
		
		Vector3 corner1 = new Vector3(0, currentHeight - Constants.BOTTOM_TOOLS_PANEL_HEIGHT, 0);
		Vector3 corner2 = new Vector3(0, currentHeight, 0);
		Vector3 corner3 = new Vector3(currentWidth, currentHeight, 0);
		Vector3 corner4 = new Vector3(currentWidth, currentHeight - Constants.BOTTOM_TOOLS_PANEL_HEIGHT, 0);
		
		camera.unproject(corner1);
		camera.unproject(corner2);
		camera.unproject(corner3);
		camera.unproject(corner4);
		
		meshBuilder.rect(
				corner1, 
				corner2, 
				corner3,
				corner4, 
				null);
		bottomToolsPanel = meshBuilder.end();
		
		bottomToolsPanel.render(GL10.GL_TRIANGLES);
	}
	
	private void renderGrid() {
		if (!Constants.IS_DEBUG) {
			return;
		}
		Gdx.gl.glEnable(GL10.GL_SCISSOR_TEST);
		Gdx.gl.glScissor(0, (int) Constants.BOTTOM_TOOLS_PANEL_HEIGHT, currentWidth, (int) (currentHeight - Constants.BOTTOM_TOOLS_PANEL_HEIGHT));
		
		grid.render(GL10.GL_LINES);
		
		Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
	}

	private void debug(String str) {
		if (Constants.IS_DEBUG) {
			debugBatch.setProjectionMatrix(camera.combined);
			debugBatch.flush();
			Gdx.gl10.glEnable(GL10.GL_ALPHA_TEST);
			Gdx.gl10.glAlphaFunc(GL10.GL_GREATER, 0.5f);

			TextBounds b = debugFont.getBounds(str);
			Vector3 v = new Vector3(currentWidth - b.width - 5, currentHeight
					- b.height - 5, 0.0f);
			camera.unproject(v);

			debugBatch.setProjectionMatrix(camera.combined);
			debugBatch.begin();
			debugFont.draw(debugBatch, str, v.x, v.y);
			debugBatch.end();

			debugBatch.flush();
			Gdx.gl10.glDisable(GL10.GL_ALPHA_TEST);
		}
	}
}