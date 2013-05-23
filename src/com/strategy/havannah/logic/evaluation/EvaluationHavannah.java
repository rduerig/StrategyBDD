package com.strategy.havannah.logic.evaluation;

import java.util.ArrayList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.util.EmptyPositionFilter;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private double[] rating;
	private double avg;
	private int best;
	private Board board;
	private BDD win;

	public EvaluationHavannah(Board board, BDD win) {
		this.board = board;
		this.win = win;
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

	// ************************************************************************

	private void init() {
		ArrayList<Position> filtered = Lists.newArrayList(Iterables.filter(
				board.getPositions(), new EmptyPositionFilter(board)));
		BDDFactory fac = win.getFactory();
		BDD varset = getVarset(fac, board);
		rating = new double[board.getRows() * board.getColumns()];
		double sum = 0d;
		double bestValue = 0d;
		for (Position pos : filtered) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bdd = win.id();
			bdd.restrictWith(fac.ithVar(field.getIndex()));
			// fac.reorder(BDDFactory.REORDER_SIFT);
			if (null != bdd) {
				// Double satCount = bdd.satCount(varset);
				Double satCount = bdd.satCount();
				rating[field.getIndex()] = satCount;
				sum += satCount;
				best = satCount > bestValue ? field.getIndex() : best;
				bestValue = satCount > bestValue ? satCount : bestValue;
				bdd.free();
			}
		}

		avg = sum / filtered.size();
	}

	private BDD getVarset(BDDFactory fac, Board b) {
		// List<BDD> variables = Lists.transform(
		// Lists.newArrayList(b.getPositions()),
		// new PositionToBdd(fac, b.getRows()));
		int[] variables = new int[board.getPositions().size()];
		int i = 0;
		for (Position p : board.getPositions()) {
			variables[i++] = board.getRows() * p.getRow() + p.getCol();
		}
		return fac.makeSet(variables);
	}

	// ************************************************************************

	private class PositionToBdd implements Function<Position, BDD> {

		private final BDDFactory fac;
		private final int rows;

		public PositionToBdd(BDDFactory fac, int rows) {
			this.fac = fac;
			this.rows = rows;
		}

		public BDD apply(Position p) {
			return fac.ithVar(rows * p.getRow() + p.getCol());
		}
	}

}
