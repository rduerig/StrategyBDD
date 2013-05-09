package com.strategy.havannah.logic.situation;

import static com.strategy.util.Output.print;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.javabdd.BDD;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.util.EmptyPositionFilter;

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

		Iterable<Position> allInnerPos = filterInnerPositions(allPos, board);
		ArrayList<Position> innerPositions = Lists.newArrayList(Iterables
				.filter(allInnerPos, new EmptyPositionFilter(board)));

		ArrayList<Position> outerPositions = Lists.newArrayList(allPos);
		Iterables.removeAll(outerPositions, innerPositions);

		print("outer: " + outerPositions.toString(), debug);
		print("inner: " + innerPositions.toString(), debug);

		BDD opposite = analyzerOpposite.getFactory().zero();
		for (Position innerPos : innerPositions) {
			for (Position outerPos : outerPositions) {
				BDD path = analyzerOpposite.getPath(outerPos, innerPos);
				opposite = opposite.id().orWith(path);
			}
		}

		result = opposite.not();
	}

	private Iterable<Position> filterInnerPositions(
			Collection<Position> allPos, Board board) {
		InnerFieldPredicate predicate = new InnerFieldPredicate(board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);
		return filtered;
	}

}
