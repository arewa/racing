package com.doublev.racing.model.impl;

import com.doublev.racing.model.Direction;
import com.doublev.racing.model.Position;

public class Direction5 extends Direction {
	
	/**
	 * - - - - x
	 * - - - x -
	 * - - P - -
	 * - - - - -
	 * - - - - - 
	 */

	@Override
	protected Position computeNextPosition(Position currentPosition) {
		return new Position(currentPosition.i + 1, currentPosition.j + 1);
	}
}
