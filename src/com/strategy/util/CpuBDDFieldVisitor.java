/**
 * 
 */
package com.strategy.util;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.field.BDDFieldVisitor;
import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.WhiteStone;

/**
 * @author Ralph Dürig
 */
public class CpuBDDFieldVisitor implements BDDFieldVisitor {

	private final BDDFactory fac;
	private BDD result;

	public CpuBDDFieldVisitor(BDDFactory fac) {
		this.fac = fac;
	}

	@Override
	public BDD getBDD() {
		if (null == result) {
			throw new IllegalStateException(
					"No BDD can be returned, a field must be visited before.");
		}
		return result;
	}

	@Override
	public void visit(EmptyField field) {
		// System.out.print("BDD for field " + field.getIndex() + " ... ");
		result = fac.ithVar(field.getIndex());
		// System.out.println("done");
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