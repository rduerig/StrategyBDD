/**
 * 
 */
package com.strategy.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.BDDFieldVisitor;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardTransformer;
import com.strategy.api.logic.Position;

/**
 * Transforms a given {@link Board} to a map between {@link Position}s and
 * {@link BDD}s using a given {@link BDDFactory}.<br>
 * 
 * @author Ralph Dürig
 */
public abstract class AbstractBoardTransformer implements BoardTransformer {

	/**
	 * Returns an instance of a {@link Position} according to the board's type.
	 * 
	 * @param row
	 *            the row the resulting position should have
	 * @param col
	 *            the column the resulting position should have
	 */
	protected abstract Position getPositionInstance(int row, int col);

	/**
	 * Returns a {@link BDDFieldVisitor} according to the board's type.
	 */
	protected abstract BDDFieldVisitor getBDDFieldVisitor();

	/**
	 * Returns a {@link Board}.
	 */
	protected abstract Board getBoard();

	/**
	 * Get a BDD map that represents the board: empty field -> BDD variable,
	 * white stone -> BDD true, black stone -> BDD false
	 */
	public Map<Position, BDD> getBDDBoard() {
		BDDFieldVisitor visitor = getBDDFieldVisitor();
		Board board = getBoard();
		Map<Position, BDD> bddBoard = new HashMap<Position, BDD>();
		for (Position pos : board.getPositions()) {
			int i = pos.getRow();
			int j = pos.getCol();
			Field field = board.getField(i, j);
			if (null != field) {
				field.accept(visitor);
				bddBoard.put(getPositionInstance(i, j), visitor.getBDD());
			}
		}

		return bddBoard;
	}

}
