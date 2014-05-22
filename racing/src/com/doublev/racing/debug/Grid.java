package com.doublev.racing.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.render.RenderedObject;

public class Grid implements RenderedObject {
	private final Color color = Color.GREEN;
	private Matrix4 gridMatrix = new Matrix4();

	@Override
	public Mesh getRenderSurface() {
		MeshBuilder meshBuilder = new MeshBuilder();
		meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Color, GL10.GL_LINES);
		meshBuilder.setColor(this.color);

		for (float u = 0.0f; u <= Constants.MAP_WIDTH; u += Constants.MAP_CELL_SIZE) {
			Vector3 from = new Vector3(u, 0.0f, 0.0f);
			Vector3 to = new Vector3(u, Constants.MAP_HEIGHT, 0.0f);
			meshBuilder.line(from, to);
		}
		
		for (float u = 0.0f; u <= Constants.MAP_HEIGHT; u += Constants.MAP_CELL_SIZE) {
			Vector3 from = new Vector3(0.0f, u, 0.0f);
			Vector3 to = new Vector3(Constants.MAP_WIDTH, u, 0.0f);
			meshBuilder.line(from, to);
		}
		
		Mesh grid = meshBuilder.end();
		grid.transform(gridMatrix);
		
		return grid;
	}
}
