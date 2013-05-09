package com.strategy.api.logic.situation;

import net.sf.javabdd.BDD;

public interface ConditionCalculator {

	/**
	 * Returns the calculated {@link BDD}.
	 * 
	 * @return the {@link BDD}
	 */
	BDD getBdd();

}
