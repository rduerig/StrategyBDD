package com.strategy.prototype.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.prototype.board.Board;
import com.strategy.prototype.field.BlackStone;
import com.strategy.prototype.field.WhiteStone;

public class BoardTransformer {

	private BDDFactory fac;
	private Board board;

	public BoardTransformer(Board board, BDDFactory fac) {
		this.board = board;
		this.fac = fac;
	}

	public BDD[][] getBDDBoard() {
		/*
		 * Get a BDD matrix that represents the board: empty field -> BDD
		 * variable, white stone -> BDD true, black stone -> BDD false
		 */
		BDD[][] bddBoard = new BDD[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				if (board.getField(i, j) instanceof WhiteStone) {
					// System.out.println("found white on: " + i + " - " + j);
					bddBoard[i][j] = fac.one();
				} else if (board.getField(i, j) instanceof BlackStone) {
					bddBoard[i][j] = fac.zero();
				} else {
					bddBoard[i][j] = fac.ithVar(i * board.getRows() + j);
				}
			}
		}

		return bddBoard;
	}

}
