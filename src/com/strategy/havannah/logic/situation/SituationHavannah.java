package com.strategy.havannah.logic.situation;

import java.io.IOException;
import java.io.PrintStream;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.FieldGenerator;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;
import com.strategy.util.preferences.Preferences;

/**
 * @author Ralph Dürig
 */
public class SituationHavannah implements Situation {

	private static final String BDD_FILE_PREFIX = "win";
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
	public Board getBoard() {
		return board;
	}

	@Override
	public StoneColor getStoneColor() {
		return color;
	}

	@Override
	public void update(int fieldIndex, StoneColor color) {
		Field field = FieldGenerator.create(
				color.getPrimitive(),
				PositionHexagon.get(fieldIndex / board.getRows(), fieldIndex
						% board.getColumns()), fieldIndex);
		board.setField(field);
		BDDFactory fac = win.getFactory();
		Logging logUpdate;
		String capPrefix = this.color + "'s situation updated for ";
		String capSuffix = "'s move";
		if (StoneColor.WHITE.equals(color)) {
			logUpdate = Logging.create(capPrefix + color + capSuffix);
			logUpdate.restrictLog(win, fac.ithVar(field.getIndex()));
		} else {
			logUpdate = Logging.create(capPrefix + color + capSuffix);
			logUpdate.restrictLog(win, fac.nithVar(field.getIndex()));
		}
		logUpdate.log();

	}

	// ************************************************************************

	private void init(BoardAnalyzer analyzer) {
		PrintStream out = Preferences.getInstance().getOut();
		if (Preferences.getInstance().isGenerateFiles()) {
			if (null != out) {
				long tBefore = System.nanoTime();
				initFromScratch(analyzer);
				long tAfter = System.nanoTime();
				double diff = (tAfter - tBefore) / 1000;
				out.println("BDD initialization from scratch took: " + diff);
			} else {
				initFromScratch(analyzer);
			}
		} else {
			try {
				if (null != out) {
					long tBefore = System.nanoTime();
					win = analyzer.getFactory().load(getFileName());
					long tAfter = System.nanoTime();
					double diff = (tAfter - tBefore) / 1000;
					out.println("BDD loading from files took: " + diff);
				} else {
					win = analyzer.getFactory().load(getFileName());
				}
			} catch (IOException e) {
				System.out
						.println("Could not load files, BDDs are generated vom scratch.");
				if (null != out) {
					long tBefore = System.nanoTime();
					initFromScratch(analyzer);
					long tAfter = System.nanoTime();
					double diff = (tAfter - tBefore) / 1000;
					out.println("BDD initialization from scratch after files not found took: "
							+ diff);
				} else {
					initFromScratch(analyzer);
				}
			}
		}
	}

	private void initFromScratch(BoardAnalyzer analyzer) {

		BDD b = getBridgeCondition(analyzer);
		BDD f = getForkCondition(analyzer);
		BDD r = getRingCondition(analyzer);

		Logging logBF = Logging.create("bridge OR fork");
		Logging logBFR = Logging.create("bf OR ring");
		win = logBFR.orLog(logBF.orLog(b, f), r);
		logBF.log();
		logBFR.log();

		try {
			analyzer.getFactory().save(getFileName(), win);
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
				color.getOpposite());
		return calc.getBdd();
	}

	private String getFileName() {
		return BDD_FILE_PREFIX + board.getBoardSize()
				+ color.name().toLowerCase();
	}

}
