package com.strategy.api.logic;

import net.sf.javabdd.BDD;

import com.google.common.base.Objects;

/**
 * Provides methods to store and to restore bdds from a cache.
 * 
 * @author Ralph Dürig
 */
public interface BddCache {

	/**
	 * Returns a bdd that was cached before for the given {@link Position}s.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @return a previously cached BDD, is null if no BDD was found in the cache
	 */
	BDD restore(Position p, Position q);

	/**
	 * Stores a given bdd in the cache for the given {@link Position}s. The
	 * returning bdd is the same as the given one.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @param bdd
	 *            the bdd to store
	 * @return the given and stored BDD
	 */
	BDD store(Position p, Position q, BDD bdd);

	/**
	 * Returns if a bdd is cached for the given {@link Position}s.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @return true if a bdd is cached for the given {@link Position}s, false
	 *         otherwise
	 */
	boolean isCached(Position p, Position q);

	/**
	 * Clears the cache.
	 */
	void free();

	// ************************************************************************

	/**
	 * An unique identifier containing two {@link Position}s used to store bdds
	 * in a {@link BddCache}.
	 * 
	 * @author Ralph Dürig
	 */
	class BddCacheIndex {
		private Position p;
		private Position q;

		public static BddCacheIndex getIndex(Position p, Position q) {
			return new BddCacheIndex(p, q);
		}

		private BddCacheIndex(Position p, Position q) {
			this.p = p;
			this.q = q;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(p);
			sb.append("|");
			sb.append(q);
			return sb.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(p, q);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof BddCacheIndex)) {
				return false;
			}
			BddCacheIndex other = (BddCacheIndex) obj;
			if (p == null) {
				if (other.p != null) {
					return false;
				}
			} else if (!p.equals(other.p)) {
				return false;
			}
			if (q == null) {
				if (other.q != null) {
					return false;
				}
			} else if (!q.equals(other.q)) {
				return false;
			}
			return true;
		}

	}

}
