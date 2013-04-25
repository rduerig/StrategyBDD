package com.strategy.havannah.logic;

import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Maps;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.Position;

/**
 * @author Ralph Dürig
 */
public class BddCacheHavannah implements BddCache {

	private Map<BddCacheIndex, BDD> cache;

	public BddCacheHavannah() {
		cache = Maps.newHashMap();
	}

	@Override
	public BDD restore(Position p, Position q) {
		BddCacheIndex key = BddCacheIndex.getIndex(p, q);
		return cache.get(key).id();
	}

	@Override
	public BDD store(Position p, Position q, BDD bdd) {
		if (null == bdd) {
			return null;
		}
		BddCacheIndex key = BddCacheIndex.getIndex(p, q);
		cache.put(key, bdd);
		// return bdd.id().simplify(bdd);
		return bdd.id();
	}

	@Override
	public boolean isCached(Position p, Position q) {
		BddCacheIndex key = BddCacheIndex.getIndex(p, q);
		return cache.containsKey(key);
	}

}
