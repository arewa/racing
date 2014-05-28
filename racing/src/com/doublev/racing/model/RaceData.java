package com.doublev.racing.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.doublev.racing.constants.Constants;
import com.doublev.racing.model.impl.Direction1;
import com.doublev.racing.model.impl.Direction2;
import com.doublev.racing.model.impl.Direction3;
import com.doublev.racing.model.impl.Direction4;
import com.doublev.racing.model.impl.Direction5;
import com.doublev.racing.model.impl.Direction6;
import com.doublev.racing.model.impl.Direction7;
import com.doublev.racing.model.impl.Direction8;

public class RaceData {
	public int trackWidth;
	public int trackHeight;
	
	public int playerSpeed = 1;
	public Cell playerPosition;
	public Cell enemyPosition;
	public List<Cell> availableTurns = new ArrayList<Cell>();
	public List<Cell> walls = new ArrayList<Cell>();
	public List<Direction> directions = new ArrayList<Direction>();
	
	public void init(int i, int j) {
		this.trackWidth = i;
		this.trackHeight = j;
		
		// Init directions
		directions.add(new Direction1());
		directions.add(new Direction2());
		directions.add(new Direction3());
		directions.add(new Direction4());
		directions.add(new Direction5());
		directions.add(new Direction6());
		directions.add(new Direction7());
		directions.add(new Direction8());
	}
	
	public void updatePlayerPosition(Cell pos) {
		updatePlayerSpeed(pos);
		this.playerPosition = pos;
	}
	
	public void updateEnemyPosition(Cell pos) {
		this.enemyPosition = pos;
	}
	
	public void update() {
		
		if (playerPosition == null) {
			return;
		}

		for (Direction d : directions) {
			d.updateRaceData(this);
			d.computeAvailableForTurn(playerPosition);
		}
	}
	
	public void resetPlayerPosition() {
		availableTurns.clear();
	}
	
	public boolean isTurnAvaiable(Cell turn) {
		
		for (Cell p : availableTurns) {
			if (p.equals(turn)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void updatePlayerSpeed(Cell turn) {
		
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
	
	public void addAvaiableTurn(Cell turn) {
		if (turn.equals(playerPosition)) {
			return;
		}
		
		availableTurns.add(turn);
	}
	
	public void loadTrack(String trackFile) {
		FileHandle fh = Gdx.files.internal(trackFile);
		BufferedReader r = new BufferedReader(fh.reader());
		String strLine;
		int col, row = 0;
		try {
			while ((strLine = r.readLine()) != null)   {
				String[] line = strLine.split(" ");
				col = 0;
				for (String s : line) {
					int ti = col;
					int tj = trackHeight - row - 1;
					if ("-".equals(s) || "|".equals(s)) {
						walls.add(new Cell(ti, tj));
					} else if ("*".equals(s)) {
						playerPosition = new Cell(ti, tj);
					} else if ("+".equals(s)) {
						enemyPosition = new Cell(ti, tj);
					}
					col ++;
				}
				row ++;
			}
		} catch (IOException e) {}
	}
}
