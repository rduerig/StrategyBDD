package com.strategy.prototype.logic;

import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Maps;
import com.strategy.api.logic.BDDCache;
import com.strategy.api.logic.Position;

/**
 * @author Ralph Dürig
 */
public class BDDCachePrototype implements BDDCache {

	Map<BDDCache.BDDCacheIndex, BDD> cache;

	public BDDCachePrototype() {
		cache = Maps.newHashMap();
	}

	@Override
	public BDD restore(Position p, Position q) {
		BDDCacheIndex key = BDDCacheIndex.getIndex(p, q);
		return cache.get(key);
	}

	@Override
	public void store(Position p, Position q, BDD bdd) {
		BDDCacheIndex key = BDDCacheIndex.getIndex(p, q);
		cache.put(key, bdd);
	}

	@Override
	public boolean isCached(Position p, Position q) {
		BDDCacheIndex key = BDDCacheIndex.getIndex(p, q);
		return cache.containsKey(key);
	}

}
