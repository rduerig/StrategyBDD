package com.strategy.api.logic.situation;

import net.sf.javabdd.BDD;

import com.strategy.api.field.Field;

/**
 * @author Ralph Dürig
 */
public interface Situation {

	BDD getWinningCondition();

	void update(Field field);

	void update(int fieldIndex, int type);

}
