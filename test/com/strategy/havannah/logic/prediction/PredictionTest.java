package com.strategy.havannah.logic.prediction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.situation.SituationHavannah;
import com.strategy.util.FieldGenerator;

/**
 * @author Ralph Dürig
 */
public class PredictionTest {

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

	@Test
	@Ignore
	public void testPathsChanging() {
		Board board = BoardHavannah.createInstance(BOARD_3, 3);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);
		Assert.assertEquals(1, p.getPossiblePaths());
		Field field = board.getField(0, 1);
		p.doNextTurn(FieldGenerator.create(2, field.getPosition(),
				field.getIndex()));
		Assert.assertEquals(0, p.getPossiblePaths());

	}

	@Test
	@Ignore
	public void testBoard3() {
		Board board = BoardHavannah.createInstance(BOARD_3, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);
	}

	@Test
	@Ignore
	public void testBoard4() {
		Board board = BoardHavannah.createInstance(BOARD_4, 4);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);
	}

	@Test
	public void testBoard5() {
		Board board = BoardHavannah.createInstance(BOARD_5, 5);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);
	}

	@Test
	@Ignore
	public void testBoard6() {
		Board board = BoardHavannah.createInstance(BOARD_6, 6);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);
	}

	@Test
	@Ignore
	public void testBoard7() {
		Board board = BoardHavannah.createInstance(BOARD_7, 7);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);
	}

	@Test
	@Ignore
	public void testSuperTurn() {
		Board board = BoardHavannah.createInstance(BOARD_10, 10);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board);
		Prediction p = new PredictionHavannah(sit);

	}

}
