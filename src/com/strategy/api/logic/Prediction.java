package com.strategy.api.logic;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.havannah.logic.PositionHexagon;

/**
 * TODO Prototype of turn prediction
 * 
 * @author Ralph Dürig
 */
public class Prediction {

	private Board board;
	private BDD win;

	public Prediction(BoardAnalyzer analyzer, Board board) {
		this.board = board;
		init(analyzer);
	}

	/**
	 * Black has set on given field, updates the board according to that.
	 * 
	 * @param field
	 * @return
	 */
	public int doNextTurn(Field lastSet) {
		BDDFactory fac = win.getFactory();
		win = win.restrict(fac.ithVar(lastSet.getIndex()).id().not()).id();
		board.setField(lastSet);
		System.out.println(board);
		// TODO make own turn
		return 0;
	}

	// ************************************************************************

	private void init(BoardAnalyzer analyzer) {
		/**
		 * corners in havannah board are as follows (let b = board size):<br>
		 * - i=0, j=0<br>
		 * - i=0, j=b-1<br>
		 * - i=b-1, j=0<br>
		 * - i=b-1, j=2b-2<br>
		 * - i=2b-2, j=b-1<br>
		 * - i=2b-2, j=2b-2
		 */

		// TODO path for all corners
		int b = board.getBoardSize();
		Position corner1 = PositionHexagon.get(0, 0);
		Position corner2 = PositionHexagon.get(0, b - 1);
		Position corner3 = PositionHexagon.get(b - 1, 0);
		Position corner4 = PositionHexagon.get(b - 1, 2 * b - 2);
		Position corner5 = PositionHexagon.get(2 * b - 2, b - 1);
		Position corner6 = PositionHexagon.get(2 * b - 2, 2 * b - 2);

		// ArrayList<Position> corners = Lists.newArrayList(corner1, corner2,
		// corner3, corner4, corner5, corner6);
		// for (Position position : corners) {
		//
		// }
		win = analyzer.getPath(corner1, corner2).id();
	}
}
