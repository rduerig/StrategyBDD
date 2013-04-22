package com.strategy.havannah.logic.prediction;

import java.util.List;

import com.google.common.collect.Iterables;
import com.strategy.api.field.Field;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.Situation;

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
	 * @return
	 */
	@Override
	public int doNextTurn(Field lastSet) {
		situation.update(lastSet);

		// TODO make own turn
		// System.out.println(win.satOne().toString());
		List<byte[]> allsat = situation.getWinningCondition().allsat();
		int index = -1;
		byte[] sat = Iterables.getFirst(allsat, new byte[0]);
		if (null != sat) {
			// System.out.println(Arrays.toString(sat));
			for (int i = 0; i < sat.length; i++) {
				if (sat[i] == 0x0001) {
					index = i;
					break;
				}
			}
		}

		if (index > 0) {
			situation.update(index, 1);
		} else {
			System.out.println("computer passes");
		}

		return index;
	}

	@Override
	public int getPossiblePaths() {
		return situation.getWinningCondition().allsat().size();
	}

}
