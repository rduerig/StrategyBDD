package com.strategy.api.logic;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.PredictedMove;
import com.strategy.util.PrimitiveBoardProvider;
import com.strategy.util.RowConstant;
import com.strategy.util.StoneColor;
import com.strategy.util.Turn;
import com.strategy.util.preferences.Preferences;

public class GtpWrapper {

	private static final Splitter splitter = Splitter.on(" ")
			.omitEmptyStrings().trimResults();

	public static String SEPARATOR = ",";

	private Board board;
	private Prediction p;

	public GtpWrapper() {
		// Preferences.getInstance().setAlg(PathCalculatorProvider.PathCalculatorKey.ITERATIVE);
		System.out.println("Using Algorithm "
				+ Preferences.getInstance().getAlg());
	}

	public void setBoardSize(Integer size) {
		this.board = BoardHavannah.createInstance(
				PrimitiveBoardProvider.getBoard(size), size);
	}

	public void setMoves(String moves) {
		if (null == board) {
			return;
		}
		List<Turn> turns = parseMoves(moves);
		if (turns.isEmpty()) {
			p = new PredictionHavannah(board);
		} else {
			Turn last = Iterables.getLast(turns);
			Field field = board
					.getField(last.getCoord(), last.getCoordNumber());
			Integer lastIndex = field.getIndex();
			p = new PredictionHavannah(board, lastIndex, turns);
		}
	}

	public String getResponse() {
		if (null == board || null == p) {
			return "?";
		}

		List<Turn> turnsSoFar = p.getTurnsSoFar();
		Integer move;
		if (turnsSoFar.isEmpty()) {
			move = p.doCalculatedTurn(StoneColor.BLACK);
		} else {
			Turn last = Iterables.getLast(turnsSoFar);
			StoneColor colorToUse = last.getColor().getOpposite();
			move = p.doCalculatedTurn(colorToUse);
		}

		if (null != move) {
			return ""
					+ RowConstant.parse(move, board.getBoardSize())
					+ RowConstant
							.parseToCoordNumber(move, board.getBoardSize());
		}

		return "?";
	}

	public String getPredictedMoves() {
		if (null == board || null == p) {
			return "?";
		}

		List<Turn> turnsSoFar = p.getTurnsSoFar();
		StoneColor color;
		if (turnsSoFar.isEmpty()) {
			color = StoneColor.BLACK;
		} else {
			Turn last = Iterables.getLast(turnsSoFar);
			color = last.getColor().getOpposite();
		}

		List<PredictedMove> prediction = p.getPrediction(color);
		StringBuilder sb = new StringBuilder();
		Iterator<PredictedMove> it = prediction.iterator();
		while (it.hasNext()) {
			PredictedMove next = it.next();
			sb.append(next.getTurn().getCoord());
			sb.append(next.getTurn().getCoordNumber());
			if (it.hasNext()) {
				sb.append(SEPARATOR);
			}
		}

		return sb.toString();
	}

	// ************************************************************************

	private List<Turn> parseMoves(String moves) {
		List<Turn> result = Lists.newArrayList();
		Iterable<String> split = splitter.split(moves);
		StoneColor color = StoneColor.BLACK;
		for (String move : split) {
			Turn turn;
			if (move.equals("swap")) {
				if (result.isEmpty()) {
					// swap must actually always be second move
					continue;
				} else {
					Turn last = Iterables.getLast(result);
					turn = new Turn(last.getCoord(), last.getCoordNumber(),
							color);
					result.set(0, turn);
				}
			} else {
				String coordLetterStr = move.substring(0, 1);
				String coordNumberStr = move.substring(1);
				int coordNumber = Integer.parseInt(coordNumberStr);

				RowConstant coord = RowConstant.parseToConstant(coordLetterStr);

				turn = new Turn(coord, coordNumber, color);
				result.add(turn);
			}

			color = color.getOpposite();
		}
		return result;
	}

}
