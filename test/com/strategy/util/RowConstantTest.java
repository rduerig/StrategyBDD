package com.strategy.util;

import org.junit.Test;

import com.strategy.api.board.Board;
import com.strategy.havannah.TestBoardProvider;
import com.strategy.havannah.board.BoardHavannah;

/**
 * @author Ralph Dürig
 */
public class RowConstantTest {

	@Test
	public void test() {
		Board b = BoardHavannah.createInstance(TestBoardProvider.BOARD_3, 3);
		System.out.println(b.toRowConstantString());
	}

}
