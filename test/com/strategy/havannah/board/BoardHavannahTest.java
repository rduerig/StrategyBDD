/**
 * 
 */
package com.strategy.havannah.board;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link BoardHavannah}.
 * 
 * @author Ralph Dürig
 */
public class BoardHavannahTest {

	@Test
	public void testValidFieldConstruction() {
		// matrix for a havannah board with size 3
		int[][] board = new int[][] {//
		/*    */{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 } };

		/*
		 * All 0 are valid fields on a havannah board of size 3
		 */

		int[][] expected = new int[][] {//
		/*    */{ 0, 0, 0, 1, 1 },//
				{ 0, 0, 0, 0, 1 },//
				{ 0, 0, 0, 0, 0 },//
				{ 1, 0, 0, 0, 0 },//
				{ 1, 1, 0, 0, 0 } };
		int havannahBoardSize = 3;
		int limit = ((2 * havannahBoardSize) - 1);

		for (int i = 0; i < limit; i++) {
			if (i < havannahBoardSize - 1) {
				for (int j = 0; j < ((havannahBoardSize + i) % limit); j++) {
					board[i][j] = 0;
				}
			} else if (i == havannahBoardSize - 1) {
				for (int j = 0; j < limit; j++) {
					board[i][j] = 0;
				}
			} else {
				for (int j = ((havannahBoardSize + i) % limit); j < limit; j++) {
					board[i][j] = 0;
				}
			}
		}

		assertMatrixEquals(expected, board);

	}

	@Test
	public void testCornerCalculation() {
		/*
		 * corners in havannah board are as follows (let b = board size): - i=0,
		 * j=0 - i=0, j=b-1 - i=b-1, j=0 - i=b-1, j=2b-2 - i=2b-2, j=b-1 -
		 * i=2b-2, j=2b-2
		 */

		// matrix for a havannah board with size 3
		int[][] board = new int[][] {//
		/*    */{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 1, 1, 1 } };

		/*
		 * All 0 are valid fields on a havannah board of size 3
		 */

		int[][] expected = new int[][] {//
		/*    */{ 0, 1, 0, 1, 1 },//
				{ 1, 1, 1, 1, 1 },//
				{ 0, 1, 1, 1, 0 },//
				{ 1, 1, 1, 1, 1 },//
				{ 1, 1, 0, 1, 0 } };
		int b = 3;

		// set all corners
		board[0][0] = 0;
		board[0][b - 1] = 0;
		board[b - 1][0] = 0;
		board[b - 1][2 * b - 2] = 0;
		board[2 * b - 2][b - 1] = 0;
		board[2 * b - 2][2 * b - 2] = 0;

		assertMatrixEquals(expected, board);
	}

	// ************************************************************************

	private void assertMatrixEquals(int[][] expected, int[][] actual) {
		Assert.assertEquals(expected.length, actual.length);
		Assert.assertTrue(null != actual[0]);
		for (int i = 0; i < expected.length; i++) {
			Assert.assertArrayEquals(expected[i], actual[i]);
		}
	}

}
