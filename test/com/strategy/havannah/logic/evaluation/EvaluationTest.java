package com.strategy.havannah.logic.evaluation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.field.WhiteStone;
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
		// for empty board with size 4 ratings are around 2^45
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);
		board.setField(new WhiteStone(PositionHexagon.get(0, 0), 0));

		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);
		Situation sit = new SituationHavannah(analyzer, board);
		Evaluation eval = new EvaluationHavannah(sit);

		// System.out.println("max: field " + max.getKey() + " with rating "
		// + max.getValue());

		int expected = 8;
		int actual = eval.getBestIndex();
		Assert.assertEquals(expected, actual);
	}

}
