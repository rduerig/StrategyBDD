package com.strategy.havannah.board;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.EvaluationFormatter;
import com.strategy.util.FieldGenerator;
import com.strategy.util.FieldIndexFormatter;

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

	private BoardHavannah(Map<Position, Integer> board, int boardSize) {
		if (null == board || board.isEmpty()) {
			throw new IllegalArgumentException("Given board was empty!");
		}

		this.boardSize = boardSize;
		fields = new HashMap<Position, Field>(board.size());
		init(board);
	}

	private BoardHavannah(int[][] board, int boardSize) {
		if (null == board || board.length < 1 || board[0].length < 1) {
			throw new IllegalArgumentException("Given board was empty!");
		}

		this.boardSize = boardSize;
		fields = new HashMap<Position, Field>();
		init(board, boardSize);
	}

	public static Board createInstance(Map<Position, Integer> board,
			int boardSize) {
		return new BoardHavannah(board, boardSize);
	}

	public static Board createInstance(int[][] board, int boardSize) {
		return new BoardHavannah(board, boardSize);
	}

	// ************************************************************************

	@Override
	public Field getField(int row, int col) {
		Position key = PositionHexagon.get(row, col);
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
	public boolean isValidField(Position p) {
		return fields.containsKey(p);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getColumns(); j++) {
				Field field = getField(i, j);
				if (null != field) {
					sb.append(field + "");
				} else {
					sb.append("   ");
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
			for (int j = 0; j < getColumns(); j++) {
				Field field = getField(i, j);
				if (null != field) {
					sb.append("|" + formatter.format(field.getIndex()) + "|");
				} else {
					sb.append(formatter.space());
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
			for (int j = 0; j < getColumns(); j++) {
				Field field = getField(i, j);
				if (null != field) {
					sb.append("|" + field.getPosition().getRow() + ":"
							+ field.getPosition().getCol() + "|");
				} else {
					sb.append("   ");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toRatingString(double[] rating, int bestIndex) {
		EvaluationFormatter formatter = new EvaluationFormatter();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getColumns(); j++) {
				Field field = getField(i, j);
				if (null != field) {
					String sep = field.getIndex() == bestIndex ? "!" : "|";
					sb.append(sep + formatter.format(rating[field.getIndex()])
							+ sep);
					// sb.append("|" + rating[field.getIndex()] + "|");
				} else {
					sb.append(formatter.space());
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
			Field field = FieldGenerator.create(entry.getValue(), pos,
					getRows() * pos.getRow() + pos.getCol());
			fields.put(field.getPosition(), field);
		}
	}

	private void init(int[][] board, int boardSize) {
		int limit = ((2 * boardSize) - 1);

		for (int i = 0; i < limit; i++) {
			if (i < boardSize - 1) {
				for (int j = 0; j < ((boardSize + i) % limit); j++) {
					Position pos = PositionHexagon.get(i, j);
					Field field = FieldGenerator.create(board[i][j], pos,
							getRows() * i + j);
					fields.put(pos, field);
				}
			} else if (i == boardSize - 1) {
				for (int j = 0; j < limit; j++) {
					Position pos = PositionHexagon.get(i, j);
					Field field = FieldGenerator.create(board[i][j], pos,
							getRows() * i + j);
					fields.put(pos, field);
				}
			} else {
				for (int j = ((boardSize + i) % limit); j < limit; j++) {
					Position pos = PositionHexagon.get(i, j);
					Field field = FieldGenerator.create(board[i][j], pos,
							getRows() * i + j);
					fields.put(pos, field);
				}
			}
		}
	}
}
