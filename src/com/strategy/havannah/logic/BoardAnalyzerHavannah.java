package com.strategy.havannah.logic;

import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.util.BddFactoryProvider;
import com.strategy.util.ColorDependingBDDFieldVisitor;
import com.strategy.util.StoneColor;
import com.strategy.util.predicates.ValidPositionFilter;

public class BoardAnalyzerHavannah implements BoardAnalyzer {

	private int rows;
	private int cols;
	private BDDFactory fac;
	private BddCache cache;
	private Board board;

	// private int rec;
	// private int allrec = 0;

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
		// rec = 0;
		BDD path = getPathTransitiveClosure(p, q, color);
		// calculating for opposite color - just fills the cache
		getPathTransitiveClosure(p, q, color.getOpposite());
		// System.out.println("recursions from " + p + " to " + q + ": " + rec);
		// allrec += rec;
		// fac.reorder(BDDFactory.REORDER_SIFT);
		return path;
	}

	public void done() {
		// fac.done();
		// System.out.println("all recursions: " + allrec);
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
		// int i = rows * cols - 1;
		// int i = IntMath.log2(board.getBoardSize(), RoundingMode.HALF_UP);
		int i = board.getBoardSize() * board.getBoardSize();
		if (!cache.isCached(color, p, q, i)) {
			BDD path = recursiveTransitiveClosure(i, p, q, color);
			cache.store(color, p, q, i, path.id());
			cache.store(color, q, p, i, path.id());
			path.free();
		}

		return cache.restore(color, p, q, i);
	}

	private BDD recursiveTransitiveClosure(int i, Position p, Position q,
			StoneColor color) {
		// rec++;
		if (i == 0) {
			if (!cache.isCached(color, p, q, i)) {
				if (p.isNeighbour(q) && board.isValidField(p)
						&& board.isValidField(q)) {
					BDD bdd = getBDDForPosition(p, color).andWith(
							getBDDForPosition(q, color));
					cache.store(color, p, q, i, bdd.id());
					cache.store(color, q, p, i, bdd.id());
					bdd.free();
				} else {
					// cache.store(color, p, q, i, fac.zero());
					// cache.store(color, q, p, i, fac.zero());
					return fac.zero();
				}
			}
			return cache.restore(color, p, q, i);
		}

		if (!cache.isCached(color, p, q, i)) {
			BDD pq;
			if (!cache.isCached(color, p, q, i - 1)) {
				pq = cache.store(color, p, q, i - 1,
						recursiveTransitiveClosure(i - 1, p, q, color));
				cache.store(color, q, p, i - 1, pq.id());
			} else {
				pq = cache.restore(color, p, q, i - 1);
			}

			// Position m = getValidIntermediatePosition(i);
			BDD existsMi = fac.zero();
			List<Position> neighbors = p.getNeighbors();
			List<Position> validNeighbors = Lists.newArrayList(Iterables
					.filter(neighbors, new ValidPositionFilter(board)));
			for (Position m : validNeighbors) {
				BDD pm;
				if (!cache.isCached(color, p, m, i - 1)) {
					pm = cache.store(color, p, m, i - 1,
							recursiveTransitiveClosure(i - 1, p, m, color));
					cache.store(color, m, p, i - 1, pm.id());
				} else {
					pm = cache.restore(color, p, m, i - 1);
				}
				BDD mq;
				if (!cache.isCached(color, m, q, i - 1)) {
					mq = cache.store(color, m, q, i - 1,
							recursiveTransitiveClosure(i - 1, m, q, color));
					cache.store(color, q, m, i - 1, mq.id());
				} else {
					mq = cache.restore(color, m, q, i - 1);
				}
				BDD pmandmq = pm.andWith(mq);
				existsMi = existsMi.orWith(pmandmq);
			}

			// BDD result = pq.orWith(pmandmq);
			BDD result = pq.orWith(existsMi);
			cache.store(color, p, q, i, result.id());
			cache.store(color, q, p, i, result.id());
			result.free();
		}

		return cache.restore(color, p, q, i);
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
