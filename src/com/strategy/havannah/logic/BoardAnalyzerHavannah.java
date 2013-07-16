package com.strategy.havannah.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.util.BddFactoryProvider;
import com.strategy.util.StoneColor;

public class BoardAnalyzerHavannah implements BoardAnalyzer {

	private BDDFactory fac;
	private PathCalculator paths;

	public BoardAnalyzerHavannah(Board board) {
		initFactory(board);
		paths = new PathsIter(fac, board);
	}

	public BDD getPath(Position p, Position q, StoneColor color) {
		BDD path = paths.getPath(p, q, color);

		return path;
	}

	public void done() {
		paths.done();
	}

	@Override
	public BDDFactory getFactory() {
		return fac;
	}

	// ************************************************************************

	private void initFactory(Board board) {
		fac = BddFactoryProvider.getOrCreateBddFactory(board);
	}

	// ************************************************************************

	protected interface PathCalculator {
		BDD getPath(Position p, Position q, StoneColor color);

		void done();
	}

}
