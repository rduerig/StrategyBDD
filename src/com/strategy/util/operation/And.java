package com.strategy.util.operation;

import net.sf.javabdd.BDD;

/**
 * @author Ralph Dürig
 */
public class And implements Op {

	@Override
	public BDD apply(BDD x, BDD y) {
		return x.andWith(y);
	}

}
