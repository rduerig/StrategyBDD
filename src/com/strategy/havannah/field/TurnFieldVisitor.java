package com.strategy.havannah.field;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.FieldVisitor;
import com.strategy.api.field.WhiteStone;
import com.strategy.util.StoneColor;

/**
 * Restricts a variable in the given {@link BDD} according to the {@link Field}
 * that is visited. If it's an empty field nothing happens.
 * 
 * @author Ralph Dürig
 */
public class TurnFieldVisitor implements FieldVisitor {

	private BDDFactory fac;
	private BDD win;
	private StoneColor color;

	public TurnFieldVisitor(BDD win, StoneColor color) {
		this.win = win;
		this.color = color;
		fac = win.getFactory();
	}

	public BDD getWin() {
		return win;
	}

	@Override
	public void visit(EmptyField field) {
		// do nothing
	}

	@Override
	public void visit(WhiteStone field) {
		if (StoneColor.WHITE.equals(color)) {
			win.restrictWith(fac.ithVar(field.getIndex()));
		} else {
			win.restrictWith(fac.ithVar(field.getIndex()).not());
		}
	}

	@Override
	public void visit(BlackStone field) {
		if (StoneColor.WHITE.equals(color)) {
			win.restrictWith(fac.ithVar(field.getIndex()).not());
		} else {
			win.restrictWith(fac.ithVar(field.getIndex()));
		}
	}

}
