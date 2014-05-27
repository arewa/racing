package com.doublev.racing.model.impl;

import com.doublev.racing.model.Direction;
import com.doublev.racing.model.Cell;

public class Direction1 extends Direction {
	
	/**
	 * - - x - -
	 * - - x - -
	 * - - P - -
	 * - - - - -
	 * - - - - - 
	 */

	@Override
	protected Cell computeNextPosition(Cell currentPosition) {
		return new Cell(currentPosition.i, currentPosition.j + 1);
	}
}
