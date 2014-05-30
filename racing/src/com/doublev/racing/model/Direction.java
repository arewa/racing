package com.doublev.racing.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Direction {

	private World world;
	private int maxHops;

	protected abstract Cell computeNextPosition(Cell currentPosition);
	
	public void updateRaceData(World world, int maxHops) {
		this.world = world;
		this.maxHops = maxHops;
	}

	public List<Cell> computeAvailableForTurn(Cell startPosition) {
		List<Cell> avaiableTurns = new ArrayList<Cell>();
		
		if (maxHops == 0) {
			return avaiableTurns;
		}

		Cell nextPosition = computeNextPosition(startPosition);
		
		if (nextPosition.equals(world.playerPosition)) {
			return avaiableTurns;
		}
		
		if (nextPosition.equals(world.opponentPosition)) {
			return avaiableTurns;
		}
		
		for (Cell w : world.walls) {
			if (nextPosition.equals(w)) {
				return avaiableTurns;
			}
		}
		
		if ((nextPosition.i < 0) || (nextPosition.i >= world.trackWidth)) {
			return avaiableTurns;
		}

		if ((nextPosition.j < 0)) {
			return avaiableTurns;
		}
		
		if (nextPosition.j >= world.trackHeight) {
			nextPosition.j = world.trackHeight - 1;
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