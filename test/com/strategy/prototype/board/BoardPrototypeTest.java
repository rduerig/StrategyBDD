/**
 * 
 */
package com.strategy.prototype.board;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.prototype.logic.BoardAnalizerPrototype;

/**
 * Test for {@link BoardPrototype}.
 * 
 * @author Ralph Dürig
 */
public class BoardPrototypeTest {

	private static int[][] PRIMITIVE_BOARD_PATHS_ONE;

	@Before
	public void init() {
		System.setProperty("bdd", "bdd");

		/*
		 * Get a primitive board, fields are represented as integers.
		 */
		PRIMITIVE_BOARD_PATHS_ONE = new int[][] {//
		/*    */{ 2, 1, 2, 0 },//
				{ 0, 2, 1, 0 },//
				{ 1, 0, 1, 0 },//
				{ 1, 0, 2, 2 } };
	}

	@Test
	public void testBoard() {

		// it exists exactly one path from (3,0) to (0,3)
		int expected = 1;

		/*
		 * Map the primitive board to the internal representation.
		 */
		Board board = BoardPrototype.getInstance(PRIMITIVE_BOARD_PATHS_ONE);
		// System.out.println("Given board:\n" + board);
		// System.out.println();

		BoardAnalizerPrototype analizer = new BoardAnalizerPrototype(board);
		int actual = analizer.getModelCount();
		// System.out.println("Models count for path from (3,0) to (0,3): "
		// + currentModels);

		Assert.assertEquals(expected, actual);
	}

}
