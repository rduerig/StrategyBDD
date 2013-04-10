package com.strategy.api.field;

public interface FieldVisitor {

	void visit(EmptyField field);

	void visit(WhiteStone field);

	void visit(BlackStone field);

}
