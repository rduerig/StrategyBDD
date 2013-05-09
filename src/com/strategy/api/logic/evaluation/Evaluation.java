package com.strategy.api.logic.evaluation;

import com.strategy.api.logic.situation.Situation;

/**
 * Evaluates a {@link Situation}.
 * 
 * @author Ralph Dürig
 */
public interface Evaluation {

	double getAverageRating();

	int getBestIndex();

	double[] getRating();

}
