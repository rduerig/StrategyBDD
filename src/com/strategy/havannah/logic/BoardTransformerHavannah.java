package com.strategy.havannah.logic;

import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.BDDFieldVisitor;
import com.strategy.api.logic.Position;
import com.strategy.util.AbstractBoardTransformer;
import com.strategy.util.CommonBDDFieldVisitor;

/**
 * Implementation for transforming a hexagonal Havannah board to BDDs.
 * 
 * @author Ralph Dürig
 */
public class BoardTransformerHavannah extends AbstractBoardTransformer {

	private final BDDFactory fac;
	private final Board board;

	public BoardTransformerHavannah(Board board, BDDFactory fac) {
		this.board = board;
		this.fac = fac;
	}

	@Override
	protected Position getPositionInstance(int row, int col) {
		return PositionHexagon.get(row, col);
	}

	@Override
	protected BDDFieldVisitor getBDDFieldVisitor() {
		return new CommonBDDFieldVisitor(fac);
	}

	@Override
	protected Board getBoard() {
		return board;
	}

}
