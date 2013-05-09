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

public class ForkConditionCalculator implements ConditionCalculator {

	private static boolean debug = false;

	private BDD result;

	public static void setDebug(boolean debug) {
		ForkConditionCalculator.debug = debug;
	}

	public ForkConditionCalculator(BoardAnalyzer analyzer, Board board) {
		calculateResult(analyzer, board);
	}

	@Override
	public BDD getBdd() {
		return result;
	}

	// ************************************************************************

	private void calculateResult(BoardAnalyzer analyzer, Board board) {
		result = analyzer.getFactory().zero();
		Collection<Position> allPos = board.getPositions();

		ArrayList<Iterable<Position>> edgePositions = Lists.newArrayList();
		for (EdgeFieldCategory cat : EdgeFieldCategory.values()) {
			edgePositions.add(filterEdgePositions(cat, allPos, board));
		}

		print(edgePositions.toString(), debug);

		int size = edgePositions.size();

		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				for (int k = j + 1; k < size; k++) {
					for (Position pos1 : edgePositions.get(i)) {
						for (Position pos2 : edgePositions.get(j)) {
							for (Position pos3 : edgePositions.get(k)) {
								print("checking: " + pos1 + " - " + pos2
										+ " - " + pos3, debug);
								BDD path = analyzer.getPath(pos1, pos2)
										.andWith(analyzer.getPath(pos2, pos3));
								result = result.id().orWith(path);
							}
						}
					}
				}
			}
		}

		analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
	}

	private Iterable<Position> filterEdgePositions(EdgeFieldCategory cat,
			Collection<Position> allPos, Board board) {
		EdgeFieldPredicate predicate = new EdgeFieldPredicate(cat, board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);

		return filtered;
	}

}
