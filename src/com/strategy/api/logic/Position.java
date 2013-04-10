package com.strategy.api.logic;

public interface Position {

	public abstract boolean isNeighbour(Position other);

	public abstract void setVisited();

	public abstract void setSeen(boolean seen);

	public abstract boolean isSeen();

	public abstract int getCol();

	public abstract int getRow();

}
