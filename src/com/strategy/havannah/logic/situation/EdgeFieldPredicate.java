package com.strategy.havannah.logic.situation;

import com.google.common.base.Predicate;
import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

/**
 * @author Ralph Dürig
 */
public class EdgeFieldPredicate implements Predicate<Position> {

	private EdgeFieldCategory cat;
	private Board board;

	public EdgeFieldPredicate(EdgeFieldCategory cat, Board board) {
		this.cat = cat;
		this.board = board;
	}

	@Override
	public boolean apply(Position input) {
		return cat.contains(input, board);
	}

}
