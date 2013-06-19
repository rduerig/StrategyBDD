package com.strategy.util.predicates;

import com.google.common.base.Predicate;
import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

public class ValidPositionFilter implements Predicate<Position> {

	private Board b;

	public ValidPositionFilter(Board b) {
		this.b = b;
	}

	@Override
	public boolean apply(Position input) {
		if (null == input) {
			return false;
		}
		return b.isValidField(input);
	}
}
