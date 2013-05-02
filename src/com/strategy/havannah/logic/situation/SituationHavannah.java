package com.strategy.havannah.logic.situation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
		// System.out.println("setting stone on: " + fieldIndex);
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

		win = analyzer.getFactory().zero();

		// computes bdd representation of the bridge condition
		// System.out.println("computing bridge");
		win.orWith(getBridgeCondition(analyzer));
		// System.out.println("...done");

		// TODO fork
		// computes bdd representation of the fork condition
		// System.out.println("computing fork");
		win.orWith(getForkCondition(analyzer));
		// System.out.println("...done");

		// TODO ring
		// computes bdd representation of the ring condition
		// win.orWith(getRingCondition(analyzer));

		try {
			analyzer.getFactory().save(getFileName(), win);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BDD getBridgeCondition(BoardAnalyzer analyzer) {
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

		BDD bridge = analyzer.getFactory().zero();
		for (int i = 0; i < corners.length; i++) {
			for (int j = i + 1; j < corners.length; j++) {
				BDD path = analyzer.getPath(corners[i], corners[j]);
				bridge = bridge.id().orWith(path);
			}
		}

		return bridge;
	}

	private BDD getForkCondition(BoardAnalyzer analyzer) {
		BDD fork = analyzer.getFactory().zero();
		Collection<Position> allPos = board.getPositions();

		ArrayList<Iterable<Position>> edgePositions = Lists.newArrayList();
		for (EdgeFieldCategory cat : EdgeFieldCategory.values()) {
			edgePositions.add(filterPositions(cat, allPos));
		}

		for (int i = 0; i < board.getBoardSize() - 2; i++) {
			for (int j = i + 1; j < board.getBoardSize() - 2; j++) {
				for (int k = j + 1; k < board.getBoardSize() - 2; k++) {
					BDD edgesConnected = analyzer.getFactory().zero();
					for (Position pos1 : edgePositions.get(i)) {
						for (Position pos2 : edgePositions.get(j)) {
							for (Position pos3 : edgePositions.get(k)) {
								BDD path = analyzer.getPath(pos1, pos2)
										.andWith(analyzer.getPath(pos2, pos3));
								edgesConnected = edgesConnected.id().orWith(
										path);
							}
						}
					}
					fork = fork.id().orWith(edgesConnected);
				}
			}
		}

		return fork;
	}

	private Iterable<Position> filterPositions(EdgeFieldCategory cat,
			Collection<Position> allPos) {
		EdgeFieldPredicate predicate = new EdgeFieldPredicate(cat, board);
		Iterable<Position> filtered = Iterables.filter(allPos, predicate);

		return filtered;
	}

	private BDD getRingCondition(BoardAnalyzer analyzer) {
		BDD ring = analyzer.getFactory().zero();

		return ring;
	}

	private String getFileName() {
		return BDD_FILE_PREFIX + board.getBoardSize()
				+ color.name().toLowerCase();
	}

}
