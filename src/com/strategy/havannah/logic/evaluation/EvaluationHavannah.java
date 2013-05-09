package com.strategy.havannah.logic.evaluation;

import java.util.ArrayList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.util.ColorFieldVisitor;
import com.strategy.util.StoneColor;

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
		rating = new double[board.getRows() * board.getColumns()];
		double sum = 0d;
		double bestValue = 0d;
		for (Position pos : filtered) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bdd = win.id();
			bdd.restrictWith(fac.ithVar(field.getIndex()));
			// fac.reorder(BDDFactory.REORDER_SIFT);
			if (null != bdd) {
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

	// ************************************************************************

	private static class EmptyPositionFilter implements Predicate<Position> {

		private Board board;
		private ColorFieldVisitor colorVisitor;

		public EmptyPositionFilter(Board board) {
			this.board = board;
			this.colorVisitor = new ColorFieldVisitor();
		}

		@Override
		public boolean apply(Position input) {
			Field field = board.getField(input.getRow(), input.getCol());
			field.accept(colorVisitor);
			return StoneColor.EMPTY.equals(colorVisitor.getColor());
		}
	}
}
