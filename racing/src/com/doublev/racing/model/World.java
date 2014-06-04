package com.doublev.racing.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class World {
	
	public static final int STATE_GAME_OVER = 1;
	
	public int trackWidth;
	public int trackHeight;
	
	public int playerSpeed = 1;
	public int opponentSpeed = 1;
	
	public Cell playerPosition;
	public Cell opponentPosition;
	
	public List<Cell> availableTurns = new ArrayList<Cell>();
	public List<Cell> opponentAvailableTurns = new ArrayList<Cell>();
	
	public List<Cell> walls = new ArrayList<Cell>();
	public List<Direction> directions = new ArrayList<Direction>();
	
	private List<WorldObserver> observers = new ArrayList<WorldObserver>();
	
	public void init(String trackFile) {
		// Init directions
		directions.add(new Direction1());
		directions.add(new Direction2());
		directions.add(new Direction3());
		directions.add(new Direction4());
		directions.add(new Direction5());
		directions.add(new Direction6());
		directions.add(new Direction7());
		directions.add(new Direction8());
		
		loadTrack(trackFile);
	}
	
	public void updatePlayerPosition(Cell pos) {
		this.playerPosition = pos;
	}
	
	public void updateOpponentPosition(Cell pos) {
		updateOpponentSpeed(pos);
		this.opponentPosition = pos;
	}
	
	public void update(Cell turn) {
		
		if (playerPosition == null) {
			return;
		}
		
		boolean isTurnCoincideWithOpponent = isTurnCoincideWithOpponent(turn);
		boolean isTurnCoincideWithWall = isTurnCoincideWithWall(turn);
		
		if (isTurnCoincideWithOpponent || isTurnCoincideWithWall) {
			availableTurns.remove(turn);
			resetPlayerSpeed();
		} else if (isTurnCoincideWithOpponent) {
			resetOpponentSpeed();
		} else {
			updatePlayerSpeed(turn);
		}
		
		updatePlayerPosition(turn);
		
		computeOpponentTurn();
		
		if ((playerPosition.j >= (this.trackHeight - 1)) || (opponentPosition.j >= (this.trackHeight - 1))) {
			updateWorldState(World.STATE_GAME_OVER);
			return;
		}
		
		resetAvailableTurns();

		for (Direction d : directions) {
			d.updateRaceData(this, playerSpeed + 1);
			availableTurns.addAll(d.computeAvailableForTurn(playerPosition));
			
			d.updateRaceData(this, opponentSpeed + 1);
			opponentAvailableTurns.addAll(d.computeAvailableForTurn(opponentPosition));
		}
	}
	
	public void resetAvailableTurns() {
		availableTurns.clear();
		opponentAvailableTurns.clear();
	}
	
	public boolean isTurnAvaiable(Cell turn) {
		
		for (Cell p : availableTurns) {
			if (p.equals(turn)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isTurnCoincideWithOpponent(Cell turn) {
		return turn.equals(opponentPosition);
	}
	
	public boolean isTurnCoincideWithWall(Cell turn) {
		for (Cell w : walls) {
			if (w.equals(turn)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void resetPlayerSpeed() {
		this.playerSpeed = 1;
	}
	
	public void resetOpponentSpeed() {
		this.opponentSpeed = 1;
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
	
	private void updateOpponentSpeed(Cell turn) {
		
		if (opponentPosition == null) {
			return;
		}
		
		if (opponentPosition.j == turn.j) {
			opponentSpeed = Math.abs(opponentPosition.i - turn.i);
		} else if (playerPosition.i == turn.i) {
			opponentSpeed = Math.abs(opponentPosition.j - turn.j);
		} else {
			opponentSpeed = Math.abs(opponentPosition.j - turn.j);
		}
		
		if (opponentSpeed == 0) {
			opponentSpeed = 1;
		}
	}
	
	private void computeOpponentTurn() {
		if (opponentAvailableTurns.size() == 0) {
			return;
		}
		
		int minWallsAround = 99;
		int maxJ = 0;
		Cell newPosition = new Cell(0, 0);
		for (Cell c : opponentAvailableTurns) {
			for (Cell w : walls) {
				int wallsAround = 0;
				if ((w.i == c.i) && (w.j == (c.j + 1))) {
					wallsAround ++;
				} 
				if ((w.i == (c.i + 1)) && (w.j == c.j)) {
					wallsAround ++;
				} 
				if ((w.i == c.i) && (w.j == (c.j - 1))) {
					wallsAround ++;
				}
				if ((w.i == (c.i - 1)) && (w.j == c.j)) {
					wallsAround ++;
				}
				if ((w.i == (c.i + 1)) && (w.j == (c.j + 1))) {
					wallsAround ++;
				}
				if ((w.i == (c.i + 1)) && (w.j == (c.j - 1))) {
					wallsAround ++;
				}
				if ((w.i == (c.i - 1)) && (w.j == (c.j - 1))) {
					wallsAround ++;
				}
				if ((w.i == (c.i - 1)) && (w.j == (c.j + 1))) {
					wallsAround ++;
				}
				
				if ((wallsAround <= minWallsAround) && (c.j >= maxJ) 
						&& ((c.i != playerPosition.i) && (c.j != playerPosition.j))) {
					minWallsAround = wallsAround;
					maxJ = c.j;
					newPosition = c;
				}
			}
		}
		
		updateOpponentPosition(newPosition);
	}
	
	private void loadTrack(String trackFile) {
		FileHandle fh = Gdx.files.internal(trackFile);
		BufferedReader r = new BufferedReader(fh.reader());
		String strLine;
		String[] line;
		int col, row = 0;
		
		try {
			// Init track size (first line in file)
			strLine = r.readLine();
			line = strLine.split(" ");
			
			trackWidth = new Integer(line[0]).intValue();
			trackHeight = new Integer(line[1]).intValue();
		
			while ((strLine = r.readLine()) != null)   {
				line = strLine.split(" ");
				col = 0;
				for (String s : line) {
					int ti = col;
					int tj = trackHeight - row - 1;
					if ("-".equals(s) || "|".equals(s)) {
						walls.add(new Cell(ti, tj));
					} else if ("*".equals(s)) {
						playerPosition = new Cell(ti, tj);
					} else if ("+".equals(s)) {
						opponentPosition = new Cell(ti, tj);
					}
					col ++;
				}
				row ++;
			}
		} catch (IOException e) {}
	}
	
	private void updateWorldState(int state) {
		for (WorldObserver wo : observers) {
			wo.worldChanged(state);
		}
	}
	
	public void addObserver(WorldObserver observer) {
		this.observers.add(observer);
	}
}
