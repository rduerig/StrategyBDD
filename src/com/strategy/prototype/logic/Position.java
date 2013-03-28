package com.strategy.prototype.logic;

public class Position {
	private final int row;
	private final int col;
	private boolean seen;
	
	public static Position get(int row, int col){
		return new Position(row, col);
	}
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
		seen = false;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	// ************************************************************************
	
	public boolean isSeen() {
		return seen;
	}
	
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	public void setVisited(){
		setSeen(true);
	}
	
	public Position getUpper(){
		return new Position(row-1, col);
	}
	
	public Position getLower(){
		return new Position(row+1, col);
	}
	
	public Position getLeft(){
		return new Position(row, col-1);
	}
	
	public Position getRight(){
		return new Position(row, col+1);
	}
	
	public boolean isNeighbour(Position other){
		return other.equals(this.getUpper()) || other.equals(this.getLower()) || other.equals(this.getLeft()) || other.equals(this.getRight());
	}

	// ************************************************************************
	
	@Override
	public String toString() {
		return "("+row+", "+col+")";
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
		Position other = (Position) obj;
		if (col != other.col) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}
	
	

}
