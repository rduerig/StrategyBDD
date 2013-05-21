package com.strategy.havannah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.Iterables;
import com.strategy.api.board.Board;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.Preferences;
import com.strategy.util.PrimitiveBoardProvider;
import com.strategy.util.Turn;

/**
 * @author Ralph Dürig
 */
public class StrategyHavannah {

	public static void main(String[] args) {
		System.setProperty("bdd", "bdd");

		Preferences.createInstance(args);

		Board board;
		int boardSize = Preferences.getInstance().getBoardSize();
		int[][] rawBoard = PrimitiveBoardProvider.getBoard(boardSize);
		List<Turn> turns = Preferences.getInstance().getTurns();
		if (null == turns || turns.isEmpty()) {
			board = BoardHavannah.createInstance(rawBoard, boardSize);
		} else {
			board = BoardHavannah.createInstance(rawBoard, boardSize, turns);
			Turn last = Iterables.getLast(turns);
			Preferences.getInstance().setCpuColor(last.getColor());
		}

		System.out.println("You are playing "
				+ Preferences.getInstance().getCpuColor().getOpposite());

		Prediction p = new PredictionHavannah(board);

		int limit = board.getRows() * board.getColumns();

		String line = null;
		while (!"exit".equals(line)) {

			System.out.println(board);

			BufferedReader console = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print(Preferences.getInstance().getCpuColor()
					.getOpposite()
					+ "s turn: ");
			try {
				line = console.readLine();
			} catch (IOException e) {
				System.out.println("Error reading from stdin");
				e.printStackTrace();
				break;
			}

			if ("exit".equals(line)) {
				System.out.println("Bye");
				break;
			}

			Integer fieldIndex;
			try {
				fieldIndex = Integer.parseInt(line);
				if (fieldIndex < 0 || fieldIndex >= limit) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out
						.println("Input must be either a possible field number on the board starting with '0' or the word 'exit'");
				continue;
			}

			int next = p.doNextTurn(fieldIndex);
			if (next < 0) {
				break;
			}
			System.out.println(Preferences.getInstance().getCpuColor()
					+ "s turn: " + next);
		}

	}

}
