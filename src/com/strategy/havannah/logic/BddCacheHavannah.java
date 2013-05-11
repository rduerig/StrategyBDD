package com.strategy.havannah.logic;

import net.sf.javabdd.BDD;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.strategy.api.HasDebugFlag;
import com.strategy.api.logic.BddCache;
import com.strategy.api.logic.Position;
import com.strategy.util.Output;

/**
 * @author Ralph Dürig
 */
public class BddCacheHavannah implements BddCache, HasDebugFlag {

	private Cache<BddCacheIndex, BDD> cache;

	public BddCacheHavannah() {
		CacheBuilder<Object, Object> cb = CacheBuilder.newBuilder();
		cb.recordStats();
		// cb.softValues();
		cache = cb.build();
		// cache = HashBasedTable.create();
	}

	@Override
	public BDD restore(Position p, Position q, int i) {
		// return cache.get(p, q).id();
		return cache.getIfPresent(BddCacheIndex.getIndex(p, q, i)).id();
	}

	@Override
	public BDD store(Position p, Position q, int i, BDD bdd) {
		if (null == bdd) {
			return null;
		}
		// cache.put(p, q, bdd);
		cache.put(BddCacheIndex.getIndex(p, q, i), bdd);
		return bdd.id();
	}

	@Override
	public boolean isCached(Position p, Position q, int i) {
		// return cache.contains(p, q);
		return cache.asMap().containsKey(BddCacheIndex.getIndex(p, q, i));
	}

	@Override
	public void free() {
		// cache.clear();
		Output.print(cache.stats().toString(), BddCacheHavannah.class);
		cache.cleanUp();
		cache.invalidateAll();
	}

}
