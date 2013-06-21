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

	private BDDFactory fac;
	private BddCache cache;
	private Board board;

	// private int rec;
	// private int allrec = 0;

	public BoardAnalyzerHavannah(Board board) {
		this.board = board;
		initFactory(board);
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
		fac = BddFactoryProvider.getOrCreateBddFactory(board);
	}

	private BDD getPathTransitiveClosure(Position p, Position q,
			StoneColor color) {
		// int i = IntMath.log2(board.getBoardSize(), RoundingMode.HALF_UP);
		int i = 2 * board.getBoardSize() + 1;
		if (!cache.isCached(color, p, q, i)) {
			BDD path = recursiveTransitiveClosure(i, p, q, color);
			cache.store(color, p, q, i, path);
			path.free();
		}

		return cache.restore(color, p, q, i);
	}

	private BDD recursiveTransitiveClosure(int i, Position p, Position q,
			StoneColor color) {
		// rec++;
		if (i == 0) {
			if (!cache.isCached(color, p, q, i)) {
				if (p.isNeighbour(q)) {
					if (StoneColor.WHITE.equals(color)) {
						BDD bdd = getBDDForPosition(p).andWith(
								getBDDForPosition(q));
						cache.store(color, p, q, i, bdd);
						bdd.free();
					} else {
						BDD bdd = getBDDForPosition(p).not().andWith(
								getBDDForPosition(q).not());
						cache.store(color, p, q, i, bdd);
						bdd.free();
					}
				} else {
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
			} else {
				pq = cache.restore(color, p, q, i - 1);
			}

			BDD result = null;
			List<Position> neighbors = p.getNeighbors();
			List<Position> validNeighbors = Lists.newArrayList(Iterables
					.filter(neighbors, new ValidPositionFilter(board)));
			for (Position m : validNeighbors) {
				BDD pm;
				if (!cache.isCached(color, p, m, i - 1)) {
					pm = cache.store(color, p, m, i - 1,
							recursiveTransitiveClosure(i - 1, p, m, color));
				} else {
					pm = cache.restore(color, p, m, i - 1);
				}
				BDD mq;
				if (!cache.isCached(color, m, q, i - 1)) {
					mq = cache.store(color, m, q, i - 1,
							recursiveTransitiveClosure(i - 1, m, q, color));
				} else {
					mq = cache.restore(color, m, q, i - 1);
				}
				if (null == result) {
					result = pq.orWith(pm.andWith(mq));
					if (result.isOne()) {
						break;
					}
				} else {
					result = result.orWith(pm.andWith(mq));
				}
				if (result.isOne()) {
					break;
				}
			}

			cache.store(color, p, q, i, result);
			result.free();
		}

		return cache.restore(color, p, q, i);
	}

	private BDD getBDDForPosition(Position p) {
		Field field = board.getField(p.getRow(), p.getCol());
		ColorDependingBDDFieldVisitor visitor = new ColorDependingBDDFieldVisitor(
				fac);
		field.accept(visitor);
		return visitor.getBDD();
	}

}
