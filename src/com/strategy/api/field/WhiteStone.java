package com.strategy.api.field;

/**
 * Represents a non emtpy field that is covered by a white stone.
 * 
 * @author Ralph Dürig
 */
public class WhiteStone implements StoneField {

	private final Integer index;

	public WhiteStone(Integer index) {
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
		return "|W|";
	}

}
