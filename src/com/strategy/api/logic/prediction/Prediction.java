package com.strategy.api.logic.prediction;

/**
 * @author Ralph Dürig
 */
public interface Prediction {

	/**
	 * Player has set on given field, updates the board according to that and
	 * make own cpu turn.
	 * 
	 * @param fieldIndex
	 *            index of the field
	 * @return the index of the field the cpu set on
	 */
	int doNextTurn(int fieldIndex);

	/**
	 * 
	 * @return true if the computer has won, false otherwise
	 */
	boolean isWinCpu();

	/**
	 * 
	 * @return true if the player has won, false otherwise
	 */
	boolean isWinPlayer();

}