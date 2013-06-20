package com.strategy.util;

import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;

public class BddFactoryProvider {

	private static BDDFactory fac = null;

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
		int dimension = board.getRows() * board.getColumns();
		BDDFactory result = BDDFactory.init(dimension * 100000,
				dimension * 100000);
		// BDDFactory result = MicroFactory.init(dimension * 200000,
		// dimension * 200000);
		result.setVarNum(dimension);
		result.reorderVerbose(0);
		result.setMaxIncrease(10000000);
		result.setIncreaseFactor(0.5);
		// result.setMaxNodeNum(10000000);
		return result;
	}

}
