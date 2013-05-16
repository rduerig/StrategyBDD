package com.strategy.api.field;

import com.strategy.api.logic.Position;

/**
 * Represents a non emtpy field that is covered by a white stone.
 * 
 * @author Ralph Dürig
 */
public class WhiteStone implements StoneField {

	private final Position pos;
	private final Integer index;

	public WhiteStone(Position pos, Integer index) {
		this.pos = pos;
		this.index = index;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public Integer getIndex() {
		return index;
	}

	@Override
	public void accept(FieldVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "W";
	}

}
