package com.strategy.util.operation;

import net.sf.javabdd.BDD;

/**
 * @author Ralph Dürig
 */
public class Or implements Op {

	@Override
	public BDD apply(BDD x, BDD y) {
		return x.orWith(y);
	}

}
