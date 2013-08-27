package com.strategy.util;

/**
 * A trick class that provides indices of cells for different board sizes that
 * have to be played in order not to get swapped.<br>
 * Note that these values are determined by feeling not by any calculation.
 * 
 * @author Ralph Dürig
 */
public class FirstMoveProvider {

	private static final int[] moves1 = new int[] { 0 };
	private static final int[] moves2 = new int[] { 0, 1, 3, 5, 7, 8 };
	private static final int[] moves3 = new int[] { 6, 7, 13, 18, 17, 11 };
	private static final int[] moves4 = new int[] { 30, 33, 18, 15, 9, 39 };
	private static final int[] moves5 = new int[] { 10, 37, 67, 70, 43, 13 };

	public static int[] getMoves(int size) {
		switch (size) {
		case 1:
			return moves1;
		case 2:
			return moves2;
		case 3:
			return moves3;
		case 4:
			return moves4;
		case 5:
			return moves5;
		default:
			return null;
		}
	}

}
