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
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private Situation situation;
	private BDDFieldVisitor visitor;

	public EvaluationHavannah(Situation situation) {
		this.situation = situation;
		if (StoneColor.BLACK.equals(situation.getStoneColor())) {
			this.visitor = new WinBlackCheckVisitor(
					situation.getWinningCondition());
		} else {
			this.visitor = new WinWhiteCheckVisitor(
					situation.getWinningCondition());
		}
	}

	@Override
	public Map<Integer, Double> getEvaluatedFields() {
		Map<Integer, Double> result = Maps.newTreeMap();

		Board board = situation.getBoard();
		Collection<Position> positions = board.getPositions();
		for (Position pos : positions) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			field.accept(visitor);
			BDD bdd = visitor.getBDD();
			if (null != bdd) {
				Double satCount = bdd.satCount();
				result.put(field.getIndex(), satCount);
				bdd.free();
			}
		}

		return result;
	}

	// ************************************************************************

	private static class WinWhiteCheckVisitor implements BDDFieldVisitor {

		private BDD bdd;
		private BDD win;
		private BDDFactory fac;

		public WinWhiteCheckVisitor(BDD win) {
			this.win = win;
			this.fac = win.getFactory();
		}

		@Override
		public BDD getBDD() {
			return bdd;
		}

		@Override
		public void visit(EmptyField field) {
			// TODO if black restrict with ...not()
			bdd = win.id().restrict(fac.ithVar(field.getIndex()));
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

	private static class WinBlackCheckVisitor implements BDDFieldVisitor {

		private BDD bdd;
		private BDD win;
		private BDDFactory fac;

		public WinBlackCheckVisitor(BDD win) {
			this.win = win;
			this.fac = win.getFactory();
		}

		@Override
		public BDD getBDD() {
			return bdd;
		}

		@Override
		public void visit(EmptyField field) {
			bdd = win.id().restrict(fac.ithVar(field.getIndex()).not());
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
