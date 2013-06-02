package com.strategy.havannah.logic.situation;

import java.io.ByteArrayInputStream;

import junit.framework.Assert;
import net.sf.javabdd.BDD;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.strategy.AbstractTest;
import com.strategy.api.board.Board;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.GameParser;
import com.strategy.util.GameParser.GameParserException;
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

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		ConditionCalculator calc = new ForkConditionCalculator(analyzer, board,
				StoneColor.WHITE);
		BDD result = calc.getBdd();

		double expected = 1d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual, 0d);
	}

	@Test
	// @Ignore
	public void testFork6() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_FORK_6, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		ConditionCalculator calc = new ForkConditionCalculator(analyzer, board,
				StoneColor.WHITE);
		BDD result = calc.getBdd();

		double expected = 6d;
		double actual = result.pathCount();
		analyzer.done();
		Assert.assertEquals(expected, actual, 0d);
	}

	@Test
	public void testSamplePredictForkInOneTurn() {
		int[][] raw = new int[][] {//
		/*    */{ 0, 2, 0, 1, 1 },//
				{ 0, 0, 2, 2, 1 },//
				{ 0, 0, 2, 0, 0 },//
				{ 1, 0, 0, 0, 0 },//
				{ 1, 1, 0, 2, 0 } };
		Board board = BoardHavannah.createInstance(raw, 3);

		Output.setDebug(PredictionHavannah.class, true);

		System.out.println(board);

		Prediction p = new PredictionHavannah(board);
		int expected = 17;
		int actual = p.doCalculatedTurn(StoneColor.BLACK);
		Assert.assertEquals(expected, actual);
		Assert.assertTrue(p.isWinBlack());
	}

	@Test
	public void testHgfPredictForkInOneTurn() throws GameParserException {
		String game = "SZ[4];W[C6];B[C5];W[D6];B[B3];W[D5];B[E6];W[E5];B[C4];W[F5];B[E4];W[F4];B[D4];";
		GameParser parser = new GameParser(new ByteArrayInputStream(
				game.getBytes()));
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_4,
				parser.getBoardSize(), parser.getTurns());

		Output.setDebug(PredictionHavannah.class, true);

		System.out.println(board);

		Prediction p = new PredictionHavannah(board);
		int expected = 11;
		int actual = p.doCalculatedTurn(StoneColor.BLACK);
		Assert.assertEquals(expected, actual);
		Assert.assertTrue(p.isWinBlack());
	}

	@AfterClass
	public static void doAfter() {
		Output.setDebug(RingConditionCalculator.class, false);
	}

}
