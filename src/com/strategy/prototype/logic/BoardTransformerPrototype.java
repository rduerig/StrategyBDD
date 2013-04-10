package com.strategy.prototype.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardTransformer;
import com.strategy.prototype.field.BDDFieldVisitor;

public class BoardTransformerPrototype implements BoardTransformer {

	private BDDFactory fac;
	private Board board;

	public BoardTransformerPrototype(Board board, BDDFactory fac) {
		this.board = board;
		this.fac = fac;
	}

	public BDD[][] getBDDBoard() {
		/*
		 * Get a BDD matrix that represents the board: empty field -> BDD
		 * variable, white stone -> BDD true, black stone -> BDD false
		 */
		BDDFieldVisitor visitor = new BDDFieldVisitor(fac);
		BDD[][] bddBoard = new BDD[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				board.getField(i, j).accept(visitor);
				bddBoard[i][j] = visitor.getBdd();
			}
		}

		return bddBoard;
	}
}
