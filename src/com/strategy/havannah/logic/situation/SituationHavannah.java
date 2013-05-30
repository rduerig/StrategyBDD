package com.strategy.havannah.logic.situation;

import java.io.IOException;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;
import com.strategy.util.Preferences;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class SituationHavannah implements Situation {

	private static final String BDD_FILE_PREFIX = "win";
	private BDD winFork;
	private BDD winBridge;
	private BDD winRing;
	private BDD win;
	private Board board;
	private StoneColor color;

	public SituationHavannah(BoardAnalyzer analyzer, Board board,
			StoneColor color) {
		this.board = board;
		this.color = color;
		init(analyzer);
	}

	@Override
	public BDD getWinningCondition() {
		return win;
	}

	@Override
	public BDD getWinningConditionFork() {
		return winFork;
	}

	@Override
	public BDD getWinningConditionBridge() {
		return winBridge;
	}

	@Override
	public BDD getWinningConditionRing() {
		return winRing;
	}

	@Override
	public boolean hasVictory() {
		return win.isOne();
	}

	@Override
	public boolean hasFork() {
		return winFork.isOne();
	}

	@Override
	public boolean hasBridge() {
		return winBridge.isOne();
	}

	@Override
	public boolean hasRing() {
		return winRing.isOne();
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
		BDDFactory fac = winFork.getFactory();
		if (this.color.equals(color)) {
			// System.out.println("restrict with ith");
			win.restrictWith(fac.ithVar(field.getIndex()));
			winFork.restrictWith(fac.ithVar(field.getIndex()));
			winBridge.restrictWith(fac.ithVar(field.getIndex()));
			winRing.restrictWith(fac.ithVar(field.getIndex()));
		} else {
			// System.out.println("restrict with nith");
			win.restrictWith(fac.nithVar(field.getIndex()));
			winFork.restrictWith(fac.nithVar(field.getIndex()));
			winBridge.restrictWith(fac.nithVar(field.getIndex()));
			winRing.restrictWith(fac.nithVar(field.getIndex()));
		}

	}

	// ************************************************************************

	private void init(BoardAnalyzer analyzer) {
		// System.out.println("try loading from file: win" +
		// board.getBoardSize()
		// + color.name().toLowerCase());
		// File winFileFork = new File(getFileName() + "fork");
		// File winFileBridge = new File(getFileName() + "bridge");
		// File winFileRing = new File(getFileName() + "ring");
		if (Preferences.getInstance().isGenerateFiles()) {
			initFromScratch(analyzer);
		} else {
			try {
				winFork = analyzer.getFactory().load(getFileName() + "fork");
				winFork = analyzer.getFactory().load(getFileName() + "bridge");
				winFork = analyzer.getFactory().load(getFileName() + "ring");
				// System.out.println("loaded from file: win" +
				// board.getBoardSize()
				// + color.name().toLowerCase());
			} catch (IOException e) {
				initFromScratch(analyzer);
				// System.out.println("loaded from scratch");
			}
		}
	}

	private void initFromScratch(BoardAnalyzer analyzer) {

		// computes bdd representation of the bridge condition
		// System.out.println("computing bridge");
		winFork = getBridgeCondition(analyzer);
		// analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
		// System.out.println("...done");

		// computes bdd representation of the fork condition
		// System.out.println("computing fork");
		winBridge = getForkCondition(analyzer);
		// analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
		// System.out.println("...done");

		// computes bdd representation of the ring condition
		winRing = getRingCondition(analyzer);

		win = winBridge.or(winFork).or(winRing);

		try {
			analyzer.getFactory().save(getFileName(), winFork);
			analyzer.getFactory().save(getFileName(), winBridge);
			analyzer.getFactory().save(getFileName(), winRing);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BDD getBridgeCondition(BoardAnalyzer analyzer) {
		ConditionCalculator calc = new BridgeConditionCalculator(analyzer,
				board, color);
		return calc.getBdd();
	}

	private BDD getForkCondition(BoardAnalyzer analyzer) {
		ConditionCalculator calc = new ForkConditionCalculator(analyzer, board,
				color);
		return calc.getBdd();
	}

	private BDD getRingCondition(BoardAnalyzer analyzer) {
		ConditionCalculator calc = new RingConditionCalculator(analyzer, board,
				color);
		return calc.getBdd();
	}

	private String getFileName() {
		return BDD_FILE_PREFIX + board.getBoardSize()
				+ color.name().toLowerCase();
	}

}
