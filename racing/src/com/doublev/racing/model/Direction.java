package com.doublev.racing.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Direction {

	private RaceData raceData;
	private int maxHops;

	protected abstract Cell computeNextPosition(Cell currentPosition);
	
	public void updateRaceData(RaceData raceData, int maxHops) {
		this.raceData = raceData;
		this.maxHops = maxHops;
	}

	public List<Cell> computeAvailableForTurn(Cell startPosition) {
		List<Cell> avaiableTurns = new ArrayList<Cell>();
		
		if (maxHops == 0) {
			return avaiableTurns;
		}

		Cell nextPosition = computeNextPosition(startPosition);

		if ((nextPosition.i < 0) || (nextPosition.i >= raceData.trackWidth)) {
			return avaiableTurns;
		}

		if ((nextPosition.j < 0) || (nextPosition.j >= raceData.trackHeight)) {
			return avaiableTurns;
		}
		
		if (nextPosition.equals(raceData.playerPosition)) {
			return avaiableTurns;
		}
		
		if (nextPosition.equals(raceData.opponentPosition)) {
			return avaiableTurns;
		}
		
		for (Cell w : raceData.walls) {
			if (nextPosition.equals(w)) {
				return avaiableTurns;
			}
		}

		if (maxHops <= 3) {
			if (!avaiableTurns.contains(nextPosition)) {
				avaiableTurns.add(nextPosition);
			}
		}

		maxHops --;
		avaiableTurns.addAll(computeAvailableForTurn(nextPosition));
		
		return avaiableTurns;
	}
}