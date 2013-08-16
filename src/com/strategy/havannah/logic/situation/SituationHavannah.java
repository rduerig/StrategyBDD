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
import com.strategy.util.Debug;
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
			win = logUpdate.restrictLog(win, fac.ithVar(field.getIndex()));
		} else {
			logUpdate = Logging.create(capPrefix + color + capSuffix);
			win = logUpdate.restrictLog(win, fac.nithVar(field.getIndex()));
		}
		logUpdate.log();

	}

	// ************************************************************************

	private void init(BoardAnalyzer analyzer) {
		if (Preferences.getInstance().isGenerateFiles()) {
			Debug initlog = Debug.create("BDD initialization from scratch");
			initFromScratch(analyzer);
			initlog.log();
		} else {
			try {
				Debug loadlog = Debug.create("BDD loading from files");
				win = analyzer.getFactory().load(getFileName());
				loadlog.log();
			} catch (IOException e) {
				System.out
						.println("Could not load files, BDDs are generated vom scratch.");
				Debug initlog = Debug
						.create("BDD initialization from scratch after files not found");
				initFromScratch(analyzer);
				initlog.log();
			}
		}
	}

	private void initFromScratch(BoardAnalyzer analyzer) {

		Debug blog = Debug.create("creating bridge for " + color);
		BDD b = getBridgeCondition(analyzer);
		blog.log();
		Debug flog = Debug.create("creating fork for " + color);
		BDD f = getForkCondition(analyzer);
		flog.log();
		Debug rlog = Debug.create("creating ring for " + color);
		BDD r = getRingCondition(analyzer);
		rlog.log();

		Logging logBF = Logging.create("bridge OR fork");
		Logging logBFR = Logging.create("bf OR ring");
		win = logBFR.orLog(logBF.orLog(b, f), r);
		logBF.log();
		logBFR.log();

		if (!Preferences.getInstance().isAvoidFiles()) {
			try {
				analyzer.getFactory().save(getFileName(), win);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
