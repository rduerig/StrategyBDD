package com.strategy.api.field;

import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

/**
 * Represents a field on the {@link Board}.
 * 
 * @author Ralph Dürig
 */
public interface Field {

	/**
	 * Accepts a visitor to handle different field types.
	 * 
	 * @param visitor
	 *            an instance of {@link FieldVisitor}
	 */
	void accept(FieldVisitor visitor);

	/**
	 * Returns the field's position on the board.
	 */
	Position getPosition();

	/**
	 * Returns an internal index for the field that was set on board
	 * transformation.
	 */
	Integer getIndex();

}
