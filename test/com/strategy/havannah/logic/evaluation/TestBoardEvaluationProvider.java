package com.strategy.havannah.logic.evaluation;

/**
 * Access to primitive boards of different sizes.
 * 
 * @author Ralph Dürig
 */
public class TestBoardEvaluationProvider {

	/**
	 * just an empty board with size 3
	 */
	public static int[][] BOARD_EMPTY = new int[][] {//
	/*    */{ 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0 } };

	/**
	 * get a fork by setting to field 01
	 */
	public static int[][] BOARD_FORK = new int[][] {//
	/*    */{ 0, 0, 0, 1, 1 },//
			{ 0, 0, 1, 2, 1 },//
			{ 0, 0, 1, 1, 0 },//
			{ 1, 0, 0, 1, 1 },//
			{ 1, 1, 0, 1, 0 } };

	/**
	 * get a bridge by setting to field 02
	 */
	public static int[][] BOARD_BRIDGE = new int[][] {//
	/*    */{ 0, 0, 0, 1, 1 },//
			{ 0, 0, 1, 0, 1 },//
			{ 0, 0, 1, 0, 0 },//
			{ 1, 0, 1, 1, 0 },//
			{ 1, 1, 0, 0, 1 } };

	/**
	 * get a ring by setting to field 06
	 */
	public static int[][] BOARD_RING = new int[][] {//
	/*    */{ 0, 0, 0, 1, 1 },//
			{ 0, 0, 1, 1, 1 },//
			{ 0, 1, 0, 1, 0 },//
			{ 1, 1, 1, 1, 0 },//
			{ 1, 1, 0, 0, 0 } };

}
