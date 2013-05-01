package com.strategy.util;

/**
 * @author Ralph Dürig
 */
public enum StoneColor {

	WHITE(1), BLACK(2), EMPTY(0);

	private final int primitive;

	private StoneColor(int primitive) {
		this.primitive = primitive;
	}

	public int getPrimitive() {
		return primitive;
	}

	public static StoneColor parse(int primitive) {
		for (StoneColor color : values()) {
			if (color.getPrimitive() == primitive) {
				return color;
			}
		}

		return EMPTY;
	}
}