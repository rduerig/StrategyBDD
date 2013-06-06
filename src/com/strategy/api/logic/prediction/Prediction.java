package com.strategy.api.logic.prediction;

import java.util.List;

import com.strategy.api.HasDebugFlag;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.util.StoneColor;
import com.strategy.util.Turn;

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
	 * @return the index of the field the cpu set on or null if someone has won
	 */
	Integer answerTurn(int fieldIndex, StoneColor colorLastSet);

	/**
	 * Make cpu turn.
	 * 
	 * @return field index the cpu has set on, null if no decision was made due
	 *         to victory of one side
	 */
	Integer doCalculatedTurn(StoneColor colorToUse);

	/**
	 * Updates all conditions to the turn made on the given field and with the
	 * given color. Does not evaluate or predict anything.
	 * 
	 * @param fieldIndex
	 *            index of the field
	 * @param colorLastSet
	 *            the color for which the turn should be recorded
	 */
	void doManualTurn(int fieldIndex, StoneColor colorLastSet);

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

	/**
	 * Returns the index the last turn happened on.
	 * 
	 * @return the last turn's field index
	 */
	Integer getLastTurn();

	/**
	 * 
	 * @return {@link Evaluation} for white's situation
	 */
	Evaluation getEvaluationWhite();

	/**
	 * 
	 * @return {@link Evaluation} for black's situation
	 */
	Evaluation getEvaluationBlack();

	/**
	 * 
	 * @return a list of {@link Turn}s that were played so far
	 */
	List<Turn> getTurnsSoFar();

}