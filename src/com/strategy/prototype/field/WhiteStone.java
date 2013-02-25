package com.strategy.prototype.field;

/**
 * Represents a non emtpy field that is covered by a white stone.
 * 
 * @author Ralph Dürig
 */
public class WhiteStone implements StoneField {

	@Override
	public String toString() {
		return "|W|";
	}

}
