package com.strategy.havannah.logic.situation;

import java.io.ByteArrayInputStream;

import junit.framework.Assert;
import net.sf.javabdd.BDD;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.strategy.AbstractTest;
import com.strategy.api.board.Board;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.evaluation.EvaluationHavannah;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.GameParser;
import com.strategy.util.GameParser.GameParserException;
import com.strategy.util.Output;
import com.strategy.util.StoneColor;

public class RingConditionCalculatorTest extends AbstractTest {

	@BeforeClass
	public static void doBefore() {
		Output.setDebug(RingConditionCalculator.class, false);
	}

	@Test
	public void testHgfHasRing() throws GameParserException {
		String game = "SZ[4];W[D2];B[C1];W[C2];B[E1];W[E2];B[F2];W[E3];B[C3];W[D4];B[D5];W[C4];B[A3];W[B3];B[A2];W[B2]";
		GameParser parser = new GameParser(new ByteArrayInputStream(
				game.getBytes()));
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_4,
				parser.getBoardSize(), parser.getTurns());
		System.out.println(board);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		ConditionCalculator calc = new RingConditionCalculator(analyzer, board,
				StoneColor.WHITE);
		BDD result = calc.getBdd();
		Assert.assertTrue(result.isOne());
	}

	@Test
	public void testHgfHasRingInOneTurn() throws GameParserException {
		String game = "SZ[4];W[D2];B[C1];W[C2];B[E1];W[E2];B[F2];W[E3];B[C3];W[D4];B[D5];W[C4];B[A3];W[B3];B[A2]";
		GameParser parser = new GameParser(new ByteArrayInputStream(
				game.getBytes()));
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_4,
				parser.getBoardSize(), parser.getTurns());

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		Situation sit = new SituationHavannah(analyzer, board, StoneColor.BLACK);
		Situation sit2 = new SituationHavannah(analyzer, board,
				StoneColor.WHITE);

		Evaluation eval = EvaluationHavannah
				.create(sit, sit2, StoneColor.BLACK);
		System.out.println(board);
		System.out.println(board.toRatingString(eval.getRating(),
				eval.getBestIndex()));
		int expected = 38;
		int actual = eval.getBestIndex();
		Assert.assertEquals(expected, actual);

		sit.update(actual, StoneColor.BLACK);
		Assert.assertTrue(sit.getWinningCondition().isOne());
	}

	@Test
	public void testHgfHasRingInOneTurnWithAllConditions()
			throws GameParserException {
		String game = "SZ[4];W[D2];B[C1];W[C2];B[E1];W[E2];B[F2];W[E3];B[C3];W[D4];B[D5];W[C4];B[A3];W[B3];B[A2]";
		GameParser parser = new GameParser(new ByteArrayInputStream(
				game.getBytes()));
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_4,
				parser.getBoardSize(), parser.getTurns());

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board);

		Situation sit = new SituationHavannah(analyzer, board, StoneColor.BLACK);
		Situation sit2 = new SituationHavannah(analyzer, board,
				StoneColor.WHITE);

		Evaluation eval = EvaluationHavannah
				.create(sit, sit2, StoneColor.BLACK);
		System.out.println(board);
		System.out.println(board.toRatingString(eval.getRating(),
				eval.getBestIndex()));
		int expected = 38;
		int actual = eval.getBestIndex();
		Assert.assertEquals(expected, actual);

		sit.update(actual, StoneColor.BLACK);
		Assert.assertTrue(sit.getWinningCondition().isOne());
	}

	@Test
	public void testHgfPredictRingInOneTurn() throws GameParserException {
		String game = "SZ[4];W[D2];B[C1];W[C2];B[E1];W[E2];B[F2];W[E3];B[C3];W[D4];B[D5];W[C4];B[A3];W[B3];B[A2]";
		GameParser parser = new GameParser(new ByteArrayInputStream(
				game.getBytes()));
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_4,
				parser.getBoardSize(), parser.getTurns());

		Output.setDebug(PredictionHavannah.class, true);

		System.out.println(board);

		Prediction p = new PredictionHavannah(board);
		int expected = 38;
		int actual = p.doCalculatedTurn(StoneColor.BLACK);
		Assert.assertEquals(expected, actual);
		Assert.assertTrue(p.isWinBlack());
	}

	@AfterClass
	public static void doAfter() {
		Output.setDebug(PredictionHavannah.class, false);
		Output.setDebug(RingConditionCalculator.class, false);
	}

}
