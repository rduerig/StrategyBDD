package com.strategy.havannah.field;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.FieldVisitor;
import com.strategy.api.field.WhiteStone;

/**
 * Restricts a variable in the given {@link BDD} according to the {@link Field}
 * that is visited. If it's an empty field nothing happens.
 * 
 * @author Ralph Dürig
 */
public class TurnFieldVisitor implements FieldVisitor {

	private BDDFactory fac;
	private BDD win;

	public TurnFieldVisitor(BDD win) {
		this.win = win;
		fac = win.getFactory();
	}

	public BDD getWin() {
		return win.id();
	}

	@Override
	public void visit(EmptyField field) {
		// do nothing
	}

	@Override
	public void visit(WhiteStone field) {
		win = win.restrict(fac.ithVar(field.getIndex()).id()).id();
	}

	@Override
	public void visit(BlackStone field) {
		win = win.restrict(fac.ithVar(field.getIndex()).id().not()).id();
	}

}
