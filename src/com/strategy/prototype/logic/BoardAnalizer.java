package com.strategy.prototype.logic;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.prototype.board.Board;

public class BoardAnalizer {

	private Map<Position, BDD> bdds;
	private int rows;
	private int cols;
	private BDDFactory fac;

	public BoardAnalizer(Board board) {
		initFactory(board);
		initBdds(board, fac);
	}

	public int getModelCount() {
		// TODO check real win situations
		Position pos = getUnseenSetPosition();
		if(null == pos){
			return 0;
		}
		
		BDD path = bdds.get(pos);

//		white.andWith(bdds[3][0].id());
//		white.andWith(bdds[2][1].id());
//		white.andWith(bdds[1][2].id());
//		white.andWith(bdds[0][3].id());
		

		Double result = path.satCount();
		return result.intValue();
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
		BoardTransformer transformer = new BoardTransformer(board, factory);
		BDD[][] bddBoard = transformer.getBDDBoard();
		rows = bddBoard.length;
		cols = bddBoard[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				bdds.put(Position.get(i, j), bddBoard[i][j]);
			}
		}
	}

	private boolean isFreeField(int row, int col) {
		return !bdds.get(Position.get(row, col)).isOne() && !bdds.get(Position.get(row, col)).isZero();
	}
	
	private boolean isInRange(Position pos) {
		boolean rowsRange = pos.getRow() >= 0 && pos.getRow() < rows;
		boolean ColsRange = pos.getCol() >= 0 && pos.getCol() < cols;

		return rowsRange && ColsRange;
	}
	
	private Position getUnseenSetPosition(){
		for (Entry<Position, BDD> entry : bdds.entrySet()) {
			if(!entry.getKey().isSeen() && entry.getValue().isOne()){
				return entry.getKey();
			}
		}
		
		return null;
	}
	
}
