package com.strategy.havannah.logic.prediction;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.util.BddFactoryProvider;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class PredictionTest {

	@Before
	public void before() {
		// System.setProperty("bdd", "bdd");
	}

	@After
	public void doAfter() {
		BddFactoryProvider.reset();
	}

	@Test
	@Ignore
	public void testNextField4() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);

		Prediction p = new PredictionHavannah(board);
		p.answerTurn(0, StoneColor.BLACK);
	}

	@Test
	@Ignore
	public void testNextField5() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_5, 5);

		Prediction p = new PredictionHavannah(board);
		p.answerTurn(0, StoneColor.BLACK);
	}

}
