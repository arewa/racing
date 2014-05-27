package com.doublev.racing.model;

import java.util.ArrayList;
import java.util.List;

import com.doublev.racing.model.impl.Direction1;
import com.doublev.racing.model.impl.Direction2;
import com.doublev.racing.model.impl.Direction3;
import com.doublev.racing.model.impl.Direction4;
import com.doublev.racing.model.impl.Direction5;
import com.doublev.racing.model.impl.Direction6;
import com.doublev.racing.model.impl.Direction7;
import com.doublev.racing.model.impl.Direction8;

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
	public List<Position> availableTurns = new ArrayList<Position>();
	public List<Direction> directions = new ArrayList<Direction>();
	
	public void init(int i, int j) {
		this.trackWidth = i;
		this.trackHeight = j;
		this.trackData = new int[i][j];
		
		// Init directions
		directions.add(new Direction1());
		directions.add(new Direction2());
		directions.add(new Direction3());
		directions.add(new Direction4());
		directions.add(new Direction5());
		directions.add(new Direction6());
		directions.add(new Direction7());
		directions.add(new Direction8());
		
		// Manually init walls
		this.trackData[0][0] = WALL;
		this.trackData[0][2] = WALL;
		this.trackData[0][5] = WALL;
		this.trackData[2][5] = WALL;
		this.trackData[1][0] = WALL;
		this.trackData[2][2] = WALL;
	}
	
	public void updatePlayerPosition(Position pos) {
		updatePlayerSpeed(pos);
		this.trackData[pos.i][pos.j] = PLAYER;
		this.playerPosition = pos;
	}
	
	public void updateEnemyPosition(Position pos) {
		this.trackData[pos.i][pos.j] = ENEMY;
	}
	
	public void update() {
		
		if (playerPosition == null) {
			return;
		}

		for (Direction d : directions) {
			d.updateRaceData(this);
			d.computeAvailableForTurn(playerPosition);
		}
		
		trackData[playerPosition.i][playerPosition.j] = PLAYER;
	}
	
	public void resetPlayerPosition() {
		for (int i = 0; i < trackWidth; i ++) {
			for (int j = 0; j < trackHeight; j ++) {
				if ((trackData[i][j] == PLAYER) || (trackData[i][j] == NEXT_TURN)) {
					trackData[i][j] = EMPTY;
				}
			}
		}
		
		availableTurns.clear();
	}
	
	public boolean isTurnAvaiable(Position turn) {
		
		for (Position p : availableTurns) {
			if (p.equals(turn)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void updatePlayerSpeed(Position turn) {
		
		if (playerPosition == null) {
			return;
		}
		
		if (playerPosition.j == turn.j) {
			playerSpeed = Math.abs(playerPosition.i - turn.i);
		} else if (playerPosition.i == turn.i) {
			playerSpeed = Math.abs(playerPosition.j - turn.j);
		} else {
			playerSpeed = Math.abs(playerPosition.j - turn.j);
		}
		
		if (playerSpeed == 0) {
			playerSpeed = 1;
		}
	}
	
	public void addAvaiableTurn(Position turn) {
		if (turn.equals(playerPosition)) {
			return;
		}
		trackData[turn.i][turn.j] = NEXT_TURN;
		availableTurns.add(turn);
	}
}
