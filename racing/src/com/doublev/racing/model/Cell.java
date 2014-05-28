package com.doublev.racing.model;

public class Cell {
	public int i;
	public int j;
	
	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cell) {
			Cell p = (Cell)obj;
			if ((p.i == this.i) && (p.j == this.j)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Cell [i=" + i + ", j=" + j + "]";
	}
	
}
