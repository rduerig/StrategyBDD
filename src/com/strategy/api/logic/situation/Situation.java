package com.strategy.api.logic.situation;

import net.sf.javabdd.BDD;

import com.strategy.api.board.Board;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public interface Situation {

	BDD getWinningCondition();

	BDD getWinningConditionFork();

	BDD getWinningConditionBridge();

	BDD getWinningConditionRing();

	Board getBoard();

	StoneColor getStoneColor();

	void update(int fieldIndex, StoneColor color);

}
