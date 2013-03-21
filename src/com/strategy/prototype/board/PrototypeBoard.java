package com.strategy.prototype.board;

import com.strategy.prototype.field.BlackStone;
import com.strategy.prototype.field.EmtpyField;
import com.strategy.prototype.field.Field;
import com.strategy.prototype.field.WhiteStone;

/**
 * Represents a board.
 * 
 * @author Ralph Dürig
 */
public class PrototypeBoard implements Board {

	private static Board instance;

	private final int rows;
	private final int columns;
	private final Field fields[][];

	// Singleton
	private PrototypeBoard(int[][] board) {

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
			instance = new PrototypeBoard(board);
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
				Field field = getFieldForPrimitive(board[i][j]);
				fields[i][j] = field;
			}
		}
	}

	private Field getFieldForPrimitive(int primitiveField) {
		switch (primitiveField) {
		case 1:
			return new WhiteStone();
		case 2:
			return new BlackStone();
		default:
			return new EmtpyField();
		}
	}

	private boolean isInRange(int r, int c) {
		boolean rowsRange = r >= 0 && r < rows;
		boolean ColsRange = c >= 0 && c < columns;

		return rowsRange && ColsRange;
	}
}
