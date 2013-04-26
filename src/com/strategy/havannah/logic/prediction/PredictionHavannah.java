package com.strategy.havannah.logic.prediction;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.strategy.api.field.Field;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.evaluation.EvaluationHavannah;

/**
 * Uses a {@link Situation} and its {@link Evaluation} to predict where to set
 * the next stone.
 * 
 * @author Ralph Dürig
 */
public class PredictionHavannah implements Prediction {

	private Situation situation;

	public PredictionHavannah(Situation situation) {
		this.situation = situation;
	}

	/**
	 * Black has set on given field, updates the board according to that.
	 * 
	 * @param field
	 * @return index of field that was rated best
	 */
	@Override
	public int doNextTurn(Field lastSet) {
		situation.update(lastSet);
		// TODO make own turn
		Evaluation eval = new EvaluationHavannah(situation);
		Map<Integer, Double> rating = eval.getEvaluatedFields();
		Entry<Integer, Double> max = Collections.max(rating.entrySet(),
				new Comparator<Entry<Integer, Double>>() {

					@Override
					public int compare(Entry<Integer, Double> o1,
							Entry<Integer, Double> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				});

		Integer best = max.getKey();
		situation.update(best, 1);

		return best;
	}

}
