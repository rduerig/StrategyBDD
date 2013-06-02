package com.strategy.havannah.logic.situation;

import junit.framework.Assert;
import net.sf.javabdd.BDD;

import org.junit.Test;

import com.strategy.AbstractTest;
import com.strategy.api.board.Board;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.util.StoneColor;

public class BridgeCondidtionCalculatorTest extends AbstractTest {

	@Test
	// @Ignore
	public void testBridge5() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_BRIDGE_5, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		ConditionCalculator calc = new BridgeConditionCalculator(analyzer,
				board, StoneColor.WHITE);
		BDD result = calc.getBdd();

		double expected = 5d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual);
	}

	@Test
	// @Ignore
	public void testBridge14() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_BRIDGE_14, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		ConditionCalculator calc = new BridgeConditionCalculator(analyzer,
				board, StoneColor.WHITE);
		BDD result = calc.getBdd();

		double expected = 14d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual);
	}

}
