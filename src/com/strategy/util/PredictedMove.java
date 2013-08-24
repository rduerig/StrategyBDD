package com.strategy.util;

/**
 * @author Ralph Dürig
 */
public class PredictedMove implements Comparable<PredictedMove> {

	private static final String SEPARATOR = " - ";

	private final Turn turn;
	private final int fieldIndex;
	private final double value;

	public static PredictedMove create(Turn turn, int fieldIndex, double value) {
		return new PredictedMove(turn, fieldIndex, value);
	}

	public static PredictedMove create(int boardSize, int fieldIndex,
			StoneColor color, double value) {
		RowConstant coord = RowConstant.parse(fieldIndex, boardSize);
		int coordNumber = RowConstant.parseToCoordNumber(fieldIndex, boardSize);
		Turn t = new Turn(coord, coordNumber, color);
		return new PredictedMove(t, fieldIndex, value);
	}

	private PredictedMove(Turn turn, int fieldIndex, double value) {
		this.turn = turn;
		this.fieldIndex = fieldIndex;
		this.value = value;
	}

	/**
	 * @return the fieldIndex
	 */
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * @return the turn
	 */
	public Turn getTurn() {
		return turn;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(turn);
		sb.append(SEPARATOR);
		sb.append(fieldIndex);
		sb.append("  ");
		sb.append("(");
		sb.append(value);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int compareTo(PredictedMove o) {
		return Double.compare(this.value, o.value);
	}

}
