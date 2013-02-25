package com.strategy.prototype.field;

/**
 * Represents a non empty field that is covered by a black stone.
 * 
 * @author Ralph Dürig
 */
public class BlackStone implements StoneField {

	@Override
	public String toString() {
		return "|B|";
	}

}
