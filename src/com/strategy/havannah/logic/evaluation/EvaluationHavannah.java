package com.strategy.havannah.logic.evaluation;

import java.util.Collection;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Maps;
import com.strategy.api.board.Board;
import com.strategy.api.field.BDDFieldVisitor;
import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.WhiteStone;
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
	private BDDFieldVisitor visitor;

	public EvaluationHavannah(Situation situation) {
		this.situation = situation;
		// this.visitor = new WinCheckVisitor(situation.getWinningCondition(),
		// situation.getStoneColor());
	}

	@Override
	public Map<Integer, Double> getEvaluatedFields() {
		Map<Integer, Double> result = Maps.newHashMap();

		Board board = situation.getBoard();
		Collection<Position> positions = board.getPositions();
		BDD win = situation.getWinningCondition();
		BDDFactory fac = win.getFactory();
		ColorFieldVisitor colorVisitor = new ColorFieldVisitor();
		for (Position pos : positions) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			// field.accept(visitor);
			// BDD bdd = visitor.getBDD();
			BDD bdd = win.id();
			field.accept(colorVisitor);
			if (StoneColor.EMPTY.equals(colorVisitor.getColor())) {
				// if (StoneColor.WHITE.equals(situation.getStoneColor())) {
				bdd.restrictWith(fac.ithVar(field.getIndex()));
				// } else {
				// bdd.restrictWith(fac.ithVar(field.getIndex()).not());
				// }
				// fac.reorder(BDDFactory.REORDER_SIFT);
				if (null != bdd) {
					Double satCount = bdd.satCount();
					result.put(field.getIndex(), satCount);
					bdd.free();
				}
			}
		}

		return result;
	}

	// ************************************************************************

	private static class WinCheckVisitor implements BDDFieldVisitor {

		private BDD bdd;
		private BDD win;
		private BDDFactory fac;
		private StoneColor color;

		public WinCheckVisitor(BDD win, StoneColor color) {
			this.win = win;
			this.color = color;
			this.fac = win.getFactory();
			// fac.reorderVerbose(0);
		}

		@Override
		public BDD getBDD() {
			return bdd;
		}

		@Override
		public void visit(EmptyField field) {
			if (StoneColor.WHITE.equals(color)) {
				bdd = win.id().restrictWith(fac.ithVar(field.getIndex()));
			} else {
				bdd = win.id().restrictWith(fac.ithVar(field.getIndex()).not());
			}
			// fac.reorder(BDDFactory.REORDER_SIFT);
		}

		@Override
		public void visit(WhiteStone field) {
			bdd = null;

		}

		@Override
		public void visit(BlackStone field) {
			bdd = null;

		}

	}

}
