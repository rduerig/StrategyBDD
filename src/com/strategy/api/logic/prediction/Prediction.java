package com.strategy.api.logic.prediction;

import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public interface Prediction {

	/**
	 * Black has set on given field, updates the board according to that.
	 * 
	 * @param fieldIndex
	 *            index of the field
	 * @param color
	 *            color of the set field
	 * @return
	 */
	int doNextTurn(int fieldIndex, StoneColor color);

	// /**
	// * frees memory from bdds
	// */
	// void cleanup();

}