package com.strategy.havannah.logic.situation;

import java.io.IOException;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class SituationHavannah implements Situation {

	private static final String BDD_FILE_PREFIX = "win";
	private BDD win;
	private Board board;
	private StoneColor color;

	public SituationHavannah(BoardAnalyzer analyzer, Board board) {
		this.board = board;
		this.color = analyzer.getStoneColor();
		init(analyzer);
	}

	@Override
	public BDD getWinningCondition() {
		return win;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public StoneColor getStoneColor() {
		return color;
	}

	@Override
	public void update(int fieldIndex, StoneColor color) {
		System.out.println("setting stone on: " + fieldIndex);
		Field field = FieldGenerator.create(
				color.getPrimitive(),
				PositionHexagon.get(fieldIndex / board.getRows(), fieldIndex
						% board.getColumns()), fieldIndex);
		board.setField(field);
		BDDFactory fac = win.getFactory();
		if (this.color.equals(color)) {
			win.restrictWith(fac.ithVar(field.getIndex()));
		} else {
			win.restrictWith(fac.nithVar(field.getIndex()));
		}

	}

	// ************************************************************************

	private void init(BoardAnalyzer analyzer) {
		// System.out.println("try loading from file: win" +
		// board.getBoardSize()
		// + color.name().toLowerCase());
		try {
			win = analyzer.getFactory().load(getFileName());
			// System.out.println("loaded from file: win" + board.getBoardSize()
			// + color.name().toLowerCase());
		} catch (IOException e) {
			initFromScratch(analyzer);
			// System.out.println("loaded from scratch");
		}
	}

	private void initFromScratch(BoardAnalyzer analyzer) {
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

		try {
			analyzer.getFactory().save(getFileName(), win);
		} catch (IOException e) {
			e.printStackTrace();
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
	}

	private String getFileName() {
		return BDD_FILE_PREFIX + board.getBoardSize()
				+ color.name().toLowerCase();
	}

}
