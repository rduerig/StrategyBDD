package com.strategy.havannah.logic.situation;

import static com.strategy.util.Output.print;

import java.util.Collection;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.HasDebugFlag;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;
import com.strategy.util.predicates.ValidPositionFilter;

public class RingConditionCalculator implements ConditionCalculator,
		HasDebugFlag {

	private BDD result;
	private StoneColor color;

	public RingConditionCalculator(BoardAnalyzer analyzer, Board board,
			StoneColor color) {
		this.color = color;
		calculateResult(analyzer, board);
	}

	@Override
	public BDD getBdd() {
		return result;
	}

	// ************************************************************************

	private void calculateResult(BoardAnalyzer analyzer, Board board) {
		Collection<Position> allPos = board.getPositions();

		List<Position> allInnerPos = Lists.newArrayList(filterInnerPositions(
				allPos, board));

		List<Position> outerPositions = Lists.newArrayList(allPos);
		Iterables.removeAll(outerPositions, allInnerPos);

		print("outer: " + outerPositions.toString(),
				RingConditionCalculator.class);
		print("inner: " + allInnerPos.toString(), RingConditionCalculator.class);

		BDD selfCanReachAllInnerPos = analyzer.getFactory().one();

		Logging logOrOuterPosCanReachInnerPos = Logging
				.create("ring: or opponent outerPosCanReachInnerPos");
		Logging logOrInnerPosReachableFromOut = Logging
				.create("ring: or opponent innerPosReachableFromOut");
		Logging logAndSelfCanReachAllInnerPos = Logging
				.create("ring: and opponent canReachAllInnerPos");
		// Bdd logNotSelfCanReachAllInnerPos =
		// Bdd.create("ring: not selfCanReachAllInnerPos");

		for (Position innerPos : allInnerPos) {
			BDD innerPosReachableFromOut = analyzer.getFactory().zero();
			List<Position> neighbours = Lists.newArrayList(Iterables.filter(
					innerPos.getNeighbors(), new ValidPositionFilter(board)));
			for (Position outerPos : outerPositions) {
				BDD outerPosCanReachInnerPos = analyzer.getFactory().zero();
				for (Position neighbour : neighbours) {
					if (board.isValidField(neighbour)) {
						// outerPosCanReachNeighbour = there is a path for the
						// color from
						// outerPos to neighbour
						BDD outerPosCanReachNeighbour = analyzer.getPath(
								outerPos, neighbour, color);

						// outerPosCanReachInnerPos = outerPosCanReachInnerPos
						// .orWith(outerPosCanReachNeighbour);
						outerPosCanReachInnerPos = logOrOuterPosCanReachInnerPos
								.orLog(outerPosCanReachInnerPos,
										outerPosCanReachNeighbour);
					}
				}
				// innerPosReachableFromOut = innerPosReachableFromOut
				// .orWith(outerPosCanReachInnerPos);
				innerPosReachableFromOut = logOrInnerPosReachableFromOut.orLog(
						innerPosReachableFromOut, outerPosCanReachInnerPos);
			}

			// selfCanReachAllInnerPos = selfCanReachAllInnerPos
			// .andWith(innerPosReachableFromOut);
			selfCanReachAllInnerPos = logAndSelfCanReachAllInnerPos.andLog(
					selfCanReachAllInnerPos, innerPosReachableFromOut);
		}

		result = selfCanReachAllInnerPos.not();

		logOrOuterPosCanReachInnerPos.log();
		logOrInnerPosReachableFromOut.log();
		logAndSelfCanReachAllInnerPos.log();
		analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
	}

	private Iterable<Position> filterInnerPositions(
			Collection<Position> allPos, Board board) {
		InnerFieldPredicate predicate = new InnerFieldPredicate(board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);
		return filtered;
	}

}
