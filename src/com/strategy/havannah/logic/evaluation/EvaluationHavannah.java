package com.strategy.havannah.logic.evaluation;

import java.util.Map;

import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.Situation;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private Situation situation;

	public EvaluationHavannah(Situation situation) {
		this.situation = situation;
	}

	@Override
	public Map<Integer, Integer> getEvaluatedFields() {
		// TODO getEvaluatedFields
		int[] set = situation.getWinningCondition().scanSet();

		return null;
	}

}
