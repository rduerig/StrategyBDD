package com.strategy.api.logic.evaluation;


/**
 * Evaluates a {@link Situation}.
 * 
 * @author Ralph Dürig
 */
public interface Evaluation {

	double getAverageRating();

	int getBestIndex();

	double[] getRating();

	void log();

}
