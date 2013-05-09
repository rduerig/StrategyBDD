package com.strategy.havannah.logic.situation;

import com.google.common.base.Predicate;
import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

/**
 * Applies to corner fields of the given board. Therefore it checks if a
 * {@link Position} belongs to any of the six corners of a hexagon board.
 * 
 * @author Ralph Dürig
 * 
 */
public class CornerFieldPredicate implements Predicate<Position> {

	private Board board;

	public CornerFieldPredicate(Board board) {
		this.board = board;
	}

	@Override
	public boolean apply(Position input) {
		boolean result = false;
		for (CornerFieldCategory cat : CornerFieldCategory.values()) {
			result |= cat.contains(input, board);
		}
		return result;
	}

}
