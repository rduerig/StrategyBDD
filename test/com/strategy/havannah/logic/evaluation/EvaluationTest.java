package com.strategy.havannah.logic.evaluation;

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
}
