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
import com.strategy.util.StoneColor;
import com.strategy.util.predicates.EmptyPositionFilter;
import com.strategy.util.predicates.ValidPositionFilter;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private double[] rating;
	private double avg;
	private int best;
	private Board board;
	private BDD win;
	private BDD varset;
	private BDD bestBdd;
	private StoneColor color;

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
		this.varset = win.fullSatOne();
		this.color = color;
		avg = 0d;
		best = 0;
		init();
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
	public BDD getBestBdd() {
		return bestBdd;
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
		for (Position pos : filtered) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bddWin = win.id();
			if (StoneColor.WHITE.equals(color)) {
				bddWin.restrictWith(fac.ithVar(field.getIndex()));
			} else {
				bddWin.restrictWith(fac.nithVar(field.getIndex()));
			}
			double valuation = bddWin.satCount();
			rating[field.getIndex()] = valuation;
			sum += valuation;
			if (valuation > bestValue) {
				best = field.getIndex();
				bestValue = valuation;
				bestBdd = bddWin.id();
			}
			bddWin.free();
		}

		avg = sum / filtered.size();
	}

}
