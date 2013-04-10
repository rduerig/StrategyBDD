package com.strategy.api.field;

/**
 * Represents a non empty field that is covered by a black stone.
 * 
 * @author Ralph Dürig
 */
public class BlackStone implements StoneField {

	private final Integer index;

	public BlackStone(Integer index) {
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
		return "|B|";
	}

}
