package com.strategy.prototype.board;

import java.util.Collection;
import java.util.HashSet;

import com.strategy.api.board.Board;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.prototype.logic.PositionSquare;
import com.strategy.util.FieldGenerator;
import com.strategy.util.RowConstant;

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
	public Field getField(RowConstant coord, Integer coordNumber) {
		return null;
	}

	@Override
	public Field getField(int index) {
		return getField(index / getRows(), index % getRows());
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
	public boolean isValidField(int row, int col) {
		return isValidField(PositionSquare.get(row, col));
	}

	@Override
	public boolean isValidField(int index) {
		return null != getField(index);
	}

	@Override
	public boolean isValidField(Position p) {
		return positions.contains(p);
	}

	@Override
	public boolean isEmptyField(int index) {
		return getField(index) instanceof EmptyField;
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

	@Override
	public String toMarkLastTurnString(Integer index) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Field field = fields[i][j];
				if (null != index && index == field.getIndex()) {
					sb.append("(" + field + ")");
				} else {
					sb.append(field + "");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public String toIndexString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sb.append(fields[i][j].getIndex() + "");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public String toRowColString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sb.append(fields[i][j].getPosition().getRow() + ":"
						+ fields[i][j].getPosition().getCol() + "");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public String toRatingString(double[] rating, int bestIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Field field = getField(i, j);
				sb.append(rating[field.getIndex()] + "");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public String toRowConstantString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sb.append(fields[i][j].getPosition().getRow() + ":"
						+ fields[i][j].getPosition().getCol() + "");
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
