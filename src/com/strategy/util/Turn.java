package com.strategy.util;

/**
 * @author Ralph Dürig
 */
public class Turn {

	private RowConstant coord;
	private int coordNumber;
	private StoneColor color;

	public Turn(RowConstant coord, int coordNumber, StoneColor color) {
		if (null == color || StoneColor.EMPTY.equals(color)) {
			throw new IllegalArgumentException(
					"Color for turn cannot be null or EMPTY");
		}
		this.coord = coord;
		this.coordNumber = coordNumber;
		this.color = color;
	}

	/**
	 * @return the color
	 */
	public StoneColor getColor() {
		return color;
	}

	/**
	 * @return the coord
	 */
	public RowConstant getCoord() {
		return coord;
	}

	/**
	 * @return the coordNumber
	 */
	public int getCoordNumber() {
		return coordNumber;
	}

	@Override
	public String toString() {
		return color.name().substring(0, 1) + ":" + coord + coordNumber;
	}

}
