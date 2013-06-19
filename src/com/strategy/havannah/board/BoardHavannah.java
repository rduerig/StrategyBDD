package com.strategy.havannah.board;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.strategy.api.board.Board;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;
import com.strategy.util.RowConstant;
import com.strategy.util.Turn;
import com.strategy.util.formatter.EvaluationFormatter;
import com.strategy.util.formatter.FieldIndexFormatter;

/**
 * Represents a hexagonal board for the game Havannah.<br>
 * 
 * @author Ralph Dürig
 * @see <a href="http://senseis.xmp.net/?Havannah">Havannah on sensei's
 *      library</a>
 */
public class BoardHavannah implements Board {

	private int boardSize;
	private Map<Position, Field> fields;
	private Table<RowConstant, Integer, Field> hgfCoordinates;

	private BoardHavannah(Map<Position, Integer> board, int boardSize) {
		if (null == board || board.isEmpty()) {
			throw new IllegalArgumentException("Given board was empty!");
		}

		this.boardSize = boardSize;
		fields = new HashMap<Position, Field>(board.size());
		hgfCoordinates = HashBasedTable.create();
		init(board);
	}

	private BoardHavannah(int[][] board, int boardSize) {
		if (null == board || board.length < 1 || board[0].length < 1) {
			throw new IllegalArgumentException("Given board was empty!");
		}

		this.boardSize = boardSize;
		fields = new HashMap<Position, Field>();
		hgfCoordinates = HashBasedTable.create();
		init(board, boardSize);
	}

	public static Board createInstance(Map<Position, Integer> board,
			int boardSize) {
		return new BoardHavannah(board, boardSize);
	}

	public static Board createInstance(int[][] board, int boardSize) {
		return new BoardHavannah(board, boardSize);
	}

	public static Board createInstance(int[][] board, int boardSize,
			List<Turn> turns) {
		BoardHavannah b = new BoardHavannah(board, boardSize);
		for (Turn t : turns) {
			Field oldField = b.getField(t.getCoord(), t.getCoordNumber());
			Field field = FieldGenerator.create(t.getColor().getPrimitive(),
					oldField.getPosition(), oldField.getIndex());
			b.setField(field);
		}
		return b;
	}

	// ************************************************************************

	@Override
	public Field getField(int row, int col) {
		Position key = PositionHexagon.get(row, col);
		return fields.get(key);
	}

	@Override
	public Field getField(RowConstant coord, Integer coordNumber) {
		return hgfCoordinates.get(coord, coordNumber);
	}

	@Override
	public Field getField(int index) {
		Position key = PositionHexagon
				.get(index / getRows(), index % getRows());
		return fields.get(key);
	}

	@Override
	public void setField(Field newField) {
		Position key = newField.getPosition();
		fields.put(key, newField);
	}

	@Override
	public int getColumns() {
		return (2 * boardSize) - 1;
	}

	@Override
	public int getRows() {
		return (2 * boardSize) - 1;
	}

	@Override
	public int getBoardSize() {
		return boardSize;
	}

	@Override
	public Collection<Position> getPositions() {
		return fields.keySet();
	}

	@Override
	public boolean isValidField(int row, int col) {
		return isValidField(PositionHexagon.get(row, col));
	}

	@Override
	public boolean isValidField(int index) {
		return null != getField(index);
	}

	@Override
	public boolean isValidField(Position p) {
		return fields.containsKey(p);
	}

	@Override
	public boolean isEmptyField(int index) {
		return getField(index) instanceof EmptyField;
	}

	private int linestart(int i) {
		return i < getBoardSize() ? 0 : i - getBoardSize() - 1;
	}

	private int lineend(int i) {
		return i < getBoardSize() ? getBoardSize() + i : getRows();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int k = 0; k < Math.abs(getBoardSize() - 1 - i) + 2; k++) {
				sb.append(" ");
			}
			for (int j = linestart(i); j < lineend(i); j++) {
				if (isValidField(PositionHexagon.get(i, j))) {
					Field field = getField(i, j);
					sb.append(" " + field);
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toMarkLastTurnString(Integer index) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int k = 0; k < Math.abs(getBoardSize() - 1 - i) + 2; k++) {
				sb.append("  ");
			}
			for (int j = linestart(i); j < lineend(i); j++) {
				if (isValidField(PositionHexagon.get(i, j))) {
					Field field = getField(i, j);
					if (null != index && index == field.getIndex()) {
						sb.append("(" + field + ")");
					} else {
						sb.append(" " + field + " ");
					}
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toIndexString() {
		FieldIndexFormatter formatter = new FieldIndexFormatter();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int k = 0; k < Math.abs(getBoardSize() - 1 - i) + 2; k++) {
				sb.append(formatter.space());
			}
			for (int j = linestart(i); j < lineend(i); j++) {
				if (isValidField(PositionHexagon.get(i, j))) {
					Field field = getField(i, j);
					sb.append(formatter.format(field.getIndex()));
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toRowColString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int k = 0; k < Math.abs(getBoardSize() - 1 - i) + 2; k++) {
				sb.append("  ");
			}
			for (int j = linestart(i); j < lineend(i); j++) {
				if (isValidField(PositionHexagon.get(i, j))) {
					Field field = getField(i, j);
					sb.append("|" + field.getPosition().getRow() + ":"
							+ field.getPosition().getCol() + "|");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toRowConstantString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int k = 0; k < Math.abs(getBoardSize() - 1 - i) + 2; k++) {
				sb.append("  ");
			}
			for (int j = linestart(i); j < lineend(i); j++) {
				if (isValidField(PositionHexagon.get(i, j))) {
					Field field = getField(i, j);
					RowConstant coord = RowConstant.parse(field.getIndex(),
							getBoardSize());
					Integer coordNumber = RowConstant.parseToCoordNumber(
							field.getIndex(), getBoardSize());
					sb.append(coord.name() + coordNumber + "  ");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toRatingString(double[] rating, int bestIndex) {
		EvaluationFormatter formatter = new EvaluationFormatter(bestIndex);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int k = 0; k < Math.abs(getBoardSize() - 1 - i) + 2; k++) {
				sb.append(formatter.space());
			}
			for (int j = linestart(i); j < lineend(i); j++) {
				if (isValidField(PositionHexagon.get(i, j))) {
					Field field = getField(i, j);
					Integer index = field.getIndex();
					sb.append(formatter.format(index, rating[index]));
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	// ************************************************************************

	private void init(Map<Position, Integer> board) {
		for (Entry<Position, Integer> entry : board.entrySet()) {
			Position pos = entry.getKey();
			int index = getIndex(pos);
			Field field = FieldGenerator.create(entry.getValue(), pos, index);
			fields.put(field.getPosition(), field);
			hgfCoordinates.put(RowConstant.parse(index, boardSize),
					RowConstant.parseToCoordNumber(index, boardSize), field);
		}
		System.out.println(hgfCoordinates);
	}

	private int getIndex(Position p) {
		return getRows() * p.getRow() + p.getCol();
	}

	private void init(int[][] board, int boardSize) {
		int limit = ((2 * boardSize) - 1);

		for (int i = 0; i < limit; i++) {
			if (i < boardSize - 1) {
				for (int j = 0; j < ((boardSize + i) % limit); j++) {
					Position pos = PositionHexagon.get(i, j);
					int index = getRows() * i + j;
					Field field = FieldGenerator
							.create(board[i][j], pos, index);
					fields.put(pos, field);
					hgfCoordinates.put(RowConstant.parse(index, boardSize),
							RowConstant.parseToCoordNumber(index, boardSize),
							field);
				}
			} else if (i == boardSize - 1) {
				for (int j = 0; j < limit; j++) {
					Position pos = PositionHexagon.get(i, j);
					int index = getRows() * i + j;
					Field field = FieldGenerator
							.create(board[i][j], pos, index);
					fields.put(pos, field);
					hgfCoordinates.put(RowConstant.parse(index, boardSize),
							RowConstant.parseToCoordNumber(index, boardSize),
							field);
				}
			} else {
				for (int j = ((boardSize + i) % limit); j < limit; j++) {
					Position pos = PositionHexagon.get(i, j);
					int index = getRows() * i + j;
					Field field = FieldGenerator
							.create(board[i][j], pos, index);
					fields.put(pos, field);
					hgfCoordinates.put(RowConstant.parse(index, boardSize),
							RowConstant.parseToCoordNumber(index, boardSize),
							field);
				}
			}
		}
	}
}
