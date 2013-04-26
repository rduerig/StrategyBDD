package com.strategy.havannah.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.util.WhiteBDDFieldVisitor;

public class BoardAnalyzerHavannah implements BoardAnalyzer {

	private Map<Position, BDD> bdds;
	private int rows;
	private int cols;
	private BDDFactory fac;
	private BddCache cache;
	private Board board;
	private WhiteBDDFieldVisitor visitor;

	public BoardAnalyzerHavannah(Board board) {
		this.board = board;
		initFactory(board);
		this.visitor = new WhiteBDDFieldVisitor(fac);
		initBdds(board, fac);
		cache = new BddCacheHavannah();
	}

	public BDD getPath(Position p, Position q) {
		BDD path = getPathTransitiveClosure(p, q);
		return path;
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
		int dimension = board.getRows() * board.getColumns();
		fac = BDDFactory.init(dimension * 500000, dimension * 100000);
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

	private BDD getPathTransitiveClosure(Position p, Position q) {
		int i = rows * cols - 1;
		return recursiveTransitiveClosure(i, p, q);
	}

	private BDD recursiveTransitiveClosure(int i, Position p, Position q) {
		if (i == 0) {
			if (p.isNeighbour(q) && bdds.containsKey(p) && bdds.containsKey(q)) {
				return getBDDForPosition(p).andWith(getBDDForPosition(q));
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
		BDD pmandmq = pm.andWith(mq);
		return pq.orWith(pmandmq);
	}

	private void freeAll() {
		for (Entry<Position, BDD> entry : bdds.entrySet()) {
			entry.getValue().free();
		}
	}

	private BDD getBDDForPosition(Position p) {
		Field field = board.getField(p.getRow(), p.getCol());
		field.accept(visitor);
		return visitor.getBDD();
	}

}
