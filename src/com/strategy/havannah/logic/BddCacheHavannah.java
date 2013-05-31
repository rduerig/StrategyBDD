package com.strategy.havannah.logic;

import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Maps;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.Position;
import com.strategy.util.Output;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class BddCacheHavannah implements BddCache {

	private Map<BddCacheIndex, BDD> cache;
	private BDDCacheStatus stats;

	public BddCacheHavannah() {
		cache = Maps.newHashMap();
		stats = new BDDCacheStatus();
	}

	@Override
	public BDD restore(StoneColor color, Position p, Position q, int i) {
		stats.incrementRestores();
		return cache.get(BddCacheIndex.getIndex(color, p, q, i)).id();
	}

	@Override
	public BDD store(StoneColor color, Position p, Position q, int i, BDD bdd) {
		stats.incrementStores();
		if (null == bdd) {
			return null;
		}
		cache.put(BddCacheIndex.getIndex(color, p, q, i), bdd);
		return bdd.id();
	}

	@Override
	public boolean isCached(StoneColor color, Position p, Position q, int i) {
		return cache.containsKey(BddCacheIndex.getIndex(color, p, q, i));
	}

	@Override
	public void free() {
		Output.print(stats.toString(), BddCacheHavannah.class);
		cache.clear();
	}

}
