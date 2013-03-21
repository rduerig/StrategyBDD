package com.strategy.prototype.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.prototype.board.Board;

public class BoardAnalizer {

	private BDD[][] bdds;
	private BDDFactory fac;

	public BoardAnalizer(Board board) {
		initFactory(board);
		initBdds(board, fac);
	}

	public int getModelCount() {
		// TODO check real win situations
		BDD white = fac.one();

		white.andWith(bdds[3][0].id());
		white.andWith(bdds[2][1].id());
		white.andWith(bdds[1][2].id());
		white.andWith(bdds[0][3].id());

		Double result = white.satCount();
		return result.intValue();
	}

	public int[] getBestPoint() {
		int[] result = null;
		for (int i = 0; i < bdds.length; i++) {
			for (int j = 0; j < bdds[0].length; j++) {
				if (isFreeField(i, j)) {
					// set free field with white - evaluate and continue with
					// another free field
					// TODO getBestPoint
				}
			}
		}

		return result;
	}

	// ************************************************************************

	private void initFactory(Board board) {
		/*
		 * Generate a BDD factory with variable numbers according to the board's
		 * size.
		 */
		// b.getRows() * b.getColumns() -> works when we have a square board
		int dimension = board.getRows() * board.getColumns();
		// dimension * 10 because we don't yet really know how much nodes we get
		fac = BDDFactory.init(dimension * 10, dimension * 10);
		fac.setVarNum(dimension);
	}

	private void initBdds(Board board, BDDFactory factory) {
		BoardTransformer transformer = new BoardTransformer(board, factory);
		bdds = transformer.getBDDBoard();
	}

	private boolean isFreeField(int row, int col) {
		return !bdds[row][col].isOne() && !bdds[row][col].isZero();
	}

}
