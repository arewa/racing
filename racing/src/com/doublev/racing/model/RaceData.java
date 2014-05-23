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
	public Position playerPosition;
	
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
		this.trackData[pos.i][pos.j] = PLAYER;
		this.playerPosition = pos;
	}
	
	public void updateEnemyPosition(Position pos) {
		this.trackData[pos.i][pos.j] = ENEMY;
	}
	
	public void update() {
		
		int x1 = playerPosition.i + playerSpeed - 1;
		int x2 = playerPosition.i - playerSpeed + 1;
		int y1 = playerPosition.j + playerSpeed - 1;
		int y2 = playerPosition.j - playerSpeed + 1;

		for (int i = 0; i < 3; i ++) {
			int t1 = y1 + i;
			int t2 = y2 - i;
			int t3 = x1 + i;
			int t4 = x2 - i;			

			if (t1 < trackHeight) {
				trackData[playerPosition.i][t1] = NEXT_TURN;
				if (t3 < trackWidth) {
					trackData[t3][t1] = NEXT_TURN;
				}
				if (t4 >= 0) {
					trackData[t4][t1] = NEXT_TURN;
				}
			}

			if (t3 < trackWidth) {
				trackData[t3][playerPosition.j] = NEXT_TURN;
				if (t2 >= 0) {
					trackData[t3][t2] = NEXT_TURN;
				}
			}

			if (t4 >= 0) {
				trackData[t4][playerPosition.j] = NEXT_TURN;
				if (t2 >= 0) {
					trackData[t4][t2] = NEXT_TURN;
				}
			}

			if (t2 >= 0) {
				trackData[playerPosition.i][t2] = NEXT_TURN;
			}
		}
		
		trackData[playerPosition.i][playerPosition.j] = PLAYER;
	}
}
