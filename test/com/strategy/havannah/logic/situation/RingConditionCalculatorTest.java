package com.strategy.havannah.logic.situation;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.util.StoneColor;

public class RingConditionCalculatorTest {

	@Test
	// @Ignore
	public void testBoard4() {
		Board board = BoardHavannah
				.createInstance(TestBoardProvider.BOARD_4, 4);

		BoardAnalyzerHavannah analyzer = new BoardAnalyzerHavannah(board,
				StoneColor.WHITE);

		// BDD ring = analyzer.getFactory().zero();
		Collection<Position> allPos = board.getPositions();

		InnerFieldPredicate predicate = new InnerFieldPredicate(board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);
		ArrayList<Position> innerPositions = Lists.newArrayList(filtered);

		ArrayList<Position> outerPositions = Lists.newArrayList(allPos);
		Iterables.removeAll(outerPositions, innerPositions);

	}

}
