package com.strategy.havannah.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.MicroFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.BDDFieldVisitor;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.util.BlackBDDFieldVisitor;
import com.strategy.util.StoneColor;
import com.strategy.util.WhiteBDDFieldVisitor;

public class BoardAnalyzerHavannah implements BoardAnalyzer {

	private int rows;
	private int cols;
	private BDDFactory fac;
	private BddCache cache;
	private Board board;
	private BDDFieldVisitor visitor;
	private StoneColor color;

	public BoardAnalyzerHavannah(Board board, StoneColor color) {
		this.board = board;
		initFactory(board);
		this.color = color;
		initVisitor(color, fac);
		initBdds(board, fac);
		cache = new BddCacheHavannah();
	}

	public BDD getPath(Position p, Position q) {
		// cache = new BddCacheHavannah();
		// cache.free();
		BDD path = getPathTransitiveClosure(p, q);
		// fac.reorder(BDDFactory.REORDER_SIFT);
		return path;
	}

	public void done() {
		// fac.done();
		cache.free();
	}

	@Override
	public BDDFactory getFactory() {
		return fac;
	}

	@Override
	public StoneColor getStoneColor() {
		return color;
	}

	// ************************************************************************

	private void initFactory(Board board) {
		/*
		 * Generate a BDD factory with variable numbers according to the board's
		 * size.
		 */
		int dimension = board.getRows() * board.getColumns();
		fac = MicroFactory.init(dimension * 100000, dimension * 100000);
		fac.setVarNum(dimension);
		fac.reorderVerbose(0);
	}

	private void initBdds(Board board, BDDFactory factory) {
		rows = board.getRows();
		cols = board.getColumns();
	}

	private void initVisitor(StoneColor color, BDDFactory factory) {
		if (StoneColor.BLACK.equals(color)) {
			visitor = new BlackBDDFieldVisitor(factory);
		} else {
			visitor = new WhiteBDDFieldVisitor(factory);
		}
	}

	private BDD getPathTransitiveClosure(Position p, Position q) {
		int i = rows * cols - 1;
		return recursiveTransitiveClosure(i, p, q);
	}

	private BDD recursiveTransitiveClosure(int i, Position p, Position q) {
		if (i == 0) {
			if (p.isNeighbour(q) && board.isValidField(p)
					&& board.isValidField(q)) {
				return getBDDForPosition(p).andWith(getBDDForPosition(q));
			} else {
				return fac.zero();
			}
		}

		Position m = PositionHexagon.get(i / rows, i % cols);
		BDD pq = cache.isCached(p, q, i) ? cache.restore(p, q, i) : cache
				.store(p, q, i, recursiveTransitiveClosure(i - 1, p, q));
		BDD pm = cache.isCached(p, m, i) ? cache.restore(p, m, i) : cache
				.store(p, m, i, recursiveTransitiveClosure(i - 1, p, m));
		BDD mq = cache.isCached(m, q, i) ? cache.restore(m, q, i) : cache
				.store(m, q, i, recursiveTransitiveClosure(i - 1, m, q));
		BDD pmandmq = pm.andWith(mq);
		return pq.orWith(pmandmq);
	}

	private BDD getBDDForPosition(Position p) {
		Field field = board.getField(p.getRow(), p.getCol());
		field.accept(visitor);
		return visitor.getBDD();
	}

}
