package com.strategy.api.field;

import com.strategy.api.board.Board;

/**
 * Represents a field on the {@link Board}.
 * 
 * @author Ralph Dürig
 */
public interface Field {

	void accept(FieldVisitor visitor);

	Integer getIndex();

}
