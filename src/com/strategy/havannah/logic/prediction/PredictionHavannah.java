package com.strategy.havannah.logic.prediction;

import static com.strategy.util.Output.print;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.evaluation.EvaluationHavannah;
import com.strategy.havannah.logic.situation.SituationHavannah;
import com.strategy.util.Debug;
import com.strategy.util.FirstMoveProvider;
import com.strategy.util.PredictedMove;
import com.strategy.util.RowConstant;
import com.strategy.util.StoneColor;
import com.strategy.util.Turn;

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
	private Integer lastTurn = null;
	private List<Turn> turnsSoFar;
	private Evaluation eval;

	public PredictionHavannah(Board board) {
		this.turnsSoFar = Lists.newArrayList();
		init(board, null);
	}

	public PredictionHavannah(Board board, int lastTurn, List<Turn> turns) {
		this.lastTurn = lastTurn;
		this.turnsSoFar = Lists.newArrayList();
		init(board, turns);
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
	public Integer getLastTurn() {
		return lastTurn;
	}

	@Override
	public Evaluation getEvaluation(StoneColor color) {
		if (null == eval || !eval.getColor().equals(color)) {
			eval = EvaluationHavannah.create(situationWhite, situationBlack,
					color);
		}

		return eval;
	}

	@Override
	public List<Turn> getTurnsSoFar() {
		return turnsSoFar;
	}

	@Override
	public Situation getWhite() {
		return situationWhite;
	}

	@Override
	public Situation getBlack() {
		return situationBlack;
	}

	@Override
	public List<PredictedMove> getPrediction(StoneColor color) {
		int boardSize = situationWhite.getBoard().getBoardSize();
		double[] rating = getEvaluation(color).getRating();

		List<PredictedMove> result = Lists.newLinkedList();
		for (int i = 0; i < rating.length; i++) {
			if (!situationWhite.getBoard().isValidField(i)) {
				continue;
			}
			PredictedMove m = PredictedMove.create(boardSize, i, color,
					rating[i]);
			result.add(m);
		}

		if (StoneColor.WHITE.equals(color)) {
			// sort from max to min - max is best for white
			Collections.sort(result, Collections.reverseOrder());
		} else {
			// sort from min to max - min is best for black
			Collections.sort(result);
		}

		return result;
	}

	@Override
	public Integer doCalculatedTurn(StoneColor colorToUse) {

		if (turnsSoFar.isEmpty()) {
			// make a move that doesn't get swapped
			int[] moves = FirstMoveProvider.getMoves(situationWhite.getBoard()
					.getBoardSize());
			if (null != moves) {
				Random r = new Random();
				int index = r.nextInt(moves.length);
				return moves[index];
			}
		}

		// has someone already won?
		checkVictory();
		if (winWhite || winBlack) {
			// no need to go any further
			return null;
		}

		// Evaluation eval = EvaluationHavannah.create(situationWhite,
		// situationBlack, colorToUse);
		int best = getEvaluation(colorToUse).getBestIndex();

		doManualTurn(best, colorToUse);

		return best;
	}

	@Override
	public Integer answerTurn(int fieldIndex, StoneColor colorLastSet) {
		StoneColor cpuColor = colorLastSet.getOpposite();
		doManualTurn(fieldIndex, colorLastSet);

		return doCalculatedTurn(cpuColor);

	}

	@Override
	public void doManualTurn(int fieldIndex, StoneColor colorLastSet) {
		StoneColor playerColor = colorLastSet;
		situationWhite.update(fieldIndex, playerColor);
		situationBlack.update(fieldIndex, playerColor);
		eval = null;

		Board b = situationWhite.getBoard();
		int bSize = b.getBoardSize();
		turnsSoFar
				.add(new Turn(RowConstant.parse(fieldIndex, bSize), RowConstant
						.parseToCoordNumber(fieldIndex, bSize), playerColor));
		lastTurn = fieldIndex;

		checkVictory();
	}

	// ************************************************************************

	private void init(Board board, List<Turn> turns) {
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);

		Debug wlog = Debug.create("BDD creation for " + StoneColor.WHITE);
		situationWhite = new SituationHavannah(analyzer, board,
				StoneColor.WHITE);
		wlog.log();
		Debug blog = Debug.create("BDD creation for " + StoneColor.BLACK);
		situationBlack = new SituationHavannah(analyzer, board,
				StoneColor.BLACK);
		blog.log();

		debugInit();

		analyzer.log();
		analyzer.done();

		if (null != turns && !turns.isEmpty()) {
			for (Turn turn : turns) {
				doManualTurn(
						board.getField(turn.getCoord(), turn.getCoordNumber())
								.getIndex(), turn.getColor());
			}
		}

	}

	private void checkVictory() {
		if (situationWhite.getWinningCondition().isOne()) {
			winWhite = true;
		}
		if (situationBlack.getWinningCondition().isOne()) {
			winBlack = true;
		}
	}

	private void debugInit() {
		print("init:\n", PredictionHavannah.class);
		print("WHITE opponent winning condition is one: "
				+ situationWhite.getWinningCondition().isOne(),
				PredictionHavannah.class);
		print("BLACK opponent winning condition is one: "
				+ situationBlack.getWinningCondition().isOne(),
				PredictionHavannah.class);
	}

	private void debug(Evaluation evalWhite, Evaluation evalBlack) {

		evalWhite.log();
		evalBlack.log();
	}

}
