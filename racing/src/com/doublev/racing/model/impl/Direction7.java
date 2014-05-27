package com.doublev.racing.model.impl;

import com.doublev.racing.model.Direction;
import com.doublev.racing.model.Position;

public class Direction7 extends Direction {
	
	/**
	 * - - - - -
	 * - - - - -
	 * - - P - -
	 * - x - - -
	 * x - - - - 
	 */

	@Override
	protected Position computeNextPosition(Position currentPosition) {
		return new Position(currentPosition.i - 1, currentPosition.j - 1);
	}
}
