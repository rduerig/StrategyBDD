package com.strategy.prototype.board;

import static org.junit.Assert.assertEquals;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import org.junit.Before;
import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;
import com.strategy.prototype.logic.BoardAnalizerPrototype;
import com.strategy.prototype.logic.PositionSquare;

/**
 * Test for {@link BoardPrototype}.
 * 
 * @author Ralph Dürig
 */
public class BoardPrototypeTest {

	private Board PRIMITIVE_BOARD_PATHS_ONE;
	private Board PRIMITIVE_BOARD_PATHS_ALL;
	private Board PRIMITIVE_BOARD_PATHS_BIG;
	private Board PRIMITIVE_BOARD_PATHS_RECURSION_DEBUG;

	@Before
	public void init() {
		System.setProperty("bdd", "bdd");

		/*
		 * Get a primitive board, fields are represented as integers.
		 */
		PRIMITIVE_BOARD_PATHS_ONE = BoardPrototype.createInstance(new int[][] {//
				/*    */{ 2, 1, 2, 0 },//
						{ 0, 2, 1, 0 },//
						{ 1, 0, 1, 0 },//
						{ 1, 0, 2, 2 } });

		PRIMITIVE_BOARD_PATHS_ALL = BoardPrototype.createInstance(new int[][] {//
				/*    */{ 0, 0, 0, 0 },//
						{ 0, 0, 0, 0 },//
						{ 0, 0, 0, 0 },//
						{ 0, 0, 0, 0 } });

		PRIMITIVE_BOARD_PATHS_RECURSION_DEBUG = BoardPrototype
				.createInstance(new int[][] {//
				/*    */{ 0, 0 },//
						{ 0, 0 } });

		PRIMITIVE_BOARD_PATHS_BIG = BoardPrototype.createInstance(new int[][] {//
				/*    */{ 2, 2, 2, 2, 2 },//
						{ 2, 2, 0, 0, 0 },//
						{ 2, 0, 0, 2, 0 },//
						{ 0, 0, 2, 0, 0 },//
						{ 2, 2, 2, 2, 2 } });
	}

	@Test
	public void testModelCountSample() {

		// it exists exactly one path from (3,0) to (0,3)
		int expectedModelCount = 1;

		// corner bottom left
		Position p = PositionSquare.get(3, 0);
		// corner top right
		Position q = PositionSquare.get(0, 3);

		BoardAnalizerPrototype analizer = new BoardAnalizerPrototype(
				PRIMITIVE_BOARD_PATHS_ONE);
		BDD path = analizer.getPath(p, q);
		int actual = path.allsat().size();
		// System.out.println("Models count for path from (3,0) to (0,3): "
		// + currentModels);
		analizer.done();
		path.free();

		assertEquals(expectedModelCount, actual);
	}

	@Test
	public void testModelCountChanging() {

		// there are 260 possible paths from bottom left to top right on a 4x4
		// board
		int expectedModelCount = 260;

		Position p = PositionSquare.get(3, 0);
		Position q = PositionSquare.get(0, 3);

		BoardAnalizerPrototype analizer = new BoardAnalizerPrototype(
				PRIMITIVE_BOARD_PATHS_ALL);
		BDD path = analizer.getPath(p, q);
		int actual = path.allsat().size();
		assertEquals(expectedModelCount, actual);

		expectedModelCount = 58;
		BDDFactory fac = path.getFactory();
		BDD restricted = path.restrict(fac.ithVar(2).not());
		actual = restricted.allsat().size();
		analizer.done();
		path.free();
		restricted.free();
		assertEquals(expectedModelCount, actual);
	}

	@Test
	public void testModelCountBigBoard() {

		int expectedModelCount = 1;
		Position p = PositionSquare.get(3, 0);
		Position q = PositionSquare.get(3, 3);
		BoardAnalizerPrototype analizer = new BoardAnalizerPrototype(
				PRIMITIVE_BOARD_PATHS_BIG);
		BDD path = analizer.getPath(p, q);
		int actual = path.allsat().size();
		analizer.done();
		path.free();

		assertEquals(expectedModelCount, actual);
	}

}