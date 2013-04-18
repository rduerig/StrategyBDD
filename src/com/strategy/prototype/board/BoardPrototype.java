package com.strategy.prototype.board;

import java.util.Collection;
import java.util.HashSet;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.prototype.logic.PositionSquare;
import com.strategy.util.FieldGenerator;

/**
 * Represents a square board.
 * 
 * @author Ralph Dürig
 */
public class BoardPrototype implements Board {

	private final int rows;
	private final int columns;
	private final Field fields[][];
	private Collection<Position> positions;

	private BoardPrototype(int[][] board) {

		if (null == board || board.length < 1 || board[0].length < 1) {
			throw new IllegalArgumentException("Given board was empty!");
		}

		rows = board.length;
		columns = board[0].length;
		fields = new Field[rows][columns];
		positions = new HashSet<Position>();
		init(board);
	}

	public static Board createInstance(int[][] board) {
		return new BoardPrototype(board);
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
	public void setField(Field newField) {
		int row = newField.getPosition().getRow();
		int col = newField.getPosition().getCol();
		if (isInRange(row, col)) {
			fields[row][col] = newField;
		}
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
	public int getBoardSize() {
		return rows;
	}

	@Override
	public Collection<Position> getPositions() {
		return positions;
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
				Position pos = PositionSquare.get(i, j);
				Field field = FieldGenerator.create(board[i][j], pos, i * rows
						+ j);
				fields[i][j] = field;
				positions.add(pos);
			}
		}
	}

	private boolean isInRange(int r, int c) {
		boolean rowsRange = r >= 0 && r < rows;
		boolean ColsRange = c >= 0 && c < columns;

		return rowsRange && ColsRange;
	}
}
