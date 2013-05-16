package com.strategy.havannah.logic.situation;

import java.io.File;
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
	private BDD win;
	private Board board;
	private StoneColor color;

	public SituationHavannah(BoardAnalyzer analyzer,
			BoardAnalyzer analyzerOpposite, Board board) {
		this.board = board;
		this.color = analyzer.getStoneColor();
		init(analyzer, analyzerOpposite);
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

	private void init(BoardAnalyzer analyzer, BoardAnalyzer analyzerOpposite) {
		// System.out.println("try loading from file: win" +
		// board.getBoardSize()
		// + color.name().toLowerCase());
		File winFile = new File(getFileName());
		if (Preferences.getInstance().isGenerateFiles() || !winFile.exists()) {
			initFromScratch(analyzer, analyzerOpposite);
		} else {
			try {
				win = analyzer.getFactory().load(getFileName());
				// System.out.println("loaded from file: win" +
				// board.getBoardSize()
				// + color.name().toLowerCase());
			} catch (IOException e) {
				initFromScratch(analyzer, analyzerOpposite);
				// System.out.println("loaded from scratch");
			}
		}
	}

	private void initFromScratch(BoardAnalyzer analyzer,
			BoardAnalyzer analyzerOpposite) {

		win = analyzer.getFactory().zero();

		// computes bdd representation of the bridge condition
		// System.out.println("computing bridge");
		win.orWith(getBridgeCondition(analyzer));
		// analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
		// System.out.println("...done");

		// computes bdd representation of the fork condition
		// System.out.println("computing fork");
		win.orWith(getForkCondition(analyzer));
		// analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
		// System.out.println("...done");

		// computes bdd representation of the ring condition
		win.orWith(getRingCondition(analyzer, analyzerOpposite));

		try {
			analyzer.getFactory().save(getFileName(), win);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BDD getBridgeCondition(BoardAnalyzer analyzer) {
		ConditionCalculator calc = new BridgeConditionCalculator(analyzer,
				board);
		return calc.getBdd();
	}

	private BDD getForkCondition(BoardAnalyzer analyzer) {
		ConditionCalculator calc = new ForkConditionCalculator(analyzer, board);
		return calc.getBdd();
	}

	private BDD getRingCondition(BoardAnalyzer analyzer,
			BoardAnalyzer analyzerOpposite) {
		ConditionCalculator calc = new RingConditionCalculator(analyzer,
				analyzerOpposite, board);
		return calc.getBdd();
	}

	private String getFileName() {
		return BDD_FILE_PREFIX + board.getBoardSize()
				+ color.name().toLowerCase();
	}

}
