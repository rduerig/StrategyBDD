package com.strategy.prototype.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.BDDFieldVisitor;
import com.strategy.api.logic.Position;
import com.strategy.util.AbstractBoardTransformer;
import com.strategy.util.ColorDependingBDDFieldVisitor;

/**
 * Transforms a given {@link Board} to a map between {@link Position}s and
 * {@link BDD}s using a given {@link BDDFactory}.<br>
 * 
 * @author Ralph Dürig
 */
public class BoardTransformerPrototype extends AbstractBoardTransformer {

	private BDDFactory fac;
	private Board board;

	public BoardTransformerPrototype(Board board, BDDFactory fac) {
		this.board = board;
		this.fac = fac;
	}

	@Override
	protected Position getPositionInstance(int row, int col) {
		return PositionSquare.get(row, col);
	}

	@Override
	protected BDDFieldVisitor getBDDFieldVisitor() {
		return new ColorDependingBDDFieldVisitor(fac);
	}

	@Override
	protected Board getBoard() {
		return board;
	}
}
