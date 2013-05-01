package com.strategy.util;

import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.FieldVisitor;
import com.strategy.api.field.WhiteStone;

/**
 * @author Ralph Dürig
 */
public class ColorFieldVisitor implements FieldVisitor {

	private StoneColor color;

	public StoneColor getColor() {
		return color;
	}

	@Override
	public void visit(EmptyField field) {
		color = StoneColor.EMPTY;
	}

	@Override
	public void visit(WhiteStone field) {
		color = StoneColor.WHITE;
	}

	@Override
	public void visit(BlackStone field) {
		color = StoneColor.BLACK;
	}

}
