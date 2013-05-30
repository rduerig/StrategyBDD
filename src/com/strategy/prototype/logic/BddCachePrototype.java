package com.strategy.prototype.logic;

import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Maps;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.Position;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class BddCachePrototype implements BddCache {

	private Map<BddCacheIndex, BDD> cache;

	public BddCachePrototype() {
		cache = Maps.newHashMap();
	}

	@Override
	public BDD restore(StoneColor color, Position p, Position q, int i) {
		BddCacheIndex key = BddCacheIndex.getIndex(color, p, q, i);
		return cache.get(key).id();
	}

	@Override
	public BDD store(StoneColor color, Position p, Position q, int i, BDD bdd) {
		BddCacheIndex key = BddCacheIndex.getIndex(color, p, q, i);
		cache.put(key, bdd.id());
		return bdd.id();
	}

	@Override
	public boolean isCached(StoneColor color, Position p, Position q, int i) {
		BddCacheIndex key = BddCacheIndex.getIndex(color, p, q, i);
		return cache.containsKey(key);
	}

	@Override
	public void free() {
		cache.clear();
	}

}
