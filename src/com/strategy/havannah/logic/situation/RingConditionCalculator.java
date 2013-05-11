package com.strategy.havannah.logic.situation;

import static com.strategy.util.Output.print;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.ConditionCalculator;

public class RingConditionCalculator implements ConditionCalculator {

	private static boolean debug = false;

	public static void setDebug(boolean debug) {
		RingConditionCalculator.debug = debug;
	}

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
		// ArrayList<Position> innerPositions = Lists.newArrayList(Iterables
		// .filter(allInnerPos, new EmptyPositionFilter(board)));

		ArrayList<Position> outerPositions = Lists.newArrayList(allPos);
		Iterables.removeAll(outerPositions, allInnerPos);

		print("outer: " + outerPositions.toString(), debug);
		print("inner: " + allInnerPos.toString(), debug);

		BDD opposite = analyzerOpposite.getFactory().zero();
		for (Position innerPos : allInnerPos) {
			ArrayList<Position> neighbours = Lists.newArrayList(
					innerPos.getSouth(), innerPos.getSouthWest(),
					innerPos.getNorthWest(), innerPos.getNorth(),
					innerPos.getNorthEast(), innerPos.getSouthEast());
			for (Position outerPos : outerPositions) {
				for (Position neighbour : neighbours) {
					BDD path = analyzerOpposite.getPath(outerPos, neighbour);
					opposite = opposite.id().orWith(path);
				}
			}
		}

		result = opposite.not();

		analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
	}

	private Iterable<Position> filterInnerPositions(
			Collection<Position> allPos, Board board) {
		InnerFieldPredicate predicate = new InnerFieldPredicate(board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);
		return filtered;
	}

}
