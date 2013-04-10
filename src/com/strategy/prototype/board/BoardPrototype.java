package com.strategy.prototype.board;

import com.strategy.api.board.Board;
import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.WhiteStone;

/**
 * Represents a board.
 * 
 * @author Ralph Dürig
 */
public class BoardPrototype implements Board {

	private static Board instance;

	private final int rows;
	private final int columns;
	private final Field fields[][];

	// Singleton
	private BoardPrototype(int[][] board) {

		if (board.length < 1 || board[0].length < 1) {
			throw new IllegalArgumentException("Given board was empty!");
		}

		rows = board.length;
		columns = board[0].length;
		fields = new Field[rows][columns];
		init(board);
	}

	public static Board getInstance(int[][] board) {
		if (null == instance) {
			instance = new BoardPrototype(board);
		}
		return instance;
	}

	// ************************************************************************

	@Override
	public Field getField(int row, int col) {
		if (!isInRange(row, col)) {
			return null;
		}
		return fields[row][col];
	}

	@Override
	public int getColumns() {
		return columns;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sb.append(fields[i][j] + "");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	// ************************************************************************

	private void init(int[][] board) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int index = i * rows + j;
				Field field = getFieldForPrimitive(board[i][j], index);
				fields[i][j] = field;
			}
		}
	}

	private Field getFieldForPrimitive(int primitiveField, int index) {
		switch (primitiveField) {
		case 1:
			return new WhiteStone(index);
		case 2:
			return new BlackStone(index);
		default:
			return new EmptyField(index);
		}
	}

	private boolean isInRange(int r, int c) {
		boolean rowsRange = r >= 0 && r < rows;
		boolean ColsRange = c >= 0 && c < columns;

		return rowsRange && ColsRange;
	}
}
