package com.strategy.util.operation;

import net.sf.javabdd.BDD;

/**
 * @author Ralph Dürig
 */
public interface Op {

	public static Op AND = new And();
	public static Op OR = new Or();
	public static Op RESTRICT = new Restrict();
	public static Op ID = new Id();

	/**
	 * Applies the operation and returns the result. <b>Important: The y BDD is
	 * consumed.</b>
	 * 
	 * @param x
	 * @param y
	 * @return the resulting BDD after the operation
	 */
	BDD apply(BDD x, BDD y);

}
