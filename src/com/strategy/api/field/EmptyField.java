package com.strategy.api.field;

import com.strategy.api.logic.Position;

/**
 * Represents an empty field.
 * 
 * @author Ralph Dürig
 */
public class EmptyField implements Field {

	private final Position pos;
	private final Integer index;

	public EmptyField(Position pos, Integer index) {
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
		return ".";
	}

}
