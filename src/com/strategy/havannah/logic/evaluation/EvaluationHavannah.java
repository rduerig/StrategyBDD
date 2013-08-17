package com.strategy.havannah.logic.evaluation;

import java.util.ArrayList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.Situation;
import com.strategy.util.Debug;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;
import com.strategy.util.predicates.EmptyPositionFilter;
import com.strategy.util.predicates.ValidPositionFilter;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private double[] rating;
	private double avg;
	private int max;
	private final Board board;
	private final BDD win;
	private final BDD winOpp;
	private final StoneColor color;
	private Logging logRestrictWhite;
	private Logging logRestrictBlack;
	private Logging logId;
	private Logging logSat;

	public static Evaluation create(Situation sit, Situation sitOpp) {
		return new EvaluationHavannah(sit.getBoard(),
				sit.getWinningCondition(), sitOpp.getWinningCondition(),
				sit.getStoneColor());
	}

	public static Evaluation create(Board board, BDD win, BDD winOpp,
			StoneColor color) {
		return new EvaluationHavannah(board, win, winOpp, color);
	}

	private EvaluationHavannah(Board board, BDD win, BDD winOpp,
			StoneColor color) {
		this.board = board;
		this.win = win;
		this.winOpp = winOpp;
		this.color = color;
		avg = 0d;
		max = 0;
		logRestrictWhite = Logging.create("evaluation " + StoneColor.WHITE
				+ " - restrict");
		logRestrictBlack = Logging.create("evaluation " + StoneColor.BLACK
				+ " - restrict");
		logId = Logging.create("evaluation " + color + " - copy win");
		logSat = Logging.create("evaluation " + color + " - sat count");

		String caption = "prediction for " + color;
		Debug initlog = Debug.create(caption);
		init();
		initlog.log();
	}

	@Override
	public double getAverageRating() {
		return avg;
	}

	@Override
	public int getMaxIndex() {
		return max;
	}

	@Override
	public double[] getRating() {
		return rating;
	}

	@Override
	public void log() {
		logRestrictWhite.log();
		logRestrictBlack.log();
		logId.log();
		logSat.log();
	}

	// ************************************************************************

	private void init() {
		Iterable<Position> empty = Iterables.filter(board.getPositions(),
				new EmptyPositionFilter(board));
		Iterable<Position> validEmpty = Iterables.filter(empty,
				new ValidPositionFilter(board));
		ArrayList<Position> filtered = Lists.newArrayList(validEmpty);
		BDDFactory fac = win.getFactory();
		rating = new double[board.getRows() * board.getColumns()];
		double sum = 0d;
		double maxValue = 0d;
		for (Position pos : filtered) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bddWin = logId.id(win);
			BDD bddWinOpp = logId.id(winOpp);
			if (StoneColor.WHITE.equals(color)) {
				bddWin = logRestrictWhite.restrictLog(bddWin,
						fac.ithVar(field.getIndex()));
				bddWinOpp = logRestrictWhite.restrictLog(bddWinOpp,
						fac.ithVar(field.getIndex()));
			} else {
				bddWin = logRestrictBlack.restrictLog(bddWin,
						fac.nithVar(field.getIndex()));
				bddWinOpp = logRestrictBlack.restrictLog(bddWinOpp,
						fac.nithVar(field.getIndex()));
			}
			double valuation = (logSat.satCountLog(bddWin) - logSat
					.satCountLog(bddWinOpp)) / Math.pow(2, filtered.size());
			rating[field.getIndex()] = valuation;
			sum += valuation;
			if (valuation > maxValue) {
				max = field.getIndex();
				maxValue = valuation;
			}
			bddWin.free();
			bddWinOpp.free();
		}

		avg = sum / filtered.size();
	}

}
