package com.strategy.havannah.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.PathCalculator;
import com.strategy.api.logic.Position;
import com.strategy.util.BddFactoryProvider;
import com.strategy.util.StoneColor;
import com.strategy.util.preferences.Preferences;

public class BoardAnalyzerHavannah implements BoardAnalyzer {

	private BDDFactory fac;
	private Board board;
	private PathCalculator paths;

	public BoardAnalyzerHavannah(Board board) {
		this.board = board;
		initFactory(board);
	}

	public BDD getPath(Position p, Position q, StoneColor color) {
		if (null == paths) {
			paths = Preferences.getInstance().getAlg().provide(fac, board);
		}
		BDD path = paths.getPath(p, q, color);
		return path;
	}

	public void log() {
		if (null != paths) {
			paths.log();
		}
	}

	public void done() {
		if (null != paths) {
			paths.done();
		}
	}

	@Override
	public BDDFactory getFactory() {
		return fac;
	}

	// ************************************************************************

	private void initFactory(Board board) {
		fac = BddFactoryProvider.getOrCreateBddFactory(board);
	}

}
