package com.strategy.util.operation;

import net.sf.javabdd.BDD;

/**
 * @author Ralph Dürig
 */
public class Restrict implements Op {

	@Override
	public BDD apply(BDD x, BDD y) {
		return x.restrictWith(y);
	}

}
