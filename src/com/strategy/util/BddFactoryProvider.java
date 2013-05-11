package com.strategy.util;

import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.MicroFactory;

import com.strategy.api.board.Board;

public class BddFactoryProvider {

	private static BDDFactory fac = null;

	public static BDDFactory getOrCreateBddFactory(Board board) {
		if (null == fac) {
			fac = createBddFactory(board);
		}

		return fac;
	}

	private static BDDFactory createBddFactory(Board board) {
		int dimension = board.getRows() * board.getColumns();
		BDDFactory result = MicroFactory.init(dimension * 100000,
				dimension * 100000);
		result.setVarNum(dimension);
		result.reorderVerbose(0);
		return result;
	}

}
