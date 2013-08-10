package com.strategy.util;

import java.util.ArrayList;

import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;
import com.strategy.util.predicates.EmptyPositionFilter;
import com.strategy.util.predicates.ValidPositionFilter;

public class BddFactoryProvider {

	private static BDDFactory fac = null;
	private static double BASE = 5;

	public static BDDFactory getOrCreateBddFactory(Board board) {
		if (null == fac) {
			fac = createBddFactory(board);
		}

		return fac;
	}

	public static void reset() {
		if (null != fac) {
			fac.done();
		}
		fac = null;
	}

	private static BDDFactory createBddFactory(Board board) {
		// int dimension = board.getRows() * board.getColumns();
		// BDDFactory result = BDDFactory.init(dimension * 100000,
		// dimension * 100000);
		// result.setVarNum(dimension);
		// result.reorderVerbose(0);
		// result.setMaxIncrease(10000000);
		// result.setIncreaseFactor(0.5);

		/*
		 * (5 * 10^b) * b!
		 */
		int dimension = board.getRows() * board.getColumns();
		// Double size = (BASE * Math.pow(10, board.getBoardSize()))
		// * factorial(board.getBoardSize());
		// BDDFactory result = BDDFactory.init(size.intValue() / 2,
		// size.intValue() / 5);
		Iterable<Position> empty = Iterables.filter(board.getPositions(),
				new EmptyPositionFilter(board));
		Iterable<Position> validEmpty = Iterables.filter(empty,
				new ValidPositionFilter(board));
		ArrayList<Position> filtered = Lists.newArrayList(validEmpty);
		Double size = Math.pow(10, board.getBoardSize()) * filtered.size()
				* factorial(board.getBoardSize() - 2);
		BDDFactory result = BDDFactory.init(size.intValue(),
				size.intValue() / 5);
		result.setVarNum(dimension);
		result.reorderVerbose(0);
		result.setIncreaseFactor(0.1);
		// result.setMaxIncrease(Double.valueOf((size / 2) * 0.5).intValue());
		result.setMaxIncrease(0);
		result.setMinFreeNodes(0.05);

		return result;
	}

	// will only be used for arguments in range [2,10]
	private static int factorial(int x) {
		int result = 1;
		for (int i = 1; i <= x; i++) {
			result *= i;
		}

		return result;
	}

}
