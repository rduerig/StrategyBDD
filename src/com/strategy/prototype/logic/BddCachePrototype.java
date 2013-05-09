package com.strategy.prototype.logic;

import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Maps;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.Position;

/**
 * @author Ralph Dürig
 */
public class BddCachePrototype implements BddCache {

	private Map<BddCacheIndex, BDD> cache;

	public BddCachePrototype() {
		cache = Maps.newHashMap();
	}

	@Override
	public BDD restore(Position p, Position q, int i) {
		BddCacheIndex key = BddCacheIndex.getIndex(p, q, i);
		return cache.get(key).id();
	}

	@Override
	public BDD store(Position p, Position q, int i, BDD bdd) {
		BddCacheIndex key = BddCacheIndex.getIndex(p, q, i);
		cache.put(key, bdd.id());
		return bdd.id();
	}

	@Override
	public boolean isCached(Position p, Position q, int i) {
		BddCacheIndex key = BddCacheIndex.getIndex(p, q, i);
		return cache.containsKey(key);
	}

	@Override
	public void free() {
		cache.clear();
	}

}
