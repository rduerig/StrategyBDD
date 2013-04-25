package com.strategy.havannah.logic.situation;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;

/**
 * @author Ralph Dürig
 */
public class SituationPerformanceTest {

	private static int[][] BOARD_10 = new int[][] {//
	/*    */{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private static int[][] BOARD_7 = new int[][] {//
	/*    */{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 } };

	private static int[][] BOARD_6 = new int[][] {//
	/*    */{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 } };

	private static int[][] BOARD_5 = new int[][] {//
	/*    */{ 0, 0, 0, 0, 0, 1, 1, 1, 1 },//
			{ 0, 2, 2, 2, 2, 2, 1, 1, 1 },//
			{ 0, 2, 0, 0, 0, 0, 0, 1, 1 },//
			{ 0, 2, 0, 0, 0, 0, 0, 0, 1 },//
			{ 0, 2, 0, 0, 0, 0, 0, 0, 2 },//
			{ 1, 2, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 1, 2, 0, 0, 0, 2 } };

	private static int[][] BOARD_4 = new int[][] {//
	/*    */{ 0, 0, 0, 0, 1, 1, 1 },//
			{ 0, 0, 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0, 0, 0 },//
			{ 1, 1, 1, 0, 0, 0, 0 } };

	private static int[][] BOARD_3 = new int[][] {//
	/*    */{ 0, 2, 0, 1, 1 },//
			{ 2, 0, 0, 2, 1 },//
			{ 0, 2, 2, 2, 2 },//
			{ 1, 2, 0, 0, 0 },//
			{ 1, 1, 0, 2, 0 } };

	private static int[][] BOARD_2 = new int[][] {//
	/*    */{ 0, 0, 1 },//
			{ 0, 0, 0 },//
			{ 1, 0, 0 } };

	@Before
	public void before() {
		System.setProperty("bdd", "bdd");
	}

	@Test(timeout = 20000)
	public void testBoard3() {
		Board board = BoardHavannah.createInstance(BOARD_3, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		System.out.println(sit.getWinningCondition().satCount());
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard4() {
		Board board = BoardHavannah.createInstance(BOARD_4, 4);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard5() {
		Board board = BoardHavannah.createInstance(BOARD_5, 5);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard6() {
		Board board = BoardHavannah.createInstance(BOARD_6, 6);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testBoard7() {
		Board board = BoardHavannah.createInstance(BOARD_7, 7);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
	}

	@Test(timeout = 20000)
	@Ignore
	public void testSuperTurn() {
		Board board = BoardHavannah.createInstance(BOARD_10, 10);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);

	}

}
