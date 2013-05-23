package com.strategy.havannah.logic.situation;

import junit.framework.Assert;
import net.sf.javabdd.BDD;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.strategy.AbstractTest;
import com.strategy.api.board.Board;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.util.Output;
import com.strategy.util.StoneColor;

public class ForkCondidtionCalculatorTest extends AbstractTest {

	@BeforeClass
	public static void doBefore() {
		// Output.setDebug(ForkConditionCalculator.class, true);
	}

	@Test
	// @Ignore
	public void testFork1() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_FORK_1, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);

		ConditionCalculator calc = new ForkConditionCalculator(analyzer, board);
		BDD result = calc.getBdd();

		double expected = 1d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual);
	}

	@Test
	// @Ignore
	public void testFork6() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_FORK_6, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);

		ConditionCalculator calc = new ForkConditionCalculator(analyzer, board);
		BDD result = calc.getBdd();

		double expected = 6d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual);
	}

	@AfterClass
	public static void doAfter() {
		Output.setDebug(RingConditionCalculator.class, false);
	}

}
