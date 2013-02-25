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
		white.andWith(bdds[0][0]);
		white.andWith(bdds[1][1]);
		white.andWith(bdds[2][2]);
		white.andWith(bdds[3][3]);

		Double result = white.satCount();
		return result.intValue();
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

}
