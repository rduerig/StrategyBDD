package com.strategy.util;

/**
 * @author Ralph Dürig
 */
public enum RowConstant {

	A(1), //
	B(2), //
	C(3), //
	D(4), //
	E(5), //
	F(6), //
	G(7), //
	H(8), //
	I(9), //
	J(10), //
	K(11), //
	L(12), //
	M(13), //
	N(14), //
	O(15), //
	P(16), //
	Q(17), //
	R(18), //
	S(19), //
	T(20), //
	U(21), //
	V(22), //
	W(23), //
	X(24), //
	Y(25), //
	Z(26); //

	private final int index;

	private RowConstant(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getLetterIndex() {
		return index;
	}

	public static Integer parseToLetterIndex(String s) {
		for (RowConstant val : values()) {
			if (val.name().equalsIgnoreCase(s)) {
				return val.getLetterIndex();
			}
		}

		return null;
	}

	public static RowConstant parseToConstant(String s) {
		for (RowConstant val : values()) {
			if (val.name().equalsIgnoreCase(s)) {
				return val;
			}
		}

		return null;
	}

	public static RowConstant getForLetterIndex(int index) {
		for (RowConstant val : values()) {
			if (val.getLetterIndex() == index) {
				return val;
			}
		}

		return null;
	}

	// c5 d4 e3______
	// b4 c4 d3 e2___
	// a3 b3 c3 d2 e1
	// ___a2 b2 c2 d1
	// ______a1 b1 c1

	public static RowConstant parse(int fieldIndex, int boardSize) {
		int dim = (2 * boardSize) - 1;
		int row = fieldIndex / dim;
		int col = fieldIndex % dim;

		int letterIndex = col - row + boardSize;

		return getForLetterIndex(letterIndex);
	}

	public static Integer parseToCoordNumber(int fieldIndex, int boardSize) {
		int dim = (2 * boardSize) - 1;
		int row = fieldIndex / dim;
		int col = fieldIndex % dim;

		int withNumber = col > row ? dim - col : dim - row;

		return withNumber;
	}

	public static String parseToCoordString(int fieldIndex, int boardSize) {
		RowConstant coord = parse(fieldIndex, boardSize);
		Integer coordNumber = parseToCoordNumber(fieldIndex, boardSize);
		StringBuilder sb = new StringBuilder();
		sb.append(coord.name().toLowerCase());
		sb.append(coordNumber.toString());

		return sb.toString();
	}

}
