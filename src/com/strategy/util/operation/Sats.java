package com.strategy.util.operation;

import java.util.Collection;

import net.sf.javabdd.BDD;

import com.google.common.collect.Collections2;
import com.strategy.util.predicates.ValidPositionFilter;
import com.strategy.util.preferences.Preferences;
import com.strategy.api.logic.Position;
import com.strategy.util.PrimitiveBoardProvider;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.api.board.Board;
import java.util.Collection;

public class Sats implements UnaryOp {

	@Override
	public double apply(BDD x) {
		BDD varset = getVarset(x);
		//return x.satCount(varset);
		return x.satCount(varset);
	}

	private BDD getVarset(BDD x) {
		int s = Preferences.getInstance().getBoardSize();
		Board board = BoardHavannah.createInstance(PrimitiveBoardProvider.getBoard(s), s);
		Collection<Position> allPos = board.getPositions();
		Collection<Position> filtered = Collections2.filter(allPos,
				new ValidPositionFilter(board));
		int[] varset = new int[filtered.size()];
		int count = 0;
		for (Position p : filtered) {
			varset[count++] = board.getField(p.getRow(), p.getCol()).getIndex();
		}
		return x.getFactory().makeSet(varset);
	}

}
