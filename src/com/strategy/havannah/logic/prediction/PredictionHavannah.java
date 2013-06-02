package com.strategy.havannah.logic.prediction;

import static com.strategy.util.Output.print;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
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
	private boolean winWhite = false;
	private boolean winBlack = false;

	public PredictionHavannah(Board board) {
		init(board);
	}

	@Override
	public boolean isWinWhite() {
		return winWhite;
	}

	@Override
	public boolean isWinBlack() {
		return winBlack;
	}

	@Override
	public int doTurn(StoneColor colorToUse) {

		Evaluation evalWhite = new EvaluationHavannah(
				situationWhite.getBoard(),
				situationWhite.getWinningConditionBridge(),
				situationWhite.getWinningConditionFork(),
				situationBlack.getWinningConditionRing());
		Evaluation evalBlack = new EvaluationHavannah(
				situationBlack.getBoard(),
				situationBlack.getWinningConditionBridge(),
				situationBlack.getWinningConditionFork(),
				situationWhite.getWinningConditionRing());

		// debug(evalWhite, evalBlack);

		double maxWhite = evalWhite.getRating()[evalWhite.getBestIndex()];
		double maxBlack = evalBlack.getRating()[evalBlack.getBestIndex()];
		Integer best = 0;
		if (StoneColor.WHITE.equals(colorToUse)) {
			best = maxWhite >= maxBlack ? evalWhite.getBestIndex() : evalBlack
					.getBestIndex();
			winWhite = evalWhite.getBestBdd().isOne();
		} else {
			best = maxBlack >= maxWhite ? evalBlack.getBestIndex() : evalWhite
					.getBestIndex();
			winBlack = evalBlack.getBestBdd().isOne();
		}

		situationWhite.update(best, colorToUse);
		situationBlack.update(best, colorToUse);

		print("WHITE win ring is zero: "
				+ situationWhite.getWinningConditionRing().not().isOne(),
				PredictionHavannah.class);
		print("BLACK win ring is zero: "
				+ situationBlack.getWinningConditionRing().not().isOne(),
				PredictionHavannah.class);
		print("WHITE win fork is one: "
				+ situationWhite.getWinningConditionFork().isOne(),
				PredictionHavannah.class);
		print("BLACK win fork is one: "
				+ situationBlack.getWinningConditionFork().isOne(),
				PredictionHavannah.class);

		return best;
	}

	@Override
	public int answerTurn(int fieldIndex, StoneColor colorLastSet) {
		StoneColor cpuColor = colorLastSet.getOpposite();
		StoneColor playerColor = colorLastSet;
		situationWhite.update(fieldIndex, playerColor);
		situationBlack.update(fieldIndex, playerColor);

		return doTurn(cpuColor);

	}

	// ************************************************************************

	private void init(Board board) {
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);

		situationWhite = new SituationHavannah(analyzer, board,
				StoneColor.WHITE);
		situationBlack = new SituationHavannah(analyzer, board,
				StoneColor.BLACK);

		analyzer.done();
	}

	private void debug(Evaluation evalWhite, Evaluation evalBlack) {
		print("Rating WHITE:\n"
				+ situationWhite.getBoard().toRatingString(
						evalWhite.getRating(), evalWhite.getBestIndex()),
				PredictionHavannah.class);
		print("Rating BLACK:\n"
				+ situationBlack.getBoard().toRatingString(
						evalBlack.getRating(), evalBlack.getBestIndex()),
				PredictionHavannah.class);

		print("Nodes BDD WHITE:\n" + evalWhite.getBestBdd().nodeCount(),
				PredictionHavannah.class);
		print("Nodes BDD BLACK:\n" + evalBlack.getBestBdd().nodeCount(),
				PredictionHavannah.class);
		print("Satcount BDD WHITE:\n" + evalWhite.getBestBdd().satCount(),
				PredictionHavannah.class);
		print("Satcount BDD BLACK:\n" + evalBlack.getBestBdd().satCount(),
				PredictionHavannah.class);
		print("Paths BDD WHITE:\n" + evalWhite.getBestBdd().pathCount(),
				PredictionHavannah.class);
		print("Paths BDD BLACK:\n" + evalBlack.getBestBdd().pathCount(),
				PredictionHavannah.class);

		Comparator<Entry<Integer, Integer>> comparator = new Comparator<Entry<Integer, Integer>>() {

			@Override
			public int compare(Entry<Integer, Integer> o1,
					Entry<Integer, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};
		Map<Integer, Integer> varsWhite = Maps.newHashMap();
		int[] varProfileWhite = evalWhite.getBestBdd().varProfile();
		for (int i = 0; i < varProfileWhite.length; i++) {
			varsWhite.put(i, varProfileWhite[i]);
		}
		Entry<Integer, Integer> maxEntryWhite = Collections.max(
				varsWhite.entrySet(), comparator);
		print("var count BDD WHITE:\n" + maxEntryWhite.getKey() + ":"
				+ maxEntryWhite.getValue(), PredictionHavannah.class);
		print("best var count BDD WHITE:\n" + evalWhite.getBestIndex() + ":"
				+ varProfileWhite[evalWhite.getBestIndex()],
				PredictionHavannah.class);

		Map<Integer, Integer> varsBlack = Maps.newHashMap();
		int[] varProfileBlack = evalBlack.getBestBdd().varProfile();
		for (int i = 0; i < varProfileBlack.length; i++) {
			varsBlack.put(i, varProfileBlack[i]);
		}
		Entry<Integer, Integer> maxEntryBlack = Collections.max(
				varsBlack.entrySet(), comparator);
		print("var count BDD BLACK:\n" + maxEntryBlack.getKey() + ":"
				+ maxEntryBlack.getValue(), PredictionHavannah.class);
		print("best var count BDD BLACK:\n" + evalBlack.getBestIndex() + ":"
				+ varProfileBlack[evalBlack.getBestIndex()],
				PredictionHavannah.class);
	}

}
