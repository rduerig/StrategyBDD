package com.strategy.api.logic.evaluation;

import java.util.Map;

import com.strategy.api.logic.situation.Situation;

/**
 * Evaluates a {@link Situation}.
 * 
 * @author Ralph Dürig
 */
public interface Evaluation {

	Map<Integer, Integer> getEvaluatedFields();

}
