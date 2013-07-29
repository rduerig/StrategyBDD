package com.strategy.util.operation;

import net.sf.javabdd.BDD;

/**
 * @author Ralph Dürig
 */
public interface UnaryOp {

	public static UnaryOp SATS = new Sats();
	public static UnaryOp PATHS = new Paths();
	public static UnaryOp NODES = new Nodes();

	/**
	 * Applies the operation and returns the result.
	 * 
	 * @param x
	 * @return the resulting value after the operation
	 */
	double apply(BDD x);

}
