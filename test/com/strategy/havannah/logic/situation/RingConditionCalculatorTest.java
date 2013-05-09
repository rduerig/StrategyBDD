package com.strategy.havannah.logic.situation;

import junit.framework.Assert;
import net.sf.javabdd.BDD;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.util.StoneColor;

public class RingConditionCalculatorTest {

	@BeforeClass
	public static void doBefore() {
		RingConditionCalculator.setDebug(true);
	}

	@Test
	// @Ignore
	public void testRing1() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_RING_1, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzerHavannah analyzerOpp = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);

		ConditionCalculator calc = new RingConditionCalculator(analyzer,
				analyzerOpp, board);
		BDD result = calc.getBdd();

		double expected = 1d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual);

	}

	@Test
	// @Ignore
	public void testRing4() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_RING_2, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzerHavannah analyzerOpp = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);

		ConditionCalculator calc = new RingConditionCalculator(analyzer,
				analyzerOpp, board);
		BDD result = calc.getBdd();

		double expected = 4d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual);

	}

	@AfterClass
	public static void doAfter() {
		RingConditionCalculator.setDebug(false);
	}

}
