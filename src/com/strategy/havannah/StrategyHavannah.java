package com.strategy.havannah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.strategy.api.board.Board;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.FieldGenerator;
import com.strategy.util.PrimitiveBoardProvider;

/**
 * @author Ralph Dürig
 */
public class StrategyHavannah {

	public static void main(String[] args) {
		System.setProperty("bdd", "bdd");

		int[][] rawBoard = PrimitiveBoardProvider.BOARD_3;
		Board board = BoardHavannah.createInstance(rawBoard, 3);
		System.out.println(board.toIndexString());

		Prediction p = new PredictionHavannah(board);

		int limit = board.getRows() * board.getColumns();

		String line = null;
		while (!"exit".equals(line)) {

			System.out.println(board);

			BufferedReader console = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print("Your turn: ");
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

			p.doNextTurn(FieldGenerator.create(2, PositionHexagon.get(
					fieldIndex / board.getRows(),
					fieldIndex % board.getColumns()), fieldIndex));
		}

	}

}
