package com.strategy.havannah.logic.prediction;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.primitives.Doubles;
import com.strategy.api.board.Board;
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

	@Override
	public int doNextTurn(int fieldIndex, StoneColor color) {
		// System.out.println("update situation white");
		situationWhite.update(fieldIndex, color);
		// System.out.println("...done");
		// System.out.println("update situation black");
		situationBlack.update(fieldIndex, color);
		// System.out.println("...done");

		// evaluate all possible white turns
		// System.out.println("evaluate all possible white turns");
		Evaluation evalWhite = new EvaluationHavannah(
				situationWhite.getBoard(), situationWhite.getWinningCondition());
		// System.out.println("...done");

		// evaluate all possible black turns
		// System.out.println("evaluate all possible black turns");
		Evaluation evalBlack = new EvaluationHavannah(
				situationBlack.getBoard(), situationBlack.getWinningCondition());
		// System.out.println("...done");

		Double avgRatingWhite = evalWhite.getAverageRating();
		Double avgRatingBlack = evalBlack.getAverageRating();

		Integer best = 0;
		if (avgRatingWhite >= avgRatingBlack) {
			best = evalWhite.getBestIndex();
		} else {
			best = evalBlack.getBestIndex();
		}

		// System.out.println("do own turn on white situation");
		situationWhite.update(best, StoneColor.WHITE);
		// System.out.println("...done");
		// System.out.println("do own turn on black situation");
		situationBlack.update(best, StoneColor.WHITE);
		// System.out.println("...done");

		return best;
	}

	// ************************************************************************

	private void init(Board board) {
		BoardAnalyzer analyzerWhite = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzer analyzerBlack = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);

		situationWhite = new SituationHavannah(analyzerWhite, analyzerBlack,
				board);
		situationBlack = new SituationHavannah(analyzerBlack, analyzerWhite,
				board);

		analyzerWhite.done();
		analyzerBlack.done();
	}

	private Double getAverage(Collection<Double> values) {
		Double sum = 0d;
		Integer valueCount = values.size();
		for (Double val : values) {
			sum += val;
		}
		Double avg = sum / valueCount;
		return avg;
	}

	private int getBestIndex(Map<Integer, Double> map) {
		return Collections.max(map.entrySet(), new DoubleEntryComparator())
				.getKey();
	}

	// ************************************************************************

	private static class DoubleEntryComparator implements
			Comparator<Entry<Integer, Double>> {
		@Override
		public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
			return Doubles.compare(o1.getValue(), o2.getValue());
		}
	}

}
