package com.strategy.util.operation;

import net.sf.javabdd.BDD;

public class Paths implements UnaryOp {

	@Override
	public double apply(BDD x) {
		return x.pathCount();
	}

}
