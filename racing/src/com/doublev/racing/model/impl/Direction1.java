package com.doublev.racing.model.impl;

import com.doublev.racing.model.Direction;
import com.doublev.racing.model.Position;

public class Direction1 extends Direction {
	
	/**
	 * - - x - -
	 * - - x - -
	 * - - P - -
	 * - - - - -
	 * - - - - - 
	 */

	@Override
	protected Position computeNextPosition(Position currentPosition) {
		return new Position(currentPosition.i, currentPosition.j + 1);
	}
}
