package com.strategy.havannah.logic.prediction;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;

/**
 * @author Ralph Dürig
 */
public class PredictionTest {

	@Before
	public void before() {
		System.setProperty("bdd", "bdd");
	}

	@Test
	@Ignore
	public void testNextField() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);

		Prediction p = new PredictionHavannah(board);
		p.doNextTurn(FieldGenerator.create(1, PositionHexagon.get(0, 0), 0));
	}

}
