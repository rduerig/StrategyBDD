package com.strategy.havannah.logic.prediction;

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
import com.strategy.util.FieldGenerator;

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
		System.out.println("player sets on: " + lastSet.getIndex());
		board.setField(lastSet);
		lastSet.accept(visitor);
		win = visitor.getWin();

		// TODO make own turn

		// System.out.println(win.satOne().toString());
		List<byte[]> allsat = win.allsat();
		int index = -1;
		byte[] sat = Iterables.getFirst(allsat, new byte[0]);
		if (null != sat) {
			// System.out.println(Arrays.toString(sat));
			for (int i = 0; i < sat.length; i++) {
				if (sat[i] == 0x0001) {
					index = i;
					break;
				}
			}
		}

		if (index > 0) {
			System.out.println("computer sets on field: " + index);
			Field ownField = FieldGenerator.create(
					1,
					PositionHexagon.get(index / board.getRows(),
							index % board.getColumns()), index);
			board.setField(ownField);
			ownField.accept(visitor);
			win = visitor.getWin();
		} else {
			System.out.println("computer passes");
		}

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

		int b = board.getBoardSize();
		Position[] corners = new PositionHexagon[6];
		corners[0] = PositionHexagon.get(0, 0);
		corners[1] = PositionHexagon.get(0, b - 1);
		corners[2] = PositionHexagon.get(b - 1, 0);
		corners[3] = PositionHexagon.get(b - 1, 2 * b - 2);
		corners[4] = PositionHexagon.get(2 * b - 2, b - 1);
		corners[5] = PositionHexagon.get(2 * b - 2, 2 * b - 2);
		/**
		 * corners for a 5x5 board:<br>
		 * |1|| ||2| <br>
		 * | || || || | <br>
		 * |3|| || || ||4|<br>
		 * ___| || || || |<br>
		 * ______|5|| ||6|<br>
		 */

		// TODO use zero to start building win
		win = analyzer.getPath(corners[0], corners[1]).id();
		for (int i = 0; i < corners.length; i++) {
			for (int j = i + 1; j < corners.length; j++) {
				win = win.id().orWith(
						analyzer.getPath(corners[i], corners[j]).id());
			}
		}
		// win = analyzer.getPath(corners[0], corners[1]).id();
		// win = win.orWith(analyzer.getPath(corners[0], corners[2]).id());
		// win = win.orWith(analyzer.getPath(corners[0], corners[3]).id());
		// win = win.orWith(analyzer.getPath(corners[0], corners[4]).id());
		// win = win.orWith(analyzer.getPath(corners[0], corners[5]).id());
		//
		// win = win.orWith(analyzer.getPath(corners[1], corners[2]).id());
		// win = win.orWith(analyzer.getPath(corners[1], corners[3]).id());
		// win = win.orWith(analyzer.getPath(corners[1], corners[4]).id());
		// win = win.orWith(analyzer.getPath(corners[1], corners[5]).id());
		//
		// win = win.orWith(analyzer.getPath(corners[2], corners[3]).id());
		// win = win.orWith(analyzer.getPath(corners[2], corners[4]).id());
		// win = win.orWith(analyzer.getPath(corners[2], corners[5]).id());
		//
		// win = win.orWith(analyzer.getPath(corners[3], corners[4]).id());
		// win = win.orWith(analyzer.getPath(corners[3], corners[5]).id());
		//
		// win = win.orWith(analyzer.getPath(corners[4], corners[5]).id());

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
