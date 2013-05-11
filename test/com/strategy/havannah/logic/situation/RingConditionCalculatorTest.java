package com.strategy.havannah.logic.situation;

import static junit.framework.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import junit.framework.Assert;
import net.sf.javabdd.BDD;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.evaluation.EvaluationHavannah;
import com.strategy.util.StoneColor;

public class RingConditionCalculatorTest {

	@BeforeClass
	public static void doBefore() {
		RingConditionCalculator.setDebug(false);
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
		assertEquals(expected, actual);

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
		assertEquals(expected, actual);

	}

	@Test
	// @Ignore
	public void testEvaluationRing4() {
		Board board = BoardHavannah.createInstance(
				TestBoardProviderConditions.BOARD_RING_2, 3);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzerHavannah analyzerOpp = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);

		ConditionCalculator calc = new RingConditionCalculator(analyzer,
				analyzerOpp, board);
		BDD result = calc.getBdd();

		Evaluation eval = new EvaluationHavannah(board, result);

		System.out.println(board.toRatingString(eval.getRating(),
				eval.getBestIndex()));
		StringWriter stringWriter = new StringWriter();
		BufferedWriter out = new BufferedWriter(stringWriter);
		try {
			analyzer.getFactory().save(out, result);
			out.close();
		} catch (IOException e) {
			Assert.fail("IOException occured!");
		}

		System.out.println(stringWriter.toString());
		double expected = 4d;
		double actual = result.pathCount();
		analyzer.done();
		assertEquals(expected, actual);

	}

	@AfterClass
	public static void doAfter() {
		RingConditionCalculator.setDebug(false);
	}

}
