package com.doublev.racing.model;

public class RaceData {
	
	public static short PLAYER = 2;
	public static short WALL = 1;
	public static short ENEMY = 3;
	public static short EMPTY = 0;
	public static short NEXT_TURN = 100;
	
	public int[][] trackData;
	public int trackWidth;
	public int trackHeight;
	
	public int playerSpeed = 1;
	
	public void init(int i, int j) {
		this.trackWidth = i;
		this.trackHeight = j;
		this.trackData = new int[i][j];
		
		// Manually init walls
//		this.trackData[0][0] = WALL;
//		this.trackData[0][2] = WALL;
//		this.trackData[0][5] = WALL;
//		this.trackData[2][5] = WALL;
//		this.trackData[1][0] = WALL;
//		this.trackData[2][2] = WALL;
	}
	
	public void updatePlayerPosition(Position pos) {
		trackData[pos.i][pos.j] = PLAYER;
	}
	
	public void updateEnemyPosition(Position pos) {
		trackData[pos.i][pos.j] = ENEMY;
	}
	
	public void update() {
		for (int i = 0; i < trackWidth; i ++) {
			for (int j = 0; j < trackHeight; j ++) {
				if (trackData[i][j] == RaceData.PLAYER) {
					for (int k = i - playerSpeed; k <= i + playerSpeed; k ++) {
						for (int n = j - playerSpeed; n <= j + playerSpeed; n ++) {
							if ((k == i) && (n == j)) {
								continue;
							}
							
							if ((k + n) % 2 == 0 || (k == i) || (n == j) || (k == n)) {
								trackData[k][n] = NEXT_TURN;
							}
							
//							if ((k == i) || (n == j) || (k == n)) {
//								trackData[k][n] = NEXT_TURN;
//							}
//							
//							if (k == i + playerSpeed) {
//								trackData[k][n] = NEXT_TURN;
//							}
						}
					}
				}
			}
		}
	}
}
