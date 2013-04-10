package com.strategy.prototype.field;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.FieldVisitor;
import com.strategy.api.field.WhiteStone;

public class BDDFieldVisitor implements FieldVisitor {

	private final BDDFactory fac;
	private BDD result;

	public BDDFieldVisitor(BDDFactory fac) {
		this.fac = fac;
	}

	public BDD getBdd() {
		return result;
	}

	@Override
	public void visit(EmptyField field) {
		result = fac.ithVar(field.getIndex());
	}

	@Override
	public void visit(WhiteStone field) {
		result = fac.one();
	}

	@Override
	public void visit(BlackStone field) {
		result = fac.zero();
	}

}
