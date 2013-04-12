package com.strategy.api.logic;

import net.sf.javabdd.BDD;

public interface BoardAnalyzer {

	/**
	 * Returns the bdd representation of the reachability problem of the given
	 * positions.
	 */
	public BDD getPath(Position p, Position q);

	/**
	 * Releases all used BDDs and frees memory.
	 */
	public void done();

}
