package com.strategy.havannah.logic;

import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.strategy.api.board.Board;
import com.strategy.api.logic.PathCalculator;
import com.strategy.api.logic.Position;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;
import com.strategy.util.predicates.ValidPositionFilter;

/**
 * @author Ralph Dürig
 */
public class PathsIter implements PathCalculator {
	private BDDFactory fac;
	private Board board;

	private Logging logPandQ = Logging.create("p and q");
	private Logging logNPandNQ = Logging.create("not p and not q");
	private Logging logPMandMQ = Logging.create("pm and mq");
	private Logging logPQorPMMQ = Logging.create("pq or (pm and mq)");
	private BDD[][] reachWhite;
	private BDD[][] reachBlack;

	/**
	 * 
	 */
	public PathsIter(BDDFactory fac, Board board) {
		this.fac = fac;
		this.board = board;
		initReachability();
	}

	public BDD getPath(Position p, Position q, StoneColor color) {
		Integer indexP = board.getField(p.getRow(), p.getCol()).getIndex();
		Integer indexQ = board.getField(q.getRow(), q.getCol()).getIndex();
		BDD path;
		if (color.equals(StoneColor.WHITE)) {
			path = reachWhite[indexP][indexQ].id();
		} else {
			path = reachBlack[indexP][indexQ].id();
		}
		return path;

		//int V = board.getColumns() * board.getRows();
		//for (int k = 0; k < V; k++) {
		//	if (color.equals(StoneColor.WHITE)) {
		//		doReach(reachWhite, k, indexP, indexQ, board);
		//	} else {
		//		doReach(reachBlack, k, indexP, indexQ, board);
		//	}
		//}

		//BDD result;
		//if (color.equals(StoneColor.WHITE)) {
		//	result = reachWhite[indexP][indexQ];
		//} else {
		//	result = reachBlack[indexP][indexQ];
		//}
		//return result;
	}

	public void done() {
		// fac.done();
		// System.out.println("all recursions: " + allrec);
		logPandQ.log();
		logNPandNQ.log();
		logPMandMQ.log();
		logPQorPMMQ.log();

		int V = board.getColumns() * board.getRows();
		freeMatrix(V, reachWhite);
		freeMatrix(V, reachBlack);
	}

	// ************************************************************************

	private void initReachability() {
		// number of vertices
		int V = board.getColumns() * board.getRows();

		// adjacency matrix with bdd style
		// BDD[][] graphWhite = new BDD[V][V];
		// BDD[][] graphBlack = new BDD[V][V];
		reachWhite = new BDD[V][V];
		reachBlack = new BDD[V][V];
		for (Position pos : board.getPositions()) {
			int indexP = board.getField(pos.getRow(), pos.getCol()).getIndex();
			List<Position> neighbors = pos.getNeighbors();
			Iterable<Position> filtered = Iterables.filter(neighbors,
					new ValidPositionFilter(board));
			for (Position n : filtered) {
				int indexN = board.getField(n.getRow(), n.getCol()).getIndex();
				if (null == reachWhite[indexP][indexN]) {
					reachWhite[indexP][indexN] = fac.zero();
				}
				if (null == reachBlack[indexP][indexN]) {
					reachBlack[indexP][indexN] = fac.zero();
				}
				reachWhite[indexP][indexN] = reachWhite[indexP][indexN]
						.orWith(logPandQ.andLog(fac.ithVar(indexP),
								fac.ithVar(indexN)));
				reachBlack[indexP][indexN] = reachBlack[indexP][indexN]
						.orWith(logNPandNQ.andLog(fac.nithVar(indexP),
								fac.nithVar(indexN)));
			}
		}

//		doFill(reachWhite, graphWhite, V);
//		doFill(reachBlack, graphBlack, V);

//		reachability matrix with floyd-warshall-algorithm and bdd style also
			for (int k = 0; k < V; k++) {
				// Pick all vertices as source one by one
				for (int i = 0; i < V; i++) {
					// Pick all vertices as destination for the
					// above picked source
					for (int j = 0; j < V; j++) {
						// If vertex k is on a path from i to j,
						// then make sure that the value of reach[i][j] is 1

						doReach(reachWhite, k, i, j, board);
						doReach(reachBlack, k, i, j, board);

					}
				}
			}
	}

	private void doReach(BDD[][] todo, int m, int p, int q, Board b) {
		BDD reachpq;
		if (!b.isValidField(p) || !b.isValidField(q) || null == todo[p][q]) {
			reachpq = fac.zero();
		} else {
			reachpq = todo[p][q];
		}
		BDD reachpm;
		if (!b.isValidField(p) || !b.isValidField(m) || null == todo[p][m]) {
			reachpm = fac.zero();
		} else {
			reachpm = todo[p][m];
		}
		BDD reachmq;
		if (!b.isValidField(m) || !b.isValidField(q) || null == todo[m][q]) {
			reachmq = fac.zero();
		} else {
			reachmq = todo[m][q];
		}
		// todo[p][q] = reachpq.or(reachpm.and(reachmq));
		todo[p][q] = logPQorPMMQ.orLog(reachpq.id(),
				logPMandMQ.andLog(reachpm.id(), reachmq.id()));
	}

	private void freeMatrix(int V, BDD[][] reach) {
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				reach[i][j].free();
			}
		}

	}

	private void printMatrix(int V, BDD[][] reach) {
		System.out
			.println("Following matrix is transitive closure of the given graph");
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++)
				System.out.print(reach[i][j]);
			System.out.println();
		}

	}

}
