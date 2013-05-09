package com.strategy.havannah.logic.situation;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.javabdd.BDD;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.ConditionCalculator;

public class RingConditionCalculator implements ConditionCalculator {

	private BDD result;

	public RingConditionCalculator(BoardAnalyzer analyzer,
			BoardAnalyzer analyzerOpposite, Board board) {
		calculateResult(analyzer, analyzerOpposite, board);
	}

	@Override
	public BDD getBdd() {
		return result;
	}

	// ************************************************************************

	private void calculateResult(BoardAnalyzer analyzer,
			BoardAnalyzer analyzerOpposite, Board board) {
		result = analyzer.getFactory().zero();
		Collection<Position> allPos = board.getPositions();

		ArrayList<Position> innerPositions = Lists
				.newArrayList(filterInnerPositions(allPos, board));

		ArrayList<Position> outerPositions = Lists.newArrayList(allPos);
		Iterables.removeAll(outerPositions, innerPositions);

		for (Position innerPos : innerPositions) {
			for (Position outerPos : outerPositions) {
				BDD path = analyzerOpposite.getPath(innerPos, outerPos);
				result = result.id().andWith(path.not());
			}
		}
	}

	private Iterable<Position> filterInnerPositions(
			Collection<Position> allPos, Board board) {
		InnerFieldPredicate predicate = new InnerFieldPredicate(board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);
		return filtered;
	}

}
