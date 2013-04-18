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

	@Test
	public void testTurn() {
		// int[][] rawBoard = new int[][] {//
		// /* */{ 0, 0, 0, 1, 1 },//
		// { 0, 0, 0, 0, 1 },//
		// { 0, 0, 0, 0, 0 },//
		// { 1, 0, 0, 0, 0 },//
		// { 1, 1, 0, 0, 0 } };

		int[][] rawBoard = new int[][] {//
		/*    */{ 0, 0, 1 },//
				{ 0, 0, 0 },//
				{ 1, 0, 0 } };

		Board board = BoardHavannah.createInstance(rawBoard, 2);
		System.out.println(board);

		BoardAnalyzer analyzer = new BoardAnalizerHavannah(board);
		Prediction p = new Prediction(analyzer, board);
		p.doNextTurn(FieldGenerator.create(2, PositionHexagon.get(0, 0), 0));

	}

}
