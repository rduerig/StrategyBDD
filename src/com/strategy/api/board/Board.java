package com.strategy.api.board;

import com.strategy.api.field.Field;

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

}