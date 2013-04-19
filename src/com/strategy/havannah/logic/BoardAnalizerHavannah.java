package com.strategy.havannah.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;

public class BoardAnalizerHavannah implements BoardAnalyzer {

	private Map<Position, BDD> bdds;
	private int rows;
	private int cols;
	private BDDFactory fac;
	private BddCache cache;

	public BoardAnalizerHavannah(Board board) {
		initFactory(board);
		initBdds(board, fac);
		cache = new BddCacheHavannah();
	}

	public BDD getPath(Position p, Position q) {
		BDD path = getPathTransitiveClosure(p, q);
		cache = new BddCacheHavannah();
		return path;
	}

	public void done() {
		freeAll();
		fac.done();
	}

	// ************************************************************************

	private void initFactory(Board board) {
		/*
		 * Generate a BDD factory with variable numbers according to the board's
		 * size.
		 */
		int dimension = board.getRows() * board.getColumns();
		fac = BDDFactory.init(dimension * 1000, dimension * 1000);
		fac.setVarNum(dimension);
	}

	private void initBdds(Board board, BDDFactory factory) {
		rows = board.getRows();
		cols = board.getColumns();
		BoardTransformerHavannah transformer = new BoardTransformerHavannah(
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
			if (p.isNeighbour(q) && bdds.containsKey(p) && bdds.containsKey(q)) {
				return getBDDCopy(p).andWith(getBDDCopy(q));
			} else {
				return fac.zero();
			}
		}

		Position m = PositionHexagon.get(i / rows, i % cols);
		BDD pq = cache.isCached(p, q) ? cache.restore(p, q) : cache.store(p, q,
				recursiveTransitiveClosure(i - 1, p, q));
		BDD pm = cache.isCached(p, m) ? cache.restore(p, m) : cache.store(p, m,
				recursiveTransitiveClosure(i - 1, p, m));
		BDD mq = cache.isCached(m, q) ? cache.restore(m, q) : cache.store(m, q,
				recursiveTransitiveClosure(i - 1, m, q));
		return pq.orWith(pm.andWith(mq));
	}

	private void freeAll() {
		for (Entry<Position, BDD> entry : bdds.entrySet()) {
			entry.getValue().free();
		}
	}

}
