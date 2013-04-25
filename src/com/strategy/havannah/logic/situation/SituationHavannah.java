package com.strategy.havannah.logic.situation;

import java.util.concurrent.Callable;

import net.sf.javabdd.BDD;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.field.TurnFieldVisitor;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;

/**
 * @author Ralph Dürig
 */
public class SituationHavannah implements Situation {

	private BDD win;
	private Board board;
	private TurnFieldVisitor visitor;

	public SituationHavannah(BoardAnalyzer analyzer, Board board) {
		this.board = board;
		init(analyzer);
	}

	@Override
	public BDD getWinningCondition() {
		return win;
	}

	@Override
	public void update(Field field) {
		System.out.println("setting stone on: " + field.getIndex());
		board.setField(field);
		field.accept(visitor);
		win = visitor.getWin();
	}

	@Override
	public void update(int fieldIndex, int type) {
		System.out.println("setting stone on: " + fieldIndex);
		Field field = FieldGenerator.create(
				type,
				PositionHexagon.get(fieldIndex / board.getRows(), fieldIndex
						% board.getColumns()), fieldIndex);
		board.setField(field);
		field.accept(visitor);
		win = visitor.getWin();
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

		win = analyzer.getFactory().zero();
		for (int i = 0; i < corners.length; i++) {
			for (int j = i + 1; j < corners.length; j++) {
				BDD path = analyzer.getPath(corners[i], corners[j]);
				win = win.id().orWith(path);
			}
		}

		// System.out.println("Nodes: " + win.nodeCount());
		// Double satCount = win.satCount();
		// System.out.println("Value: " + satCount.longValue());

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

	class PathCaller implements Callable<BDD> {

		private BoardAnalyzer analyzer;
		private Position p;
		private Position q;

		public PathCaller(BoardAnalyzer analyzer, Position p, Position q) {
			this.analyzer = analyzer;
			this.p = p;
			this.q = q;
		}

		@Override
		public BDD call() {
			return analyzer.getPath(p, q);

		}

	}

}
