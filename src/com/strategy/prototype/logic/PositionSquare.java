package com.strategy.prototype.logic;

import com.strategy.api.logic.Position;

/**
 * Represents a position on a square board and provides the according
 * neighborhood calculations.
 * 
 * @author Ralph Dürig
 */
public class PositionSquare implements Position {
	private final int row;
	private final int col;
	private boolean seen;

	public static Position get(int row, int col) {
		return new PositionSquare(row, col);
	}

	public PositionSquare(int row, int col) {
		this.row = row;
		this.col = col;
		seen = false;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getCol() {
		return col;
	}

	@Override
	public boolean isSeen() {
		return seen;
	}

	@Override
	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	@Override
	public void setVisited() {
		setSeen(true);
	}

	@Override
	public boolean isNeighbour(Position other) {
		return other.equals(this.getUpper()) || other.equals(this.getLower())
				|| other.equals(this.getLeft())
				|| other.equals(this.getRight())
				|| other.equals(this);
	}

	// ************************************************************************

	public Position getUpper() {
		return new PositionSquare(row - 1, col);
	}

	public Position getLower() {
		return new PositionSquare(row + 1, col);
	}

	public Position getLeft() {
		return new PositionSquare(row, col - 1);
	}

	public Position getRight() {
		return new PositionSquare(row, col + 1);
	}

	// ************************************************************************

	@Override
	public String toString() {
		return "(" + row + ", " + col + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PositionSquare other = (PositionSquare) obj;
		if (col != other.col) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}

}
