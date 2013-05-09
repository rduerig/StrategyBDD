package com.strategy.util;

import com.google.common.base.Predicate;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;

public class EmptyPositionFilter implements Predicate<Position> {

	private Board board;
	private ColorFieldVisitor colorVisitor;

	public EmptyPositionFilter(Board board) {
		this.board = board;
		this.colorVisitor = new ColorFieldVisitor();
	}

	@Override
	public boolean apply(Position input) {
		Field field = board.getField(input.getRow(), input.getCol());
		field.accept(colorVisitor);
		return StoneColor.EMPTY.equals(colorVisitor.getColor());
	}
}