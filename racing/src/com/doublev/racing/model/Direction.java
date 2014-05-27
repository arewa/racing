package com.doublev.racing.model;

public abstract class Direction {

	private RaceData raceData;
	private int maxHops;

	protected abstract Cell computeNextPosition(Cell currentPosition);
	
	public void updateRaceData(RaceData raceData) {
		this.raceData = raceData;
		this.maxHops = raceData.playerSpeed + 1;
	}

	public void computeAvailableForTurn(Cell startPosition) {
		if (maxHops == 0) {
			return;
		}

		Cell nextPosition = computeNextPosition(startPosition);

		if ((nextPosition.i < 0) || (nextPosition.i >= raceData.trackWidth)) {
			return;
		}

		if ((nextPosition.j < 0) || (nextPosition.j >= raceData.trackHeight)) {
			return;
		}
		
		if (nextPosition.equals(raceData.playerPosition)) {
			return;
		}
		
		for (Cell w : raceData.walls) {
			if (nextPosition.equals(w)) {
				return;
			}
		}

		if (maxHops <= 3) {
			raceData.addAvaiableTurn(nextPosition);
		}

		maxHops --;
		computeAvailableForTurn(nextPosition);
	}
}