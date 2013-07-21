package com.strategy.havannah.logic.situation;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.util.BddFactoryProvider;
import com.strategy.util.Output;
import com.strategy.util.StoneColor;
import com.strategy.util.preferences.Preferences;

/**
 * @author Ralph Dürig
 */
public class SituationPerformanceTest {

	@BeforeClass
	public static void before() {
		System.setProperty("bdd", "bdd");
		Output.setDebug(BridgeConditionCalculator.class, true);
		Preferences.getInstance().setGenerateFiles(true);
	}

	@After
	public void doAfter() {
		BddFactoryProvider.reset();
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard3() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_3, 3);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		new SituationHavannah(analyzer, board, StoneColor.WHITE);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard4() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		new SituationHavannah(analyzer, board, StoneColor.WHITE);
	}

	@Test
	@Ignore
	public void testBoard5() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_5, 5);
		System.out.println(board.toIndexString());
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		new SituationHavannah(analyzer, board, StoneColor.BLACK);
	}

	@Test(timeout = 60000)
	@Ignore
	public void testBoard6() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_6, 6);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		new SituationHavannah(analyzer, board, StoneColor.WHITE);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard7() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_7, 7);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		new SituationHavannah(analyzer, board, StoneColor.WHITE);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testSuperTurn() {
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_10,
				10);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		new SituationHavannah(analyzer, board, StoneColor.WHITE);

	}

}
