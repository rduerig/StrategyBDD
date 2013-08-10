package com.strategy.prototype.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.util.StoneColor;

public class BoardAnalizerPrototype implements BoardAnalyzer {

	private Map<Position, BDD> bdds;
	private int rows;
	private int cols;
	private BDDFactory fac;
	private BddCache cache;

	public BoardAnalizerPrototype(Board board) {
		initFactory(board);
		initBdds(board, fac);
		cache = new BddCachePrototype();
	}

	public BDD getPath(Position p, Position q) {
		BDD path = getPathTransitiveClosure(p, q);
		return path;
	}

	@Deprecated
	public BDD getPath(Position p, Position q, boolean forCpu) {
		BDD path = getPathTransitiveClosure(p, q);
		return path;
	}

	public void log() {
		// nothing to log here
	}

	public void done() {
		freeAll();
		fac.done();
	}

	@Override
	public BDDFactory getFactory() {
		return fac;
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
		fac = BDDFactory.init(dimension * 1000, dimension * 1000);
		fac.setVarNum(dimension);
	}

	private void initBdds(Board board, BDDFactory factory) {
		rows = board.getRows();
		cols = board.getColumns();
		BoardTransformerPrototype transformer = new BoardTransformerPrototype(
				board, factory);
		Map<Position, BDD> bddBoard = transformer.getBDDBoard();
		bdds = new HashMap<Position, BDD>(bddBoard);
	}

	private BDD getBDDCopy(Position pos) {
		BDD bdd = bdds.get(pos);
		if (null == bdd) {
			return null;
		}
		return bdd.id();
	}

	private BDD getPathTransitiveClosure(Position p, Position q) {
		int i = rows * cols - 1;
		return recursiveTransitiveClosure(i, p, q);
	}

	private BDD recursiveTransitiveClosure(int i, Position p, Position q) {
		if (i == 0) {
			if (p.isNeighbour(q)) {
				return getBDDCopy(p).andWith(getBDDCopy(q));
			} else {
				return fac.zero();
			}
		}

		Position m = PositionSquare.get(i / rows, i % rows);
		BDD pq = cache.isCached(StoneColor.WHITE, p, q, i) ? cache.restore(
				StoneColor.WHITE, p, q, i) : cache.store(StoneColor.WHITE, p,
				q, i, recursiveTransitiveClosure(i - 1, p, q));
		BDD pm = cache.isCached(StoneColor.WHITE, p, m, i) ? cache.restore(
				StoneColor.WHITE, p, m, i) : cache.store(StoneColor.WHITE, p,
				m, i, recursiveTransitiveClosure(i - 1, p, m));
		BDD mq = cache.isCached(StoneColor.WHITE, m, q, i) ? cache.restore(
				StoneColor.WHITE, m, q, i) : cache.store(StoneColor.WHITE, m,
				q, i, recursiveTransitiveClosure(i - 1, m, q));
		return pq.orWith(pm.andWith(mq));
	}

	private void freeAll() {
		for (Entry<Position, BDD> entry : bdds.entrySet()) {
			entry.getValue().free();
		}
	}

	@Override
	public BDD getPath(Position p, Position q, StoneColor color) {
		return getPath(p, q, StoneColor.WHITE);
	}

}
