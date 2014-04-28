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
		
		if (myCar.calculateBoundingBox().contains(touchPoint)) {
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

// }
// private OrthographicCamera camera;
// private Rectangle glViewport;
// private ShapeRenderer batch;
// private SpriteBatch batch1;
// private SpriteBatch debugBatch;
// private BitmapFont debugInfo;
// private Sprite car;
// private int carPosX, carPosY, speed = 1;
// private boolean carCaptured = false;
// private Texture carTexture;
// private float w;
// private float h;
// private final int CELL_SIZE = 32;
// // private final int MAP_SIZE = 20;
// private final int MAP_WIDTH = 10;
// private final int MAP_HEIGHT = 20;
//
// private int mouseX, mouseY;
//
// private int[][] map = new int[MAP_WIDTH][MAP_HEIGHT];
//
// private Mesh testMesh;
//
// // private Texture texture;
// // private Sprite sprite;
//
// @Override
// public void create() {
// w = Gdx.graphics.getWidth();
// h = Gdx.graphics.getHeight();
//
// initCameraAndViewport();
//
// // camera = new OrthographicCamera(1, h / w);
// // camera.setToOrtho(true);
//
// initDebug();
//
// batch = new ShapeRenderer();
//
// // MyInputProcessor inputProcessor = new MyInputProcessor();
// Gdx.input.setInputProcessor(this);
//
// for (int i = 0; i < MAP_WIDTH; i++) {
// for (int j = 0; j < MAP_HEIGHT; j++) {
// if (i == 0 || j == 0 || (i == MAP_WIDTH - 1)
// || (j == MAP_HEIGHT - 1)
// // || (i > 5 && i < 14 && j > 5 && j < 14)
// ) {
// map[i][j] = 1;
// } else {
// map[i][j] = 0;
// }
// }
// }
//
// carTexture = new Texture(Gdx.files.internal("data/red-car.png"));
// carTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
// car = new Sprite(carTexture, 0, 0, 16, 32);
// car.flip(false, true);
//
// batch1 = new SpriteBatch();
//
// carPosX = 1;
// carPosY = 1;
//
// testMesh = createFullScreenQuad();
// }
//
// @Override
// public void dispose() {
// batch.dispose();
// batch1.dispose();
// debugBatch.dispose();
// debugInfo.dispose();
// carTexture.dispose();
// }
//
// @Override
// public void render() {
//
// // handleInput();
//
// GL10 gl = Gdx.graphics.getGL10();
//
// gl.glClearColor(1, 1, 1, 1);
// gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//
// gl.glViewport((int) glViewport.x, (int) glViewport.y,
// (int) glViewport.width, (int) glViewport.height);
//
// camera.update();
// camera.apply(gl);
//
// // batch.setProjectionMatrix(camera.combined);
// // batch.identity();
// //
// // renderGrid();
// //
// // batch.begin(ShapeType.Filled);
// //
// // for (int i = 0; i < MAP_WIDTH; i++) {
// // for (int j = 0; j < MAP_HEIGHT; j++) {
// // // for (int i = 0; i < MAP_SIZE; i ++) {
// // // for (int j = 0; j < MAP_SIZE; j ++) {
// // if (map[i][j] > 0) {
// // batch.rect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE,
// // CELL_SIZE);
// // }
// // }
// // }
// // batch.end();
// //
// // if (carCaptured) {
// // batch.begin(ShapeType.Line);
// // batch.setColor(Color.RED);
// // batch.rect(carPosX * CELL_SIZE, carPosY * CELL_SIZE, CELL_SIZE,
// // CELL_SIZE);
// // batch.end();
// //
// // batch.begin(ShapeType.Filled);
// // batch.setColor(Color.GREEN);
// // batch.rect((carPosX + 1) * CELL_SIZE, (carPosY + 1) * CELL_SIZE,
// // CELL_SIZE, CELL_SIZE);
// // batch.rect((carPosX) * CELL_SIZE, (carPosY + 1) * CELL_SIZE,
// // CELL_SIZE, CELL_SIZE);
// // batch.rect((carPosX + 1) * CELL_SIZE, (carPosY) * CELL_SIZE,
// // CELL_SIZE, CELL_SIZE);
// // batch.rect((carPosX - 1) * CELL_SIZE, (carPosY - 1) * CELL_SIZE,
// // CELL_SIZE, CELL_SIZE);
// // batch.rect((carPosX - 1) * CELL_SIZE, (carPosY) * CELL_SIZE,
// // CELL_SIZE, CELL_SIZE);
// // batch.rect((carPosX) * CELL_SIZE, (carPosY - 1) * CELL_SIZE,
// // CELL_SIZE, CELL_SIZE);
// // batch.end();
// // }
// //
// // batch1.setProjectionMatrix(camera.combined);
// // batch1.begin();
// // car.setPosition(carPosX * CELL_SIZE, carPosY * CELL_SIZE);
// // car.draw(batch1);
// // batch1.end();
// //
// // StringBuffer m = new StringBuffer().append("FPS: ")
// // .append(Gdx.graphics.getFramesPerSecond()).append(", w = ")
// // .append(w).append(", h = ").append(h);
// //
// // debug(m.toString());
//
// testMesh.render( GL10.GL_TRIANGLES );
// }
//
// @Override
// public void resize(int width, int height) {
// this.w = width;
// this.h = height;
//
// initCameraAndViewport();
// }
//
// @Override
// public void pause() {
// }
//
// @Override
// public void resume() {
// }
//

//
// private void renderGrid() {
//
// batch.begin(ShapeType.Line);
// batch.setColor(Color.GRAY);
//
// // float wh = MAP_HEIGHT * CELL_SIZE;
//
// for (int i = 0; i <= MAP_HEIGHT; i++) {
// batch.line(0, i * CELL_SIZE, CELL_SIZE * MAP_WIDTH, i * CELL_SIZE);
// // batch.line(i, 0, i, wh);
// }
//
// // wh = MAP_WIDTH * CELL_SIZE;
// for (int i = 0; i <= MAP_WIDTH; i++) {
// batch.line(i * CELL_SIZE, 0, i * CELL_SIZE, CELL_SIZE * MAP_HEIGHT);
// }
//
// // for (int i = 0; i <= wh; i += CELL_SIZE) {
// // batch.line(0, i, wh, i);
// // batch.line(i, 0, i, wh);
// // }
//
// batch.end();
// }
//
// private void initCameraAndViewport() {
// camera = new OrthographicCamera(w, h);
// camera.position.set(w / 2, h / 2, 0);
// // camera.setToOrtho(true);
//
// glViewport = new Rectangle(0, 0, w, h);
// }
//
// // private void handleInput() {
// // if (Gdx.input.isKeyPressed(Input.Keys.A)) {
// // camera.zoom += 0.02;
// // }
// // if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
// // camera.zoom -= 0.02;
// // }
// // if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
// // // if (camera.position.x > w / 2)
// // camera.translate(-3, 0, 0);
// // }
// // if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
// // // if (camera.position.x < (tmxMap.mapWidthInPixels -
// HALF_CAMERA_WIDTH))
// // camera.translate(3, 0, 0);
// // }
// // if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
// // // if (camera.position.y > HALF_CAMERA_HEIGHT +
// HALF_TOOLS_PANEL_HEIGHT)
// // camera.translate(0, -3, 0);
// // }
// // if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
// // // if (camera.position.y < (tmxMap.mapHeightInPixels -
// HALF_CAMERA_HEIGHT
// // - HALF_TOOLS_PANEL_HEIGHT))
// // camera.translate(0, 3, 0);
// // }
// // // if (Gdx.input.isKeyPressed(Input.Keys.W)) {
// // // camera.rotate(-ROTATION_SPEED, 0, 0, 1);
// // // }
// // // if (Gdx.input.isKeyPressed(Input.Keys.E)) {
// // // camera.rotate(ROTATION_SPEED, 0, 0, 1);
// // // }
// //
// // // Mouse
// // if (Gdx.input.isTouched()) {
// // mouseX = Gdx.input.getX();
// // mouseY = Gdx.input.getY();
// //
// // if ((mouseX >= carPosX * CELL_SIZE) && (mouseX <= (carPosX + 1) *
// // CELL_SIZE) &&
// // (mouseY >= carPosY * CELL_SIZE) && (mouseY <= (carPosY + 1) *
// CELL_SIZE))
// // {
// //
// // carCaptured = true;
// // } else if (carCaptured) {
// // carCaptured = false;
// //
// // carPosX = Math.round(mouseX / CELL_SIZE);
// // carPosY = Math.round(mouseY / CELL_SIZE);
// // }
// //
// // // batch.begin(ShapeType.Line);
// // // batch.setColor(Color.RED);
// // // batch.rect(0, 0, CELL_SIZE, CELL_SIZE);
// // // batch.end();
// // //worker.moveTo(Gdx.input.getX(), Gdx.input.getY());
// // }
// // }
//
// private void initDebug() {
// if (Constants.IS_DEBUG) {
//
// Texture texture = new Texture(
// Gdx.files.internal("data/font/myfont1.png"), false); // true
// // enables
// // mipmaps
// texture.setFilter(TextureFilter.MipMapLinearNearest,
// TextureFilter.Linear); // linear filtering in nearest mipmap
// // image
//
// // FileHandle fontFile = Gdx.files.internal(Constants.FONT);
// // FreeTypeFontGenerator generator = new
// // FreeTypeFontGenerator(fontFile);
// // debugInfo = generator.generateFont(Constants.FONT_SIZE,
// // Constants.CHARS, true);
// debugInfo = new BitmapFont(
// Gdx.files.internal("data/font/myfont1.fnt"),
// new TextureRegion(texture), false);
// debugInfo.setColor(Color.MAGENTA);
// debugBatch = new SpriteBatch();
// // debugBatch.setProjectionMatrix(camera.combined);
// }
// }
//
// private void debug(String str) {
// if (Constants.IS_DEBUG) {
// TextBounds b = debugInfo.getBounds(str);
// debugBatch.setProjectionMatrix(camera.combined);
// debugBatch.begin();
// debugInfo.draw(debugBatch, str, w + b.width + 5, h + b.height + 5);
// debugBatch.end();
// }
// }
//
// public Mesh createFullScreenQuad() {
//
// float[] verts = new float[20];
// int i = 0;
//
// verts[i++] = -1; // x1
// verts[i++] = -1; // y1
// verts[i++] = 0;
// verts[i++] = 0f; // u1
// verts[i++] = 0f; // v1
//
// verts[i++] = 1f; // x2
// verts[i++] = -1; // y2
// verts[i++] = 0;
// verts[i++] = 1f; // u2
// verts[i++] = 0f; // v2
//
// verts[i++] = 1f; // x3
// verts[i++] = 1f; // y2
// verts[i++] = 0;
// verts[i++] = 1f; // u3
// verts[i++] = 1f; // v3
//
// verts[i++] = -1; // x4
// verts[i++] = 1f; // y4
// verts[i++] = 0;
// verts[i++] = 0f; // u4
// verts[i++] = 1f; // v4
//
// Mesh mesh = new Mesh(true, 4, 0, // static mesh with 4 vertices and no
// // indices
// new VertexAttribute(Usage.Position, 3,
// ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
// Usage.TextureCoordinates, 2,
// ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
//
// mesh.setVertices(verts);
// return mesh;
// }

// private void handleInput() {
// if (Gdx.input.isKeyPressed(Input.Keys.A)) {
// camera.zoom += 0.02;
// }
// if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
// camera.zoom -= 0.02;
// }
// if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
// if (camera.position.x > WIDTH / 2)
// camera.translate(-3, 0, 0);
// }
// if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
// if (camera.position.x < 1024 - WIDTH / 2)
// camera.translate(3, 0, 0);
// }
// if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
// if (camera.position.y > HEIGHT / 2)
// camera.translate(0, -3, 0);
// }
// if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
// if (camera.position.y < 1024 - HEIGHT / 2)
// camera.translate(0, 3, 0);
// }
// if (Gdx.input.isKeyPressed(Input.Keys.W)) {
// camera.rotate(-rotationSpeed, 0, 0, 1);
// }
// if (Gdx.input.isKeyPressed(Input.Keys.E)) {
// camera.rotate(rotationSpeed, 0, 0, 1);
// }
// }

// mesh = new Mesh(true, 4, 4,
// new VertexAttribute(Usage.Position, 4, "a_position"));
//
// mesh.setVertices(new float[] {
// 0.0f, 1.0f, 0,
// 1.0f, 0.0f, 0});

// mesh.setIndices(new short[] { 0, 1 });

// rotationSpeed = 0.5f;
// mesh = new Mesh(true, 4, 6,
// new VertexAttribute(VertexAttributes.Usage.Position,
// 3,"attr_Position"),
// new VertexAttribute(Usage.TextureCoordinates, 2, "attr_texCoords"));
// texture = new Texture(Gdx.files.internal("data/sc_map.png"));
// mesh.setVertices(new float[] {
// 0, 0, 0, 0, 1,
// 1024f, 0, 0, 1, 1,
// 1024f, 1024f, 0, 1, 0,
// 0, 1024f, 0, 0, 0
// });
// mesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
//
// cam = new OrthographicCamera(WIDTH, HEIGHT);
// cam.position.set(WIDTH / 2, HEIGHT / 2, 0);
//
// glViewport = new Rectangle(0, 0, WIDTH, HEIGHT);

// Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
// Gdx.gl10.glLoadIdentity();
// Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
// Gdx.gl10.glLoadIdentity();
// Gdx.gl10.glColor4f(255f, 0f, 0f, 0f);
// mesh.render(GL10.GL_TRIANGLES);

// handleInput();
// GL10 gl = Gdx.graphics.getGL10();
//
// // Camera --------------------- /
// gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
// gl.glViewport((int) glViewport.x, (int) glViewport.y,
// (int) glViewport.width, (int) glViewport.height);
//
// cam.update();
// cam.apply(gl);
//
// // Texturing --------------------- /
// gl.glActiveTexture(GL10.GL_TEXTURE0);
// gl.glEnable(GL10.GL_TEXTURE_2D);
// texture.bind();
//
// mesh.render(GL10.GL_TRIANGLES);