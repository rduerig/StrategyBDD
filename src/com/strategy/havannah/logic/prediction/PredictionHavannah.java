package com.strategy.havannah.logic.prediction;

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
import com.strategy.util.Preferences;
import com.strategy.util.StoneColor;

/**
 * Uses a {@link Situation} and its {@link Evaluation} to predict where to set
 * the next stone.
 * 
 * @author Ralph Dürig
 */
public class PredictionHavannah implements Prediction {

	private Situation situationCpu;
	private Situation situationPlayer;
	private boolean winCpu = false;
	private boolean winPlayer = false;

	public PredictionHavannah(Board board) {
		init(board);
	}

	@Override
	public boolean isWinCpu() {
		return winCpu;
	}

	@Override
	public boolean isWinPlayer() {
		return winPlayer;
	}

	@Override
	public int doNextTurn(int fieldIndex) {
		StoneColor cpuColor = Preferences.getInstance().getCpuColor();
		StoneColor playerColor = cpuColor.getOpposite();
		situationCpu.update(fieldIndex, playerColor);
		situationPlayer.update(fieldIndex, playerColor);

		// evaluate all possible cpu turns
		Map<Double, Evaluation> mappingCpu = getMapping(situationCpu);
		Double maxCpu = Collections
				.max(Lists.newArrayList(mappingCpu.keySet()));
		// evaluate all possible player turns
		Map<Double, Evaluation> mappingPlayer = getMapping(situationPlayer);
		Double maxPlayer = Collections.max(Lists.newArrayList(mappingPlayer
				.keySet()));
		System.out
				.println("max cpu: " + maxCpu + " | max player: " + maxPlayer);
		Integer best = maxCpu >= maxPlayer ? mappingCpu.get(maxCpu)
				.getBestIndex() : mappingPlayer.get(maxPlayer).getBestIndex();

		situationCpu.update(best, cpuColor);
		if (situationCpu.hasFork() || situationCpu.hasBridge()
				|| situationCpu.hasRing()) {
			winCpu = true;
		}
		situationPlayer.update(best, cpuColor);
		if (situationPlayer.hasFork() || situationPlayer.hasBridge()
				|| situationPlayer.hasRing()) {
			winPlayer = true;
		}

		return best;
	}

	// ************************************************************************

	private void init(Board board) {
		BoardAnalyzer analyzerCpu = new BoardAnalyzerHavannah(board,
				Preferences.getInstance().getCpuColor());
		BoardAnalyzer analyzerPlayer = new BoardAnalyzerHavannah(board,
				Preferences.getInstance().getCpuColor().getOpposite());

		situationCpu = new SituationHavannah(analyzerCpu, analyzerPlayer, board);
		situationPlayer = new SituationHavannah(analyzerPlayer, analyzerCpu,
				board);

		analyzerCpu.done();
		analyzerPlayer.done();
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
		double bestRatingBridge = evalBridge.getRating()[evalBridge
				.getBestIndex()];
		double bestRatingRing = evalRing.getRating()[evalRing.getBestIndex()];

		Map<Double, Evaluation> mapping = Maps.newHashMap();
		mapping.put(bestRatingFork, evalFork);
		mapping.put(bestRatingBridge, evalBridge);
		mapping.put(bestRatingRing, evalRing);

		return mapping;
	}

}
