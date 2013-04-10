package com.strategy.api.field;

/**
 * Represents an empty field.
 * 
 * @author Ralph Dürig
 */
public class EmptyField implements Field {

	private final Integer index;

	public EmptyField(Integer index) {
		this.index = index;
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
		return "| |";
	}

}
