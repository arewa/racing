package com.doublev.racing.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.model.Position;

public class NextTurn implements RenderedObject {
	private final Color color = Color.LIGHT_GRAY;
	private Position position = new Position(0, 0);
	
	@Override
	public Mesh getRenderSurface() {
		MeshBuilder meshBuilder = new MeshBuilder();
		
		meshBuilder.begin(VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Color, GL10.GL_TRIANGLES);
		meshBuilder.setColor(color);
		
		Vector3 corner1 = new Vector3(position.i * Constants.MAP_CELL_SIZE, position.j * Constants.MAP_CELL_SIZE, 0);
		Vector3 corner2 = new Vector3((position.i + 1) * Constants.MAP_CELL_SIZE, position.j * Constants.MAP_CELL_SIZE, 0);
		Vector3 corner3 = new Vector3((position.i + 1) * Constants.MAP_CELL_SIZE, (position.j + 1) * Constants.MAP_CELL_SIZE, 0);
		Vector3 corner4 = new Vector3(position.i * Constants.MAP_CELL_SIZE, (position.j + 1) * Constants.MAP_CELL_SIZE, 0);
		
		meshBuilder.rect(
				corner1, 
				corner2, 
				corner3,
				corner4, 
				null);
		
		return meshBuilder.end();
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
}