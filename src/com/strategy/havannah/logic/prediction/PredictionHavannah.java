package com.strategy.havannah.logic.prediction;

import net.sf.javabdd.BDD;

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

	public static int WIN_CPU = -1;
	public static int WIN_PLAYER = -2;

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

		// evaluate all possible white turns
		BDD winningCondition = situationCpu.getWinningCondition().id();
		BDD winningConditionOpp = situationPlayer.getWinningCondition().id();
		Evaluation eval = new EvaluationHavannah(situationCpu.getBoard(),
				winningCondition);
		Evaluation evalOpp = new EvaluationHavannah(situationPlayer.getBoard(),
				winningConditionOpp);

		Double avgRating = eval.getAverageRating();
		Double avgRatingOpp = evalOpp.getAverageRating();

		Integer best = 0;
		if (avgRating < avgRatingOpp) {
			best = eval.getBestIndex();
		} else {
			best = evalOpp.getBestIndex();
		}

		situationCpu.update(best, cpuColor);
		if (situationCpu.getWinningCondition().isOne()) {
			winCpu = true;
		}
		situationPlayer.update(best, cpuColor);
		if (situationPlayer.getWinningCondition().isOne()) {
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

}
