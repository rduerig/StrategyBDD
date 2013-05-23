package com.strategy.havannah.logic.situation;

/**
 * Access to primitive boards with restrictions for different winning
 * conditions.
 * 
 * @author Ralph Dürig
 */
public class TestBoardProviderConditions {

	/**
	 * Board with size 3, is empty.
	 */
	public static int[][] BOARD_EMPTY = new int[][] {//
	/*    */{ 0, 0, 0, 1, 1 },//
			{ 0, 0, 0, 0, 1 },//
			{ 0, 0, 0, 0, 0 },//
			{ 1, 0, 0, 0, 0 },//
			{ 1, 1, 0, 0, 0 } };

	/**
	 * Board with size 3, has 3 path for bridge condition.
	 */
	public static int[][] BOARD_BRIDGE_5 = new int[][] {//
	/*    */{ 0, 2, 0, 1, 1 },//
			{ 2, 0, 0, 2, 1 },//
			{ 2, 2, 0, 2, 2 },//
			{ 1, 2, 0, 2, 2 },//
			{ 1, 1, 0, 2, 2 } };

	/**
	 * Board with size 3, has already won.
	 */
	public static int[][] BOARD_BRIDGE_WON = new int[][] {//
	/*    */{ 0, 2, 1, 1, 1 },//
			{ 2, 0, 1, 2, 1 },//
			{ 2, 2, 1, 2, 2 },//
			{ 1, 2, 1, 2, 2 },//
			{ 1, 1, 1, 2, 2 } };

	/**
	 * Board with size 3, has 3 path for bridge condition.
	 */
	public static int[][] BOARD_BRIDGE_14 = new int[][] {//
	/*    */{ 0, 2, 0, 1, 1 },//
			{ 2, 0, 0, 2, 1 },//
			{ 2, 0, 2, 0, 0 },//
			{ 1, 2, 0, 2, 2 },//
			{ 1, 1, 0, 2, 2 } };

	/**
	 * Board with size 3, has 1 path for fork condition.
	 */
	public static int[][] BOARD_FORK_1 = new int[][] {//
	/*    */{ 2, 2, 2, 1, 1 },//
			{ 0, 2, 2, 2, 1 },//
			{ 2, 0, 2, 2, 2 },//
			{ 1, 0, 0, 2, 2 },//
			{ 1, 1, 2, 0, 2 } };

	/**
	 * Board with size 3, has 6 path for fork condition.
	 */
	public static int[][] BOARD_FORK_6 = new int[][] {//
	/*    */{ 2, 2, 2, 1, 1 },//
			{ 0, 2, 2, 2, 1 },//
			{ 2, 0, 2, 2, 2 },//
			{ 1, 0, 0, 0, 0 },//
			{ 1, 1, 2, 0, 2 } };

	/**
	 * Board with size 3, has 2 path for ring condition.
	 */
	public static int[][] BOARD_RING_1 = new int[][] {//
	/*    */{ 2, 2, 2, 1, 1 },//
			{ 2, 1, 0, 1, 1 },//
			{ 2, 0, 0, 1, 2 },//
			{ 1, 1, 1, 1, 2 },//
			{ 1, 1, 2, 2, 2 } };

	/**
	 * Board with size 3, has 1 path for ring condition.
	 */
	public static int[][] BOARD_RING_1_OTHER = new int[][] {//
	/*    */{ 2, 2, 2, 1, 1 },//
			{ 2, 1, 0, 1, 1 },//
			{ 2, 1, 0, 1, 2 },//
			{ 1, 1, 1, 1, 2 },//
			{ 1, 1, 2, 2, 2 } };

	/**
	 * Board with size 3, has 1 path for ring condition.
	 */
	public static int[][] BOARD_RING_2 = new int[][] {//
	/*    */{ 1, 0, 1, 1, 1 },//
			{ 1, 0, 1, 1, 1 },//
			{ 1, 1, 1, 1, 1 },//
			{ 1, 1, 1, 0, 0 },//
			{ 1, 1, 1, 1, 1 } };

}
