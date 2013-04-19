package com.strategy.havannah.logic;

import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Prediction;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.util.FieldGenerator;

/**
 * @author Ralph Dürig
 */
public class PredictionTest {

	private static int[][] BOARD_5x5 = new int[][] {//
	/*    */{ 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0 } };

	private static int[][] BOARD_3x3 = new int[][] {//
	/*    */{ 0, 0, 1 },//
			{ 0, 0, 0 },//
			{ 1, 0, 0 } };

	@Test
	public void testTurn() {
		Board board = BoardHavannah.createInstance(BOARD_5x5, 3);
		System.out.println(board);

		BoardAnalyzer analyzer = new BoardAnalizerHavannah(board);
		Prediction p = new Prediction(analyzer, board);
		p.doNextTurn(FieldGenerator.create(2, PositionHexagon.get(1, 0), 2));

	}

}
