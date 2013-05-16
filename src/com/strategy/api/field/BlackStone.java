package com.strategy.api.field;

import com.strategy.api.logic.Position;

/**
 * Represents a non empty field that is covered by a black stone.
 * 
 * @author Ralph Dürig
 */
public class BlackStone implements StoneField {

	private final Position pos;
	private final Integer index;

	public BlackStone(Position pos, Integer index) {
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
		return "B";
	}

}
