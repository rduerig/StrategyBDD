package com.strategy.api.logic.prediction;

import com.strategy.api.field.Field;

/**
 * @author Ralph Dürig
 */
public interface Prediction {

	/**
	 * Black has set on given field, updates the board according to that.
	 * 
	 * @param field
	 * @return
	 */
	int doNextTurn(Field lastSet);

}