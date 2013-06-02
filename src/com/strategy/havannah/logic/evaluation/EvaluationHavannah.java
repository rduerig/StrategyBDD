package com.strategy.havannah.logic.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
	private BDD bridge;
	private BDD fork;
	private BDD bestBdd;
	private BDD ring;
	private BDD varsetBridge;
	private BDD varsetFork;
	private BDD varsetRing;

	public EvaluationHavannah(Board board, BDD bridge, BDD fork, BDD ring) {
		this.board = board;
		this.bridge = bridge;
		this.varsetBridge = bridge.fullSatOne();
		this.fork = fork;
		this.varsetFork = fork.fullSatOne();
		this.ring = ring;
		this.varsetRing = ring.fullSatOne();
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
		ArrayList<Position> filtered = Lists.newArrayList(Iterables.filter(
				board.getPositions(), new EmptyPositionFilter(board)));
		BDDFactory fac = bridge.getFactory();
		rating = new double[board.getRows() * board.getColumns()];
		double sum = 0d;
		double bestValue = 0d;
		for (Position pos : filtered) {
			Field field = board.getField(pos.getRow(), pos.getCol());
			BDD bddBridge = bridge.id();
			bddBridge.restrictWith(fac.ithVar(field.getIndex()));
			BDD bddFork = fork.id();
			bddFork.restrictWith(fac.ithVar(field.getIndex()));
			BDD bddRing = ring.id();
			bddRing.restrictWith(fac.nithVar(field.getIndex()));
			// fac.reorder(BDDFactory.REORDER_SIFT);
			Map<BDD, Double> sat = Maps.newHashMap();
			sat.put(bddBridge, bddBridge.satCount(varsetBridge));
			sat.put(bddFork, bddFork.satCount(varsetFork));
			sat.put(bddRing, bddRing.satCount(varsetRing));
			Entry<BDD, Double> valuation = Collections.max(sat.entrySet(),
					new EntryComparator());
			rating[field.getIndex()] = valuation.getValue();
			sum += valuation.getValue();
			if (valuation.getValue() > bestValue) {
				best = field.getIndex();
				bestValue = valuation.getValue();
				bestBdd = valuation.getKey().id();
			}
			bddBridge.free();
			bddFork.free();
			bddRing.free();
		}

		avg = sum / filtered.size();
	}

	// ************************************************************************

	private class EntryComparator implements Comparator<Entry<BDD, Double>> {

		@Override
		public int compare(Entry<BDD, Double> o1, Entry<BDD, Double> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}

	}
}
