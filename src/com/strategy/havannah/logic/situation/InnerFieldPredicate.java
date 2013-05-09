package com.strategy.havannah.logic.situation;

import com.google.common.base.Predicate;
import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

/**
 * Applies for all inner fields of the given board, i.e. fields that haven
 * neighbors in all directions.
 * 
 * @author Ralph Dürig
 * 
 */
public class InnerFieldPredicate implements Predicate<Position> {

	private Board board;

	public InnerFieldPredicate(Board board) {
		this.board = board;
	}

	@Override
	public boolean apply(Position input) {
		return board.isValidField(input.getSouth())
				&& board.isValidField(input.getSouthWest())
				&& board.isValidField(input.getNorthWest())
				&& board.isValidField(input.getNorth())
				&& board.isValidField(input.getNorthEast())
				&& board.isValidField(input.getSouthEast());
	}

}
