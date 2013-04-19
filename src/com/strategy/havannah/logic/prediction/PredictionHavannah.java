package com.strategy.havannah.logic.prediction;

import java.util.Arrays;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.strategy.api.board.Board;
import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.FieldVisitor;
import com.strategy.api.field.WhiteStone;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.havannah.logic.PositionHexagon;

/**
 * TODO Prototype of turn prediction
 * 
 * @author Ralph Dürig
 */
public class PredictionHavannah implements Prediction {

	private Board board;
	private BDD win;
	private TurnFieldVisitor visitor;

	public PredictionHavannah(BoardAnalyzer analyzer, Board board) {
		this.board = board;
		init(analyzer);
	}

	/**
	 * Black has set on given field, updates the board according to that.
	 * 
	 * @param field
	 * @return
	 */
	@Override
	public int doNextTurn(Field lastSet) {
		System.out.println("opponent sets on: " + lastSet.getIndex());
		board.setField(lastSet);
		lastSet.accept(visitor);
		win = visitor.getWin();

		// TODO make own turn

		List<byte[]> allsat = win.allsat();
		int index = -1;
		byte[] sat = Iterables.getFirst(allsat, new byte[0]);
		System.out.println(Arrays.toString(sat));
		for (int i = 0; i < sat.length; i++) {
			if (sat[i] == 0x0001) {
				index = i;
				break;
			}
		}

		System.out.println("set on field: " + index);

		return index;
	}

	@Override
	public int getPossiblePaths() {
		return win.id().allsat().size();
	}

	// ************************************************************************

	private void registerTurn(Field field) {

	}

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
		/**
		 * corners for a 5x5 board:<br>
		 * |1|| ||2| <br>
		 * | || || || | <br>
		 * |3|| || || ||4|<br>
		 * ___| || || || |<br>
		 * ______|5|| ||6|<br>
		 */

		// ArrayList<Position> corners = Lists.newArrayList(corner1, corner2,
		// corner3, corner4, corner5, corner6);
		// for (Position position : corners) {
		//
		// }
		win = analyzer.getPath(corner1, corner3).id();
		visitor = new TurnFieldVisitor(win);
	}

	// ************************************************************************

	private class TurnFieldVisitor implements FieldVisitor {

		private BDDFactory fac;
		private BDD win;

		public TurnFieldVisitor(BDD win) {
			this.win = win;
			fac = win.getFactory();
		}

		public BDD getWin() {
			return win.id();
		}

		@Override
		public void visit(EmptyField field) {
			// do nothing
		}

		@Override
		public void visit(WhiteStone field) {
			win = win.restrict(fac.ithVar(field.getIndex()).id()).id();
		}

		@Override
		public void visit(BlackStone field) {
			win = win.restrict(fac.ithVar(field.getIndex()).id().not()).id();
		}

	}
}
