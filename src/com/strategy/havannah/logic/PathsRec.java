package com.strategy.havannah.logic;

import java.math.RoundingMode;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.math.IntMath;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.PathCalculator;
import com.strategy.api.logic.Position;
import com.strategy.util.ColorDependingBDDFieldVisitor;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;

/**
 * @author Ralph Dürig
 */
public class PathsRec implements PathCalculator {
	private BDDFactory fac;
	private BddCache cache;
	private Board board;

	private Logging logPandQ = Logging.create("p and q");
	private Logging logNPandNQ = Logging.create("not p and not q");
	private Logging logPMandMQ = Logging.create("pm and mq");
	private Logging logPQorPMMQ = Logging.create("pq or (pm and mq)");
	private Logging logPMMQ = Logging.create("or pm and mq");

	public PathsRec(BDDFactory fac, Board board) {
		this.fac = fac;
		this.board = board;
		cache = new BddCacheHavannah();
	}

	public BDD getPath(Position p, Position q, StoneColor color) {
		BDD path = getPathTransitiveClosure(p, q, color);
		getPathTransitiveClosure(p, q, color.getOpposite());
		return path;
	}

	public void done() {
		// fac.done();
		// System.out.println("all recursions: " + allrec);
		logPandQ.log();
		logNPandNQ.log();
		logPMandMQ.log();
		logPQorPMMQ.log();
		logPMMQ.log();
		cache.free();
	}

	// ************************************************************************

	private BDD getPathTransitiveClosure(Position p, Position q,
			StoneColor color) {
		int i = IntMath.log2(board.getBoardSize(), RoundingMode.HALF_UP);
		// int i = 2 * board.getBoardSize() + 1;
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
						// BDD bdd = getBDDForPosition(p).andWith(
						// getBDDForPosition(q));
						BDD bdd = logPandQ.andLog(getBDDForPosition(p),
								getBDDForPosition(q));
						cache.store(color, p, q, i, bdd);
						bdd.free();
					} else {
						// BDD bdd = getBDDForPosition(p).not().andWith(
						// getBDDForPosition(q).not());
						BDD bdd = logNPandNQ.andLog(getBDDForPosition(p).not(),
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

			BDD pmAndmq = fac.zero();
			for (Position m : board.getPositions()) {
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
				// pmAndmq = pmAndmq.orWith(pm.andWith(mq));
				pmAndmq = logPMMQ.orLog(pmAndmq, logPMandMQ.andLog(pm, mq));
			}

			BDD result = logPQorPMMQ.orLog(pq, pmAndmq);

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
