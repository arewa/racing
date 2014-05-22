package com.doublev.racing.model;

public class RaceTrack {
	
	public static short PLAYER = 2;
	public static short WALL = 1;
	public static short ENEMY = 3;
	public static short EMPTY = 0;
	
	public int[][] trackData;
	public Position trackSize;
	
	public void init(int i, int j) {
		this.trackSize = new Position(i, j);
		this.trackData = new int[i][j];
	}
	
	public void updatePlayerPosition(Position pos) {
		trackData[pos.i][pos.j] = PLAYER;
	}
}
