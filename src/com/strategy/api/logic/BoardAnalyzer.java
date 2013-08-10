package com.strategy.api.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.util.StoneColor;

public interface BoardAnalyzer {

	/**
	 * Returns the bdd representation of the reachability problem of the given
	 * positions.
	 */
	public BDD getPath(Position p, Position q, StoneColor color);

	/**
	 * Calls all available logging methods.
	 */
	public void log();

	/**
	 * Releases all used BDDs and frees memory.
	 */
	public void done();

	public BDDFactory getFactory();

}
