package com.strategy.api.logic.situation;

import net.sf.javabdd.BDD;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public interface Situation {

	BDD getWinningCondition();

	Board getBoard();

	StoneColor getStoneColor();

	void update(Field field);

	void update(int fieldIndex, int type);

}
