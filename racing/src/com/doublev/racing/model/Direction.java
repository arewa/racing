package com.doublev.racing.model;

public abstract class Direction {

	private RaceData raceData;
	private int maxHops;

	protected abstract Position computeNextPosition(Position currentPosition);
	
	public void updateRaceData(RaceData raceData) {
		this.raceData = raceData;
		this.maxHops = raceData.playerSpeed + 1;
	}

	public void computeAvailableForTurn(Position startPosition) {
		if (maxHops == 0) {
			return;
		}

		Position nextPosition = computeNextPosition(startPosition);

		if ((nextPosition.i < 0) || (nextPosition.i >= raceData.trackWidth)) {
			return;
		}

		if ((nextPosition.j < 0) || (nextPosition.j >= raceData.trackHeight)) {
			return;
		}

		if ((raceData.trackData[nextPosition.i][nextPosition.j] == RaceData.WALL) || 
				(raceData.trackData[nextPosition.i][nextPosition.j] == RaceData.PLAYER)) {
			return;
		}

		if (maxHops <= 3) {
			raceData.addAvaiableTurn(nextPosition);
		}

		maxHops --;
		computeAvailableForTurn(nextPosition);
	}
}