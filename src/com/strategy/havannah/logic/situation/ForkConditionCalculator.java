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
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;

public class ForkConditionCalculator implements ConditionCalculator,
		HasDebugFlag {

	private BDD result;
	private StoneColor color;

	public ForkConditionCalculator(BoardAnalyzer analyzer, Board board,
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
		result = analyzer.getFactory().zero();
		Collection<Position> allPos = board.getPositions();

		ArrayList<Iterable<Position>> edgePositions = Lists.newArrayList();
		for (EdgeFieldCategory cat : EdgeFieldCategory.values()) {
			edgePositions.add(filterEdgePositions(cat, allPos, board));
		}

		print(edgePositions.toString(), ForkConditionCalculator.class);

		int size = edgePositions.size();

		Logging logForkPath = Logging.create("or fork path");

		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				for (int k = j + 1; k < size; k++) {
					for (Position pos1 : edgePositions.get(i)) {
						for (Position pos2 : edgePositions.get(j)) {
							for (Position pos3 : edgePositions.get(k)) {
								print("checking: " + pos1 + " - " + pos2
										+ " - " + pos3,
										ForkConditionCalculator.class);
								BDD path = analyzer.getPath(pos1, pos2, color)
										.andWith(
												analyzer.getPath(pos2, pos3,
														color));
								// result = result.orWith(path);
								result = logForkPath.orLog(result, path);
							}
						}
					}
				}
			}
		}

		logForkPath.log();
		analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
	}

	private Iterable<Position> filterEdgePositions(EdgeFieldCategory cat,
			Collection<Position> allPos, Board board) {
		EdgeFieldPredicate predicate = new EdgeFieldPredicate(cat, board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);

		return filtered;
	}

}
