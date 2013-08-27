package com.strategy.havannah.logic;

import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Collections2;
import com.google.common.math.IntMath;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.PathCalculator;
import com.strategy.api.logic.Position;
import com.strategy.util.ColorDependingBDDFieldVisitor;
import com.strategy.util.Debug;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;
import com.strategy.util.predicates.ValidPositionFilter;
import com.strategy.util.preferences.Preferences;

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

	private int pathLength;
	private int rec;

	// private Map<com.strategy.api.logic.BddCache.BddCacheIndex, Set<Position>>
	// distance;

	public PathsRec(BDDFactory fac, Board board) {
		this.fac = fac;
		this.board = board;
		cache = new BddCacheHavannah();
		// this.pathLength = IntMath.log2(
		// board.getBoardSize() * board.getBoardSize(),
		// RoundingMode.HALF_UP);

		// System.out
		// .println("all cells: " + board.getColumns() * board.getRows());
		// System.out.println("legal cells: " + board.getPositions().size());

		Collection<Position> valid = Collections2.filter(board.getPositions(),
				new ValidPositionFilter(board));
		this.pathLength = IntMath.log2(valid.size(), RoundingMode.HALF_UP);
		// this.pathLength = 1;
		this.rec = 0;
		// System.out.println("starting distance calculation");
		// distance = new HashMap<com.strategy.api.logic.BddCache.BddCacheIndex,
		// Set<Position>>();
		// for (Position p : board.getPositions()) {
		// if (!board.isValidField(p)) {
		// continue;
		// }
		// for (Position q : board.getPositions()) {
		// if (!board.isValidField(q) || p.equals(q)) {
		// continue;
		// }
		// for (int i = 0; i <= pathLength; i++) {
		// distance.put(com.strategy.api.logic.BddCache.BddCacheIndex
		// .getIndex(StoneColor.EMPTY, p, q, i),
		// getIntermediateNodes(p, q, i));
		// }
		// }
		// }
		// System.out.println("finished distance calculation");

		Debug initlog = Debug.create("recursively creating reachability");
		for (Position p : board.getPositions()) {
			// System.out.println("checking for doing for p="+p);
			if (!board.isValidField(p)) {
				continue;
			}
			// System.out.println("doing for p="+p);
			for (Position q : board.getPositions()) {
				// System.out.println("checking for doing for q="+p);
				if (!board.isValidField(q) || p.equals(q) || p.isNeighbour(q)) {
					continue;
				}
				// System.out.println("doing for q= "+q);
				getPathTransitiveClosure(p, q, StoneColor.WHITE);
				getPathTransitiveClosure(p, q, StoneColor.BLACK);
			}
		}
		initlog.log();

		// System.out.println("path length: "+pathLength);
	}

	public BDD getPath(Position p, Position q, StoneColor color) {
		// BDD path = getPathTransitiveClosure(p, q, color);
		// getPathTransitiveClosure(p, q, color.getOpposite());
		BDD path;
		if (p.equals(q)) {
			return getBDDForPosition(p);
		} else if (p.isNeighbour(q)) {
			return recursiveTransitiveClosure(0, p, q, color);
		} else {
			path = cache.restore(color, p, q, pathLength);
		}
		return path;
	}

	public void log() {
		if (null != Preferences.getInstance().getOut()) {
			Preferences.getInstance().getOut()
					.println("all recursions: " + rec);
		}
		logPandQ.log();
		logNPandNQ.log();
		logPMandMQ.log();
		logPQorPMMQ.log();
	}

	public void done() {
		cache.free();
	}

	// ************************************************************************

	private BDD getPathTransitiveClosure(Position p, Position q,
			StoneColor color) {
		// int i = IntMath.log2(board.getBoardSize()*board.getBoardSize(),
		// RoundingMode.DOWN);
		// int i = board.getBoardSize();

		// if (!cache.isCached(color, p, q, pathLength)) {
		BDD path = recursiveTransitiveClosure(pathLength, p, q, color);
		// cache.store(color, p, q, pathLength, path);
		// path.free();
		// }

		// return cache.restore(color, p, q, pathLength);

		return path;
	}

	private BDD recursiveTransitiveClosure(int i, Position p, Position q,
			StoneColor color) {
		rec++;
		if (i == 0) {
			// if (!cache.isCached(color, p, q, i)) {
			if (p.isNeighbour(q)) {
				if (StoneColor.WHITE.equals(color)) {
					// BDD bdd = getBDDForPosition(p).andWith(
					// getBDDForPosition(q));
					BDD bdd = logPandQ.andLog(getBDDForPosition(p),
							getBDDForPosition(q));
					// cache.store(color, p, q, i, bdd);
					// bdd.free();
					return bdd;
				} else {
					// BDD bdd = getBDDForPosition(p).not().andWith(
					// getBDDForPosition(q).not());
					BDD bdd = logNPandNQ.andLog(getBDDForPosition(p).not(),
							getBDDForPosition(q).not());
					// cache.store(color, p, q, i, bdd);
					// bdd.free();
					return bdd;
				}
			} else {
				return fac.zero();
			}
			// }
			// return cache.restore(color, p, q, i);
		}

		if (!cache.isCached(color, p, q, i)) {
			// BDD pq = recursiveTransitiveClosure(i-1, p, q, color);
			BDD pq;
			if (!cache.isCached(color, p, q, i - 1)) {
				pq = cache.store(color, p, q, i - 1,
						recursiveTransitiveClosure(i - 1, p, q, color));
			} else {
				pq = cache.restore(color, p, q, i - 1);
			}

			// Set<Position> special = distance
			// .get(com.strategy.api.logic.BddCache.BddCacheIndex
			// .getIndex(StoneColor.EMPTY, p, q, i - 1));
			Collection<Position> ms = board.getPositions();
			// if (null != special) {
			//
			// System.out.println("ms for p="+p+", q="+q+", dist=2^"+(i-1)+": "+ms.size());
			// System.out.println(ms.toString());
			for (Position m : ms) {
				if (!board.isValidField(m) || p.equals(q) || p.equals(m)
						|| q.equals(m)) { // || !special.contains(m)) {
					continue;
				}
				// BDD pm = recursiveTransitiveClosure(i-1, p, m, color);
				BDD pm;
				if (!cache.isCached(color, p, m, i - 1)) {
					pm = cache.store(color, p, m, i - 1,
							recursiveTransitiveClosure(i - 1, p, m, color));
				} else {
					pm = cache.restore(color, p, m, i - 1);
				}
				// BDD mq = recursiveTransitiveClosure(i-1, m, q, color);
				BDD mq;
				if (!cache.isCached(color, m, q, i - 1)) {
					mq = cache.store(color, m, q, i - 1,
							recursiveTransitiveClosure(i - 1, m, q, color));
				} else {
					mq = cache.restore(color, m, q, i - 1);
				}
				// pmAndmq = pmAndmq.orWith(pm.andWith(mq));
				pq = logPQorPMMQ.orLog(pq, logPMandMQ.andLog(pm, mq));
			}
			// }

			// return result;
			cache.store(color, p, q, i, pq);
			pq.free();
		}

		return cache.restore(color, p, q, i);
	}

	private Set<Position> getIntermediateNodes(Position p, Position q, int i) {
		// Set<Position> ps = getDistantNodes(p, Math.pow(2, i));
		// Set<Position> qs = getDistantNodes(q, Math.pow(2, i));
		Set<Position> ps = getDistantNodes(p, i);
		Set<Position> qs = getDistantNodes(q, i);
		ps.retainAll(qs);
		return ps;
	}

	private Set<Position> getDistantNodes(Position p, double dist) {
		if (dist == 0) {
			Set<Position> ns = new HashSet<Position>();
			for (Position n : p.getNeighbors()) {
				if (board.isValidField(n)) {
					ns.add(n);
				}
			}
			return ns;
		}

		Set<Position> result = new HashSet<Position>();
		for (Position n : p.getNeighbors()) {
			if (board.isValidField(n)) {
				Set<Position> nodes = getDistantNodes(n, dist - 1);
				for (Position node : nodes) {
					if (!node.equals(p) && !p.getNeighbors().contains(node)) {
						result.add(node);
					}
				}
			}
		}

		return result;
	}

	private BDD getBDDForPosition(Position p) {
		Field field = board.getField(p.getRow(), p.getCol());
		ColorDependingBDDFieldVisitor visitor = new ColorDependingBDDFieldVisitor(
				fac);
		field.accept(visitor);
		return visitor.getBDD();
	}

}
