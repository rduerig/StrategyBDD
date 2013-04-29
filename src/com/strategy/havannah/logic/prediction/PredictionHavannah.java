package com.strategy.havannah.logic.prediction;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.evaluation.EvaluationHavannah;
import com.strategy.havannah.logic.situation.SituationHavannah;
import com.strategy.util.StoneColor;

/**
 * Uses a {@link Situation} and its {@link Evaluation} to predict where to set
 * the next stone.
 * 
 * @author Ralph Dürig
 */
public class PredictionHavannah implements Prediction {

	private Situation situationWhite;
	private Situation situationBlack;

	public PredictionHavannah(Board board) {
		init(board);
	}

	/**
	 * Black has set on given field, updates the board according to that.
	 * 
	 * @param field
	 * @return index of field that was rated best
	 */
	@Override
	public int doNextTurn(Field lastSet) {
		situationWhite.update(lastSet);
		// TODO make own turn

		// evaluate all possible white turns
		Evaluation evalWhite = new EvaluationHavannah(situationWhite);
		Map<Integer, Double> ratingWhite = evalWhite.getEvaluatedFields();
		Entry<Integer, Double> maxWhite = Collections.max(
				ratingWhite.entrySet(), new FieldValueEntryComparator());
		// Integer bestWhite = maxWhite.getKey();

		// evaluate all possible black turns
		Evaluation evalBlack = new EvaluationHavannah(situationBlack);
		final Map<Integer, Double> ratingBlack = evalBlack.getEvaluatedFields();
		// Entry<Integer, Double> maxBlack =
		// Collections.max(ratingBlack.entrySet(),
		// new FieldValueEntryComparator());
		// Integer bestBlack = maxblack.getKey();

		// System.out.println("best for white: " + best);
		// System.out.println("best for black: " + bestBlack);

		// TODO should use an epsilon when comparing double values
		Optional<Entry<Integer, Double>> bestBoth = Iterables.tryFind(
				ratingWhite.entrySet(),
				new Predicate<Entry<Integer, Double>>() {

					@Override
					public boolean apply(Entry<Integer, Double> input) {
						return ratingBlack.containsValue(input.getValue());
					}
				});

		Integer best = bestBoth.or(maxWhite).getKey();
		situationWhite.update(best, 1);

		return best;
	}

	// ************************************************************************

	private void init(Board board) {
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		situationWhite = new SituationHavannah(analyzer, board);

		analyzer = new BoardAnalyzerHavannah(board, StoneColor.BLACK);
		situationBlack = new SituationHavannah(analyzer, board);
	}

	// ************************************************************************

	private static class FieldValueEntryComparator implements
			Comparator<Entry<Integer, Double>> {

		@Override
		public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}

	}

}
