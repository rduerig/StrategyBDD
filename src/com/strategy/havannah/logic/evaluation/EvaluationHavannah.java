package com.strategy.havannah.logic.evaluation;

import java.util.Collection;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.Situation;
import com.strategy.util.ColorFieldVisitor;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private Situation situation;
	private double avg;
	private int best;

	public EvaluationHavannah(Situation situation) {
		this.situation = situation;
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

	// ************************************************************************

	private void init() {
		Board board = situation.getBoard();
		Collection<Position> positions = board.getPositions();
		Iterables.filter(board.getPositions(), new EmptyPositionFilter(board));
		BDD win = situation.getWinningCondition();
		BDDFactory fac = win.getFactory();
		double sum = 0d;
		double bestValue = 0d;
		for (Position pos : positions) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bdd = win.id();
			bdd.restrictWith(fac.ithVar(field.getIndex()));
			// fac.reorder(BDDFactory.REORDER_SIFT);
			if (null != bdd) {
				Double satCount = bdd.satCount();
				sum += satCount;
				best = satCount > bestValue ? field.getIndex() : best;
				bestValue = satCount > bestValue ? satCount : bestValue;
				bdd.free();
			}
		}

		avg = sum / positions.size();
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
