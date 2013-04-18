package com.strategy.prototype.board;

import static org.junit.Assert.assertEquals;
import net.sf.javabdd.BDD;

import org.junit.Before;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;
import com.strategy.prototype.logic.BoardAnalizerPrototypeWithCache;
import com.strategy.prototype.logic.PositionSquare;

/**
 * Test for {@link BoardPrototype}.
 * 
 * @author Ralph Dürig
 */
public class BoardPrototypeWithCacheTest {

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
	public void testModelCount() {

		// it exists exactly one path from (3,0) to (0,3)
		int expectedModelCount = 1;

		/*
		 * Map the primitive board to the internal representation.
		 */
		Board board = BoardPrototype.getInstance(PRIMITIVE_BOARD_PATHS_ONE);
		// System.out.println("Given board:\n" + board);
		// System.out.println();

		// corner bottom left
		Position p = PositionSquare.get(3, 0);
		// corner top right
		Position q = PositionSquare.get(0, 3);

		BoardAnalizerPrototypeWithCache analizer = new BoardAnalizerPrototypeWithCache(
				board);
		BDD path = analizer.getPath(p, q);
		int actual = path.allsat().size();
		// System.out.println("Models count for path from (3,0) to (0,3): "
		// + currentModels);

		assertEquals(expectedModelCount, actual);
	}

}
