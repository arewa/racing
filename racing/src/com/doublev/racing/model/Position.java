package com.doublev.racing.model;

public class Position {
	public int i;
	public int j;
	
	public Position(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			Position p = (Position)obj;
			if ((p.i == this.i) && (p.j == this.j)) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
