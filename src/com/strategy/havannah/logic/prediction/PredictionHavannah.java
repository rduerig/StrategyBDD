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

	private Situation situationCpu;
	private Situation situationPlayer;

	public PredictionHavannah(Board board) {
		init(board);
	}

	@Override
	public int doNextTurn(int fieldIndex) {
		StoneColor cpuColor = Preferences.getInstance().getCpuColor();
		StoneColor playerColor = cpuColor.getOpposite();
		situationCpu.update(fieldIndex, playerColor);
		situationPlayer.update(fieldIndex, playerColor);

		if (situationCpu.getWinningCondition().isOne()) {
			System.out.println("Computer wins!");
			return -1;
		}
		if (situationPlayer.getWinningCondition().isOne()) {
			System.out.println("You win!");
			return -1;
		}

		// evaluate all possible white turns
		BDD winningCondition = situationCpu.getWinningCondition().id();
		BDD winningConditionOpp = situationPlayer.getWinningCondition().id();
		Evaluation eval = new EvaluationHavannah(situationCpu.getBoard(),
				winningCondition);
		Evaluation evalOpp = new EvaluationHavannah(situationPlayer.getBoard(),
				winningConditionOpp);
		System.out.println(situationPlayer.getBoard().toRatingString(
				eval.getRating(), eval.getBestIndex()));

		Double avgRating = eval.getAverageRating();
		Double avgRatingOpp = evalOpp.getAverageRating();

		Integer best = 0;
		if (avgRating < avgRatingOpp) {
			best = eval.getBestIndex();
		} else {
			best = evalOpp.getBestIndex();
		}

		situationCpu.update(best, cpuColor);
		situationPlayer.update(best, cpuColor);
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
