package com.strategy.prototype.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;

public class BoardAnalizerPrototype implements BoardAnalyzer {

	private Map<Position, BDD> bdds;
	private int rows;
	private int cols;
	private BDDFactory fac;

	public BoardAnalizerPrototype(Board board) {
		initFactory(board);
		initBdds(board, fac);
	}

	public int getModelCount() {
		// TODO check real win situations
		Position p = getUnseenSetPosition();
		if (null == p) {
			return 0;
		}

		// TODO check with dynamic position, here we use a fixed position for
		// testing
		// corner bottom left
		p = PositionSquare.get(3, 0);

		// corner top right
		Position q = PositionSquare.get(0, 3);
		// build the formula to check if there is a path from p to q
		BDD path = getPathTransitiveClosure(p, q);
		BDD someResult = path.satOne();
		someResult.printSet();
		List allsat = path.allsat();

		int result = allsat.size();
		path.free();
		someResult.free();
		return result;
	}

	public int[] getBestPoint() {
		int[] result = null;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (isFreeField(i, j)) {
					// set free field with white - evaluate and continue with
					// another free field
					// TODO getBestPoint
				}
			}
		}

		return result;
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
		// b.getRows() * b.getColumns() -> works when we have a square board
		int dimension = board.getRows() * board.getColumns();
		// dimension * 10 because we don't yet really know how much nodes we get
		fac = BDDFactory.init(dimension * 10, dimension * 10);
		fac.setVarNum(dimension);
	}

	private void initBdds(Board board, BDDFactory factory) {
		BoardTransformerPrototype transformer = new BoardTransformerPrototype(
				board, factory);
		BDD[][] bddBoard = transformer.getBDDBoard();
		rows = bddBoard.length;
		cols = bddBoard[0].length;
		bdds = new HashMap<Position, BDD>(rows * cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				bdds.put(PositionSquare.get(i, j), bddBoard[i][j]);
			}
		}
	}

	private boolean isFreeField(int row, int col) {
		return !bdds.get(PositionSquare.get(row, col)).isOne()
				&& !bdds.get(PositionSquare.get(row, col)).isZero();
	}

	private Position getUnseenSetPosition() {
		for (Entry<Position, BDD> entry : bdds.entrySet()) {
			if (!entry.getKey().isSeen() && entry.getValue().isOne()) {
				entry.getKey().setVisited();
				return entry.getKey();
			}
		}

		return null;
	}

	private BDD getBDDCopy(Position pos) {
		return bdds.get(pos).id();
	}

	private BDD getPathTransitiveClosure(Position p, Position q) {
		return recursiveTransitiveClosure(rows * cols - 1, p, q);
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
		return recursiveTransitiveClosure(i - 1, p, q).orWith(
				recursiveTransitiveClosure(i - 1, p, m).andWith(
						recursiveTransitiveClosure(i - 1, m, q)));
	}

	private void freeAll() {
		for (Entry<Position, BDD> entry : bdds.entrySet()) {
			entry.getValue().free();
		}
	}

}