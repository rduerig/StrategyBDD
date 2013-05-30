package com.strategy.havannah.logic;

import java.util.Map;

import net.sf.javabdd.BDD;

import com.google.common.collect.Maps;
import com.strategy.api.HasDebugFlag;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.Position;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class BddCacheHavannah implements BddCache, HasDebugFlag {

	// private Cache<BddCacheIndex, BDD> cache;
	private Map<BddCacheIndex, BDD> cache;

	public BddCacheHavannah() {
		// CacheBuilder<Object, Object> cb = CacheBuilder.newBuilder();
		// cb.recordStats();
		// cb.softValues();
		// cache = cb.build();
		cache = Maps.newHashMap();
	}

	@Override
	public BDD restore(StoneColor color, Position p, Position q, int i) {
		// return cache.getIfPresent(BddCacheIndex.getIndex(p, q, i)).id();
		return cache.get(BddCacheIndex.getIndex(color, p, q, i)).id();
	}

	@Override
	public BDD store(StoneColor color, Position p, Position q, int i, BDD bdd) {
		if (null == bdd) {
			return null;
		}
		cache.put(BddCacheIndex.getIndex(color, p, q, i), bdd);
		return bdd.id();
	}

	@Override
	public boolean isCached(StoneColor color, Position p, Position q, int i) {
		// return null != cache.getIfPresent(BddCacheIndex.getIndex(p, q, i));
		return cache.containsKey(BddCacheIndex.getIndex(color, p, q, i));
	}

	@Override
	public void free() {
		cache.clear();
		// Output.print(cache.stats().toString(), BddCacheHavannah.class);
		// cache.cleanUp();
		// cache.invalidateAll();
	}

}
