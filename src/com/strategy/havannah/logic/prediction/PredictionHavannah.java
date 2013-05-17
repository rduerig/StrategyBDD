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

		if (situationWhite.getWinningCondition().isOne()) {
			System.out.println("Computer wins!");
			return -1;
		}
		if (situationBlack.getWinningCondition().isOne()) {
			System.out.println("You win!");
			return -1;
		}

		// evaluate all possible white turns
		// System.out.println("evaluate all possible white turns");
		BDD winningCondition;
		BDD winningConditionOpp;
		Evaluation eval;
		Evaluation evalOpp;
		if (color.equals(StoneColor.BLACK)) {
			winningCondition = situationWhite.getWinningCondition().id();
			winningConditionOpp = situationBlack.getWinningCondition().id();
			eval = new EvaluationHavannah(situationWhite.getBoard(),
					winningCondition);
			evalOpp = new EvaluationHavannah(situationBlack.getBoard(),
					winningConditionOpp);
			System.out.println(situationWhite.getBoard().toRatingString(
					eval.getRating(), eval.getBestIndex()));
		} else {
			winningCondition = situationBlack.getWinningCondition().id();
			winningConditionOpp = situationWhite.getWinningCondition().id();
			eval = new EvaluationHavannah(situationBlack.getBoard(),
					winningCondition);
			evalOpp = new EvaluationHavannah(situationWhite.getBoard(),
					winningConditionOpp);
			System.out.println(situationBlack.getBoard().toRatingString(
					eval.getRating(), eval.getBestIndex()));
		}
		// System.out.println("...done");

		// evaluate all possible black turns
		// System.out.println("evaluate all possible black turns");
		// Evaluation evalBlack = new EvaluationHavannah(
		// situationBlack.getBoard(), situationBlack.getWinningCondition());
		// System.out.println("...done");

		Double avgRating = eval.getAverageRating();
		Double avgRatingOpp = evalOpp.getAverageRating();

		Integer best = 0;
		if (avgRating < avgRatingOpp) {
			best = eval.getBestIndex();
		} else {
			best = evalOpp.getBestIndex();
		}

		// System.out.println("do own turn on white situation");
		situationWhite.update(best, color.getOpposite());
		// System.out.println("...done");
		// System.out.println("do own turn on black situation");
		situationBlack.update(best, color.getOpposite());
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

}
