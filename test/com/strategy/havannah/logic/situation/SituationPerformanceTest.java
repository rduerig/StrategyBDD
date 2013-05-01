package com.strategy.havannah.logic.situation;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class SituationPerformanceTest {

	@Before
	public void before() {
		System.setProperty("bdd", "bdd");
	}

	@Test(timeout = 20000)
	// @Ignore
	public void testBoard3() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_3, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	// @Ignore
	public void testBoard4() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 30000)
	// @Ignore
	public void testBoard5() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_5, 5);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 60000)
	@Ignore
	public void testBoard6() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_6, 6);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard7() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_7, 7);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testSuperTurn() {
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_10,
				10);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);

	}

}
