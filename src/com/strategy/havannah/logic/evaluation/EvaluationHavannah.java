package com.strategy.havannah.logic.evaluation;

import java.util.Collection;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Maps;
import com.strategy.api.board.Board;
import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.FieldVisitor;
import com.strategy.api.field.WhiteStone;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.situation.Situation;

/**
 * @author Ralph Dürig
 */
public class EvaluationHavannah implements Evaluation {

	private Situation situation;
	private WinCheckVisitor visitor;

	public EvaluationHavannah(Situation situation) {
		this.situation = situation;
		this.visitor = new WinCheckVisitor(situation.getWinningCondition());
	}

	@Override
	public Map<Integer, Double> getEvaluatedFields() {
		Map<Integer, Double> result = Maps.newTreeMap();

		Board board = situation.getBoard();
		Collection<Position> positions = board.getPositions();
		for (Position pos : positions) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			field.accept(visitor);
			BDD bdd = visitor.getBdd();
			if (null != bdd) {
				Double satCount = bdd.satCount();
				result.put(field.getIndex(), satCount);
			}
		}

		// TODO do this for two situations, one for white and one for black

		return result;
	}

	// ************************************************************************

	private static class WinCheckVisitor implements FieldVisitor {

		private BDD bdd;
		private BDD win;
		private BDDFactory fac;

		public WinCheckVisitor(BDD win) {
			this.win = win;
			this.fac = win.getFactory();
		}

		public BDD getBdd() {
			return bdd;
		}

		@Override
		public void visit(EmptyField field) {
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

}
