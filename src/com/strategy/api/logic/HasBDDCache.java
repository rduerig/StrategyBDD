package com.strategy.api.logic;

import net.sf.javabdd.BDD;

/**
 * Let a class implement methods to access a {@link BDDCache}.
 * 
 * @author Ralph Dürig
 */
public interface HasBDDCache {

	/**
	 * Tries to return a bdd from cache for the given {@link Position}s. If no
	 * such bdd is found in the cache, the given bdd is stored.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @param defaultValue
	 *            a bdd to store if nothing is found
	 * @return either a bdd from the cache or the given one
	 */
	BDD findOrStore(Position p, Position q, BDD defaultValue);

}
