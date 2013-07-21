package com.strategy.util;

import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;

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
		Double size = (BASE * Math.pow(10, board.getBoardSize()))
				* factorial(board.getBoardSize());
		BDDFactory result = BDDFactory.init(size.intValue(),
				size.intValue() * 10);
		result.setVarNum(dimension);
		result.reorderVerbose(0);
		result.setIncreaseFactor(0.5);

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
