package com.strategy.havannah.logic.prediction;

import static com.strategy.util.Output.print;

import java.util.Collections;
import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Lists;
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
	private boolean winWite = false;
	private boolean winBlack = false;

	public PredictionHavannah(Board board) {
		init(board);
	}

	@Override
	public boolean isWinWhite() {
		return winWite;
	}

	@Override
	public boolean isWinBlack() {
		return winBlack;
	}

	@Override
	public int doTurn(StoneColor colorToUse) {
		// evaluate all possible white turns
		Map<Double, Evaluation> mappingWhite = getMapping(situationWhite);
		Double maxWhite = Collections.max(Lists.newArrayList(mappingWhite
				.keySet()));
		// evaluate all possible black turns
		Map<Double, Evaluation> mappingBlack = getMapping(situationBlack);
		Double maxBlack = Collections.max(Lists.newArrayList(mappingBlack
				.keySet()));
		print("max " + StoneColor.WHITE + ": " + maxWhite + " | max "
				+ StoneColor.BLACK + ": " + maxBlack, PredictionHavannah.class);
		Integer best = maxWhite >= maxBlack ? mappingWhite.get(maxWhite)
				.getBestIndex() : mappingBlack.get(maxBlack).getBestIndex();

		situationWhite.update(best, colorToUse);
		if (situationWhite.hasFork() || situationWhite.hasBridge()
				|| situationWhite.hasRing()) {
			winWite = true;
		}
		situationBlack.update(best, colorToUse);
		if (situationBlack.hasFork() || situationBlack.hasBridge()
				|| situationBlack.hasRing()) {
			winBlack = true;
		}

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

	private Map<Double, Evaluation> getMapping(Situation sit) {
		BDD winningConditionFork = sit.getWinningConditionFork().id();
		BDD winningConditionBridge = sit.getWinningConditionBridge().id();
		BDD winningConditionRing = sit.getWinningConditionRing().id();
		Evaluation evalFork = new EvaluationHavannah(sit.getBoard(),
				winningConditionFork);
		Evaluation evalBridge = new EvaluationHavannah(sit.getBoard(),
				winningConditionBridge);
		Evaluation evalRing = new EvaluationHavannah(sit.getBoard(),
				winningConditionRing);

		double bestRatingFork = evalFork.getRating()[evalFork.getBestIndex()];
		print("best rating fork for " + sit.getStoneColor() + ": "
				+ bestRatingFork + " - best index: " + evalFork.getBestIndex(),
				PredictionHavannah.class);
		double bestRatingBridge = evalBridge.getRating()[evalBridge
				.getBestIndex()];
		print("best rating bridge for " + sit.getStoneColor() + ": "
				+ bestRatingBridge + " - best index: "
				+ evalBridge.getBestIndex(), PredictionHavannah.class);
		double bestRatingRing = evalRing.getRating()[evalRing.getBestIndex()];
		print("best rating ring for " + sit.getStoneColor() + ": "
				+ bestRatingRing + " - best index: " + evalRing.getBestIndex(),
				PredictionHavannah.class);

		Map<Double, Evaluation> mapping = Maps.newHashMap();
		mapping.put(bestRatingFork, evalFork);
		mapping.put(bestRatingBridge, evalBridge);
		mapping.put(bestRatingRing, evalRing);

		return mapping;
	}

}
