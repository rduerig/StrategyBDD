package com.strategy.api.board;

import java.util.Collection;

import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.util.RowConstant;

/**
 * Represents a board with fields that can be empty or contain a stone of a
 * certain color (black or white).<br>
 * Indexing is as follows (for a 4x4 board):<br>
 * 
 * <table>
 * <tr>
 * <td>(3,0)</td>
 * <td>(3,1)</td>
 * <td>(3,2)</td>
 * <td>(3,3)</td>
 * </tr>
 * <tr>
 * <td>(2,0)</td>
 * <td>(2,1)</td>
 * <td>(2,2)</td>
 * <td>(2,3)</td>
 * </tr>
 * <tr>
 * <td>(1,0)</td>
 * <td>(1,1)</td>
 * <td>(1,2)</td>
 * <td>(1,3)</td>
 * </tr>
 * <tr>
 * <td>(0,0)</td>
 * <td>(0,1)</td>
 * <td>(0,2)</td>
 * <td>(0,3)</td>
 * </tr>
 * </table>
 * 
 * @author Ralph Dürig
 */
public interface Board {

	/**
	 * Returns the field specified by its row and column.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @return the specified field
	 */
	Field getField(int row, int col);

	/**
	 * Returns the field specified by its {@link RowConstant} and the row
	 * number. Such coordinates are used on little golem for instance.
	 * 
	 * @param coord
	 *            the {@link RowConstant}
	 * @param coordNumber
	 *            the row number
	 * @return the specified field
	 */
	Field getField(RowConstant coord, Integer coordNumber);

	/**
	 * Returns the field spedified by its index when the board is represented as
	 * a two-dimensional array.
	 * 
	 * @param index
	 *            the fields index on the board when represented as
	 *            two-dimensional array
	 * @return the specified field
	 */
	Field getField(int index);

	/**
	 * Overwrites a field on the board on the same position as the given field.
	 * 
	 * @param newField
	 *            field to set
	 */
	void setField(Field newField);

	/**
	 * Returns the horizontal length of the board.
	 * 
	 * @return the horizontal length
	 */
	int getColumns();

	/**
	 * Returns the vertical length of the board.
	 * 
	 * @return the vertical length
	 */
	int getRows();

	/**
	 * Returns the size of the board. In case of a square board, it's the length
	 * of one edge. The same applies to hexagonal boards.
	 * 
	 * @return the board's size
	 */
	int getBoardSize();

	/**
	 * Returns all available positions on the board.
	 */
	Collection<Position> getPositions();

	/**
	 * Checks if there is a valid field on the board.
	 * 
	 * @param row
	 *            row of field to check
	 * @param col
	 *            column of field to check
	 * @return true if there is a field on the given row and the given column on
	 *         the current board
	 */
	boolean isValidField(int row, int col);

	/**
	 * Checks if there is a valid field on the board.
	 * 
	 * @param index
	 *            the index of the field to check
	 * @return true if there is a field with the given index on the current
	 *         board
	 */
	boolean isValidField(int index);

	/**
	 * Checks if there is a valid field on the board.
	 * 
	 * @param p
	 *            {@link Position} of the field to check
	 * @return true if there is a field on the given {@link Position} on the
	 *         current board
	 */
	boolean isValidField(Position p);

	/**
	 * Checks if there is an empty field on the board.
	 * 
	 * @param index
	 *            index of the field to check
	 * @return true if there is an empty field at the specified index
	 */
	boolean isEmptyField(int index);

	/**
	 * Transforms the board to a String representation showing each field's
	 * index
	 * 
	 * @return the index values of the fields as they are lying on the board
	 */
	String toIndexString();

	/**
	 * Transforms the board to a String representation showing each field's row
	 * and column
	 * 
	 * @return the row and column values of the fields as they are lying on the
	 *         board
	 */
	String toRowColString();

	/**
	 * Transforms the board to a String representation showing each field's
	 * rating
	 * 
	 * @return the rating of each field
	 */
	String toRatingString(double[] rating, int bestIndex);

	String toRowConstantString();

	/**
	 * Returns the same String representation of the board as
	 * {@link Board#toString()} but marks the field the latest stone was set.
	 * 
	 * @param index
	 *            field index where the latest move happened
	 * @return the board's fields
	 */
	String toMarkLastTurnString(Integer index);
}