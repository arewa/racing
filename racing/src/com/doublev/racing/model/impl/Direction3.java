package com.doublev.racing.model.impl;

import com.doublev.racing.model.Direction;
import com.doublev.racing.model.Position;

public class Direction3 extends Direction {
	
	/**
	 * - - - - -
	 * - - - - -
	 * - - P - -
	 * - - x - -
	 * - - x - - 
	 */

	@Override
	protected Position computeNextPosition(Position currentPosition) {
		return new Position(currentPosition.i, currentPosition.j - 1);
	}
}