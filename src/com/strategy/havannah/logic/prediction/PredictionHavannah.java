package com.strategy.havannah.logic.prediction;

import static com.strategy.util.Output.print;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	public Evaluation getEvaluationWhite() {
		return createEvaluationWhite();
	}

	@Override
	public Evaluation getEvaluationBlack() {
		return createEvaluationBlack();
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
	public Integer doCalculatedTurn(StoneColor colorToUse) {

		// has someone already won?
		checkVictory();
		if (winWhite || winBlack) {
			// no need to go any further
			return null;
		}

		Evaluation evalWhite = createEvaluationWhite();
		Evaluation evalBlack = createEvaluationBlack();

		debug(evalWhite, evalBlack);

		double maxWhite = evalWhite.getRating()[evalWhite.getBestIndex()];
		double maxBlack = evalBlack.getRating()[evalBlack.getBestIndex()];
		Integer best = 0;
		if (StoneColor.WHITE.equals(colorToUse)) {
			best = maxWhite >= maxBlack ? evalWhite.getBestIndex() : evalBlack
					.getBestIndex();
		} else {
			best = maxBlack >= maxWhite ? evalBlack.getBestIndex() : evalWhite
					.getBestIndex();
		}

		// situationWhite.update(best, colorToUse);
		// situationBlack.update(best, colorToUse);
		//
		// lastTurn = best;
		//
		// checkVictory();

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

		situationWhite = new SituationHavannah(analyzer, board,
				StoneColor.WHITE);
		situationBlack = new SituationHavannah(analyzer, board,
				StoneColor.BLACK);

		debugInit();

		analyzer.done();

		if (null != turns && !turns.isEmpty()) {
			for (Turn turn : turns) {
				doManualTurn(
						board.getField(turn.getCoord(), turn.getCoordNumber())
								.getIndex(), turn.getColor());
			}
		}

	}

	private Evaluation createEvaluationWhite() {
		Evaluation evalWhite = EvaluationHavannah.create(situationWhite);
		return evalWhite;
	}

	private Evaluation createEvaluationBlack() {
		Evaluation evalBlack = EvaluationHavannah.create(situationBlack);
		return evalBlack;
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
