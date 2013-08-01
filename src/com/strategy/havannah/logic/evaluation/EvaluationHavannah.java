package com.strategy.havannah.logic.evaluation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.Situation;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;
import com.strategy.util.predicates.EmptyPositionFilter;
import com.strategy.util.predicates.ValidPositionFilter;
import com.strategy.util.preferences.Preferences;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private double[] rating;
	private double avg;
	private int best;
	private Board board;
	private BDD win;
	private StoneColor color;
	private Logging logRestrictWhite;
	private Logging logRestrictBlack;

	public static Evaluation create(Situation sit) {
		return new EvaluationHavannah(sit.getBoard(),
				sit.getWinningCondition(), sit.getStoneColor());
	}

	public static Evaluation create(Board board, BDD win, StoneColor color) {
		return new EvaluationHavannah(board, win, color);
	}

	private EvaluationHavannah(Board board, BDD win, StoneColor color) {
		this.board = board;
		this.win = win;
		this.color = color;
		avg = 0d;
		best = 0;
		logRestrictWhite = Logging.create("evaluation " + StoneColor.WHITE
				+ " - restrict");
		logRestrictBlack = Logging.create("evaluation " + StoneColor.BLACK
				+ " - restrict");

		PrintStream out = Preferences.getInstance().getOut();
		if (null != out) {
			long tBefore = System.nanoTime();
			init();
			long tAfter = System.nanoTime();
			double diff = tAfter - tBefore;
			out.println("prediction for " + color + " took: " + diff / 1000
					+ " microsec");
		} else {
			init();
		}
	}

	@Override
	public double getAverageRating() {
		return avg;
	}

	@Override
	public int getBestIndex() {
		return best;
	}

	@Override
	public double[] getRating() {
		return rating;
	}

	@Override
	public void log() {
		logRestrictWhite.log();
		logRestrictBlack.log();
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
		double bestValue = 0d;
		BDD varset = getVarset();
		for (Position pos : filtered) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bddWin = win.id();
			if (StoneColor.WHITE.equals(color)) {
				// bddWin.restrictWith(fac.ithVar(field.getIndex()));
				logRestrictWhite.restrictLog(bddWin,
						fac.ithVar(field.getIndex()));
			} else {
				// bddWin.restrictWith(fac.nithVar(field.getIndex()));
				logRestrictBlack.restrictLog(bddWin,
						fac.nithVar(field.getIndex()));
			}
			double valuation = bddWin.satCount(varset);
			rating[field.getIndex()] = valuation;
			sum += valuation;
			if (valuation > bestValue) {
				best = field.getIndex();
				bestValue = valuation;
			}
			bddWin.free();
		}

		avg = sum / filtered.size();
	}

	private BDD getVarset() {
		Collection<Position> allPos = board.getPositions();
		Collection<Position> filtered = Collections2.filter(allPos,
				new ValidPositionFilter(board));
		int[] varset = new int[filtered.size()];
		int count = 0;
		for (Position p : filtered) {
			varset[count++] = board.getField(p.getRow(), p.getCol()).getIndex();
		}
		return win.getFactory().makeSet(varset);
	}

}
