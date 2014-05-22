package com.doublev.racing.model;

public class RaceTrack {
	
	public static short PLAYER = 2;
	public static short WALL = 1;
	public static short ENEMY = 3;
	public static short EMPTY = 0;
	
	public int[][] trackData;
	public int width;
	public int height;
	
	public void init(int i, int j) {
		this.width = i;
		this.height = j;
		this.trackData = new int[i][j];
		
		// Manually init walls
		this.trackData[0][0] = WALL;
		this.trackData[0][2] = WALL;
		this.trackData[0][5] = WALL;
		this.trackData[2][5] = WALL;
		this.trackData[1][0] = WALL;
		this.trackData[2][2] = WALL;
	}
	
	public void updatePlayerPosition(Position pos) {
		trackData[pos.i][pos.j] = PLAYER;
	}
	
	public void updateEnemyPosition(Position pos) {
		trackData[pos.i][pos.j] = ENEMY;
	}
}
