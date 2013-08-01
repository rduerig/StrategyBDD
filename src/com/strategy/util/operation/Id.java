package com.strategy.util.operation;

import net.sf.javabdd.BDD;

public class Id implements Op {

	@Override
	/**
	 * just copies the x bdd
	 */
	public BDD apply(BDD x, BDD y) {
		return x.id();
	}

}
