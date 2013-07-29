package com.strategy.havannah;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.Iterables;
import com.strategy.api.board.Board;
import com.strategy.api.interpreter.GtpInterpreter;
import com.strategy.api.interpreter.InterpreterManager;
import com.strategy.api.interpreter.StrategyInterpreter;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.PrimitiveBoardProvider;
import com.strategy.util.StoneColor;
import com.strategy.util.Turn;
import com.strategy.util.preferences.Preferences;

/**
 * @author Ralph Dürig
 */
public class StrategyHavannah {

	/**
	 * @param args
	 *            Command line arguments:
	 *            <table>
	 *            <th><b>Argument</b></th>
	 *            <th><b>Effect</b></th>
	 *            <tr>
	 *            <td>-f</td>
	 *            <td>force generating new bdds and according files</td>
	 *            </tr>
	 *            <tr>
	 *            <td>-hgf [PATH]</td>
	 *            <td>load the hgf file defined by PATH, never forces generating
	 *            new bdds ignoring if -f is present or not</td>
	 *            </tr>
	 *            <tr>
	 *            <td>-s [NUMBER]</td>
	 *            <td>use NUMBER as size for the board, ignored when an hgf file
	 *            is given</td>
	 *            </tr>
	 *            </table>
	 */
	public static void main(String[] args) {
		System.setProperty("bdd", "bdd");
		Preferences.createInstance(args);
		Locale.setDefault(Locale.US);

		if (Preferences.getInstance().isHelp()) {
			return;
		}

		Thread interpreter;
		if (!Preferences.getInstance().isModeInterpreter()) {
			interpreter = new GtpInterpreter();
		} else {
			Board board;
			int boardSize = Preferences.getInstance().getBoardSize();
			int[][] rawBoard = PrimitiveBoardProvider.getBoard(boardSize);
			List<Turn> turns = Preferences.getInstance().getTurns();
			StoneColor cpuColor;
			Prediction p;
			if (null == turns || turns.isEmpty()) {
				board = BoardHavannah.createInstance(rawBoard, boardSize);
				cpuColor = StoneColor.WHITE;
				p = new PredictionHavannah(board);
			} else {
				board = BoardHavannah
						.createInstance(rawBoard, boardSize, turns);
				Turn last = Iterables.getLast(turns);
				cpuColor = last.getColor();
				int lastIndex = board.getField(last.getCoord(),
						last.getCoordNumber()).getIndex();
				p = new PredictionHavannah(board, lastIndex, turns);
			}
			interpreter = new StrategyInterpreter(board, cpuColor, p);
		}
		InterpreterManager.scheduleInterpreter(interpreter);

	}

}
