package com.strategy.havannah.logic.situation;

import static com.strategy.util.Output.print;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.HasDebugFlag;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.ConditionCalculator;

public class RingConditionCalculator implements ConditionCalculator,
		HasDebugFlag {

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
		Collection<Position> allPos = board.getPositions();

		ArrayList<Position> allInnerPos = Lists
				.newArrayList(filterInnerPositions(allPos, board));

		ArrayList<Position> outerPositions = Lists.newArrayList(allPos);
		Iterables.removeAll(outerPositions, allInnerPos);

		print("outer: " + outerPositions.toString(),
				RingConditionCalculator.class);
		print("inner: " + allInnerPos.toString(), RingConditionCalculator.class);

		result = analyzer.getFactory().zero();
		for (Position innerPos : allInnerPos) {
			BDD innerPosInRing = analyzer.getFactory().one();
			ArrayList<Position> neighbours = Lists.newArrayList(
					innerPos.getSouth(), innerPos.getSouthWest(),
					innerPos.getNorthWest(), innerPos.getNorth(),
					innerPos.getNorthEast(), innerPos.getSouthEast());
			for (Position outerPos : outerPositions) {
				BDD outerPosCannotReachInnerPos = analyzer.getFactory().one();
				for (Position neighbour : neighbours) {
					if (board.isValidField(neighbour)) {
						// path = there is no path for the opposite color from
						// outerPos to neighbour
						BDD path = analyzerOpposite
								.getPath(outerPos, neighbour);
						// print("path from " + outerPos + " to " + neighbour +
						// ": "
						// + path, RingConditionCalculator.class);
						outerPosCannotReachInnerPos = outerPosCannotReachInnerPos
								.id().andWith(path.not());
					}
				}
				innerPosInRing = innerPosInRing.id().andWith(
						outerPosCannotReachInnerPos);
			}
			result = result.id().orWith(innerPosInRing);
		}

		result = result.not();

		analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
	}

	private Iterable<Position> filterInnerPositions(
			Collection<Position> allPos, Board board) {
		InnerFieldPredicate predicate = new InnerFieldPredicate(board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);
		return filtered;
	}

}
