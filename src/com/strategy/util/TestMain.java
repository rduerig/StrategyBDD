package com.strategy.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import net.sf.javabdd.BDD;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.situation.SituationHavannah;

/**
 * @author Ralph Dürig
 */
public class TestMain {

	private static Joiner joiner = Joiner.on(", ");
	private static int SIZE = 2;

	public static void main(String[] args) throws FileNotFoundException {
		Preferences.getInstance().setGenerateFiles(true);
		Preferences.getInstance().setOut(
				new PrintStream("operations" + SIZE + ".log"));
		// System.setOut(new PrintStream("operations2.log"));
		int[][] rawBoard = PrimitiveBoardProvider.getBoard(SIZE);
		Board board = BoardHavannah.createInstance(rawBoard, SIZE);
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board, StoneColor.WHITE);
		analyzer.done();
		// BDD win = sit.getWinningCondition();
		// System.out.println("win:" + win);
		// System.out.println("satCount: " + win.satCount());
		// System.out.println("pathCount: " + win.pathCount());
		// System.out.println("nodeCount: " + win.nodeCount());

		// human readable output of solutions (playouts)
		// printBoards(win, size);

		// latex output of solutions
		// printLatex(win, size);
	}

	// ************************************************************************

	private static void printBoards(BDD win, int size) {
		int[][] rawBoard = PrimitiveBoardProvider.getBoard(size);
		for (Object o : win.allsat()) {
			byte[] solution = (byte[]) o;
			// System.out.println(Arrays.toString(solution));
			Board cpBoard = BoardHavannah.createInstance(rawBoard, size);
			for (int i = 0; i < solution.length; i++) {
				if (solution[i] == 1) {
					Field field = cpBoard.getField(i);
					cpBoard.setField(FieldGenerator.create(1,
							field.getPosition(), field.getIndex()));
				}
			}
			System.out.println(cpBoard);
		}
	}

	private static void printLatex(BDD win, int size) {
		StringBuilder sb;
		for (Object o : win.allsat()) {
			byte[] solution = (byte[]) o;
			List<String> fields = Lists.newArrayList();
			for (int i = 0; i < solution.length; i++) {
				if (solution[i] == 1) {
					fields.add(RowConstant.parseToCoordString(i, size));
				}
			}
			sb = new StringBuilder();
			sb.append("\\begin{HavannahBoard}[board size=2]\n");
			sb.append("\\HStoneGroup[color=white]{");
			sb.append(joiner.join(fields));
			sb.append("}\n");
			sb.append("\\end{HavannahBoard}");
			System.out.println(sb.toString());
		}
	}

}
