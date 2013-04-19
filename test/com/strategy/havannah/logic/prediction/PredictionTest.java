package com.strategy.havannah.logic.prediction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalizerHavannah;
import com.strategy.util.FieldGenerator;

/**
 * @author Ralph Dürig
 */
public class PredictionTest {

	private static int[][] BOARD_5x5 = new int[][] {//
	/*    */{ 1, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 2, 2 },//
			{ 1, 1, 0, 2, 0 } };

	private static int[][] BOARD_3x3 = new int[][] {//
	/*    */{ 0, 0, 1 },//
			{ 0, 0, 0 },//
			{ 1, 0, 0 } };

	@Before
	public void before() {
		System.setProperty("bdd", "bdd");
	}

	@Test
	public void testPathsChanging() {
		Board board = BoardHavannah.createInstance(BOARD_5x5, 3);

		BoardAnalyzer analyzer = new BoardAnalizerHavannah(board);
		Prediction p = new PredictionHavannah(analyzer, board);
		Assert.assertEquals(1, p.getPossiblePaths());
		Field field = board.getField(0, 1);
		p.doNextTurn(FieldGenerator.create(2, field.getPosition(),
				field.getIndex()));
		Assert.assertEquals(0, p.getPossiblePaths());

	}

	@Test
	public void testTurn() {
		Board board = BoardHavannah.createInstance(BOARD_5x5, 3);

		BoardAnalyzer analyzer = new BoardAnalizerHavannah(board);
		Prediction p = new PredictionHavannah(analyzer, board);
		Field field = board.getField(1, 0);
		p.doNextTurn(FieldGenerator.create(2, field.getPosition(),
				field.getIndex()));

	}

}
