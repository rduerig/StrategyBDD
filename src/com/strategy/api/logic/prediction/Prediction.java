package com.strategy.api.logic.prediction;

import com.strategy.api.HasDebugFlag;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public interface Prediction extends HasDebugFlag {

	/**
	 * Player has set on given field, updates the board according to that and
	 * make own cpu turn.
	 * 
	 * @param fieldIndex
	 *            index of the field
	 * @return the index of the field the cpu set on
	 */
	int answerTurn(int fieldIndex, StoneColor colorLastSet);

	/**
	 * Make cpu turn.
	 * 
	 * @return field index the cpu has set on, null if no decision was made due
	 *         to victory of one side
	 */
	Integer doTurn(StoneColor colorToUse);

	/**
	 * 
	 * @return true if the computer has won, false otherwise
	 */
	boolean isWinWhite();

	/**
	 * 
	 * @return true if the player has won, false otherwise
	 */
	boolean isWinBlack();

}