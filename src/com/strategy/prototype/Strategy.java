package com.strategy.prototype;

import com.strategy.api.board.Board;
import com.strategy.prototype.board.BoardPrototype;
import com.strategy.prototype.logic.BoardAnalizerPrototype;

/**
 * Gets a board situation and uses BDDs to check if there are models for winning
 * conditions.
 * 
 * @author Ralph Dürig
 */
public class Strategy {

	// TODO make a board to BDD mapper (one for black and one for white)
	public static void main(String[] args) {

		System.setProperty("bdd", "bdd");

		/*
		 * Get a primitive board, fields are represented as integers.
		 */
		int[][] primitiveBoard = new int[][] {//
		/*    */{ 2, 1, 2, 0 },//
				{ 0, 2, 1, 0 },//
				{ 1, 0, 1, 0 },//
				{ 1, 0, 2, 2 } };

		/*
		 * Map the primitive board to the internal representation.
		 */
		Board board = BoardPrototype.getInstance(primitiveBoard);
		System.out.println("Given board:\n" + board);
		System.out.println();

		BoardAnalizerPrototype analizer = new BoardAnalizerPrototype(board);
		int currentModels = analizer.getModelCount();
		System.out.println("Models count for path from (3,0) to (0,3): "
				+ currentModels);
	}

}
