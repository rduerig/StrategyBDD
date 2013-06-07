package com.strategy.havannah.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.util.BddFactoryProvider;
import com.strategy.util.ColorDependingBDDFieldVisitor;
import com.strategy.util.StoneColor;

public class BoardAnalyzerHavannah implements BoardAnalyzer {

	private int rows;
	private int cols;
	private BDDFactory fac;
	private BddCache cache;
	private Board board;

	public BoardAnalyzerHavannah(Board board) {
		this.board = board;
		initFactory(board);
		rows = board.getRows();
		cols = board.getColumns();
		cache = new BddCacheHavannah();
	}

	public BDD getPath(Position p, Position q, StoneColor color) {
		// cache = new BddCacheHavannah();
		// cache.free();
		BDD path = getPathTransitiveClosure(p, q, color);
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

	// ************************************************************************

	private void initFactory(Board board) {
		// /*
		// * Generate a BDD factory with variable numbers according to the
		// board's
		// * size.
		// */
		// int dimension = board.getRows() * board.getColumns();
		// fac = MicroFactory.init(dimension * 100000, dimension * 100000);
		// fac.setVarNum(dimension);
		// fac.reorderVerbose(0);

		fac = BddFactoryProvider.getOrCreateBddFactory(board);
	}

	private BDD getPathTransitiveClosure(Position p, Position q,
			StoneColor color) {
		int i = rows * cols - 1;
		return recursiveTransitiveClosure(i, p, q, color);
	}

	// private BDD recursiveTransitiveClosure(int i, Position p, Position q,
	// StoneColor color) {
	// if (i == 0) {
	// if (p.isNeighbour(q) && board.isValidField(p)
	// && board.isValidField(q)) {
	// return getBDDForPosition(p, color).andWith(
	// getBDDForPosition(q, color));
	// } else {
	// return fac.zero();
	// }
	// }
	//
	// Position m = getValidIntermediatePosition(i);
	//
	// BDD pq = cache.isCached(color, p, q, i) ? cache.restore(color, p, q, i)
	// : cache.store(color, p, q, i,
	// recursiveTransitiveClosure(i - 1, p, q, color));
	// BDD pm = cache.isCached(color, p, m, i) ? cache.restore(color, p, m, i)
	// : cache.store(color, p, m, i,
	// recursiveTransitiveClosure(i - 1, p, m, color));
	// BDD mq = cache.isCached(color, m, q, i) ? cache.restore(color, m, q, i)
	// : cache.store(color, m, q, i,
	// recursiveTransitiveClosure(i - 1, m, q, color));
	// BDD pmandmq = pm.andWith(mq);
	// return pq.orWith(pmandmq);
	// }

	private BDD recursiveTransitiveClosure(int i, Position p, Position q,
			StoneColor color) {
		if (i == 0) {
			if (p.isNeighbour(q) && board.isValidField(p)
					&& board.isValidField(q)) {
				return getBDDForPosition(p, color).andWith(
						getBDDForPosition(q, color));
			} else {
				return fac.zero();
			}
		}

		if (cache.isCached(color, p, q, i)) {
			return cache.restore(color, p, q, i);
		} else {
			Position m = getValidIntermediatePosition(i);
			BDD pq = cache.isCached(color, p, q, i - 1) ? cache.restore(color,
					p, q, i - 1) : cache.store(color, p, q, i - 1,
					recursiveTransitiveClosure(i - 1, p, q, color));
			BDD pm = cache.isCached(color, p, m, i - 1) ? cache.restore(color,
					p, m, i - 1) : cache.store(color, p, m, i - 1,
					recursiveTransitiveClosure(i - 1, p, m, color));
			BDD mq = cache.isCached(color, m, q, i - 1) ? cache.restore(color,
					m, q, i - 1) : cache.store(color, m, q, i - 1,
					recursiveTransitiveClosure(i - 1, m, q, color));
			BDD pmandmq = pm.andWith(mq);
			BDD result = pq.orWith(pmandmq);
			cache.store(color, p, q, i, result.id());
			return result;
		}
	}

	private Position getValidIntermediatePosition(int i) {
		Position m;
		do {
			m = PositionHexagon.get(i / rows, i % cols);
			if (!board.isValidField(m)) {
				i = i - 1;
			}
		} while (!board.isValidField(m) && i > 0);

		return m;
	}

	private BDD getBDDForPosition(Position p, StoneColor color) {
		Field field = board.getField(p.getRow(), p.getCol());
		if (null == field) {
			return fac.zero();
		}
		ColorDependingBDDFieldVisitor visitor = new ColorDependingBDDFieldVisitor(
				fac, color);
		field.accept(visitor);
		return visitor.getBDD();
	}

}
