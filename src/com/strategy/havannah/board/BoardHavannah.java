package com.strategy.havannah.board;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;

/**
 * Represents a hexagonal board for the game Havannah.<br>
 * 
 * @author Ralph Dürig
 * @see <a href="http://senseis.xmp.net/?Havannah">Havannah on sensei's
 *      library</a>
 */
public class BoardHavannah implements Board {

	private static Board instance;
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

	public static Board getInstance(Map<Position, Integer> board, int boardSize) {
		if (null == instance) {
			instance = new BoardHavannah(board, boardSize);
		}
		return instance;
	}

	// ************************************************************************

	@Override
	public Field getField(int row, int col) {
		Position key = PositionHexagon.get(row, col);
		return fields.get(key);
	}

	@Override
	public int getColumns() {
		return boardSize;
	}

	@Override
	public int getRows() {
		return boardSize;
	}

	@Override
	public Collection<Position> getPositions() {
		return fields.keySet();
	}

	// ************************************************************************

	private void init(Map<Position, Integer> board) {
		int index = 0;
		for (Entry<Position, Integer> entry : board.entrySet()) {
			Field field = FieldGenerator.create(entry.getValue(),
					entry.getKey(), index++);
			fields.put(field.getPosition(), field);
		}
	}
}
