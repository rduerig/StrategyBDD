package com.strategy.api.logic;

import java.util.List;

public interface Position {

	public abstract boolean isNeighbour(Position other);

	public abstract void setVisited();

	public abstract void setSeen(boolean seen);

	public abstract boolean isSeen();

	public abstract int getCol();

	public abstract int getRow();

	public abstract Position getNorth();

	public abstract Position getNorthWest();

	public abstract Position getEast();

	public abstract Position getNorthEast();

	public abstract Position getSouth();

	public abstract Position getSouthWest();

	public abstract Position getWest();

	public abstract Position getSouthEast();

	public abstract List<Position> getNeighbors();

}
