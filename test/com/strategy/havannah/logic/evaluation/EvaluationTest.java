package com.strategy.havannah.logic.evaluation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.field.BlackStone;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.havannah.logic.situation.SituationHavannah;
import com.strategy.util.GameParser;
import com.strategy.util.GameParser.GameParserException;
import com.strategy.util.Preferences;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class EvaluationTest {

	@Before
	public void before() {
		System.setProperty("bdd", "bdd");
	}

	@Test
	public void testEvaluation() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);
		board.setField(new BlackStone(PositionHexagon.get(0, 0), 0));

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzer analyzerOpp = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);
		Situation sit = new SituationHavannah(analyzer, analyzerOpp, board);
		// Situation sitOpp = new SituationHavannah(analyzerOpp, analyzer,
		// board);
		Evaluation eval = new EvaluationHavannah(board,
				sit.getWinningCondition());

		// System.out.println("max: field " + max.getKey() + " with rating "
		// + max.getValue());

		System.out.println(board.toRatingString(eval.getRating(),
				eval.getBestIndex()));

		// int expected = 8;
		// int actual = eval.getBestIndex();
		// Assert.assertEquals(expected, actual);
	}

	@Test
	public void testFork() {
		Preferences.createInstance(new String[] { "-f" });
		Board board = BoardHavannah.createInstance(
				TestBoardEvaluationProvider.BOARD_FORK, 3);
		System.out.println(board);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzer analyzerOpp = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);
		Situation sit = new SituationHavannah(analyzer, analyzerOpp, board);
		Evaluation eval = new EvaluationHavannah(board,
				sit.getWinningCondition());

		System.out.println(board.toRatingString(eval.getRating(),
				eval.getBestIndex()));

		int expectedBest = 1;
		int actualBest = eval.getBestIndex();
		Assert.assertEquals(expectedBest, actualBest);

		sit.update(actualBest, StoneColor.WHITE);
		Assert.assertTrue(sit.getWinningCondition().isOne());
	}

	@Test
	public void testHgf() {
		String hgf = "(;FF[4]EV[havannah.in.size4.4]PB[mickgraham]PW[kiwibill]SZ[4]RU[LG]RE[B]GC[ game #1541446]SO[http://www.littlegolem.com];"
				+ "W[E1];B[swap];W[F2];B[D3];W[G1];B[E4];W[G4];B[F3];W[E5];B[D6];W[D5];B[C2];W[G3];B[G2];W[E6];B[E3];W[D7])";
		InputStream in = new ByteArrayInputStream(hgf.getBytes());
		GameParser parser;
		try {
			parser = new GameParser(in);
		} catch (GameParserException e) {
			Assert.fail("Got an Exception: " + e.getMessage());
			return;
		}
		Preferences.getInstance().setGenerateFiles(true);
		Preferences.getInstance().setCpuColor(StoneColor.BLACK);
		Board board = BoardHavannah.createInstance(TestBoardProvider.BOARD_4,
				parser.getBoardSize(), parser.getTurns());
		// System.out.println(board);

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		BoardAnalyzer analyzerOpp = new BoardAnalyzerHavannah(board,
				StoneColor.BLACK);
		Situation sit = new SituationHavannah(analyzer, analyzerOpp, board);
		sit.update(46, StoneColor.WHITE);
		Evaluation eval = new EvaluationHavannah(board,
				sit.getWinningCondition());

		// System.out.println(board.toRatingString(eval.getRating(),
		// eval.getBestIndex()));

		int expectedBest = 10;
		int actualBest = eval.getBestIndex();
		Assert.assertEquals(expectedBest, actualBest);
	}
}
