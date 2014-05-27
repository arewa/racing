package com.doublev.racing.model.impl;

import com.doublev.racing.model.Direction;
import com.doublev.racing.model.Cell;

public class Direction3 extends Direction {
	
	/**
	 * - - - - -
	 * - - - - -
	 * - - P - -
	 * - - x - -
	 * - - x - - 
	 */

	@Override
	protected Cell computeNextPosition(Cell currentPosition) {
		return new Cell(currentPosition.i, currentPosition.j - 1);
	}
}