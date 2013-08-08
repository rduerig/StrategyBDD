package com.strategy.api.logic;

import net.sf.javabdd.BDD;

import com.google.common.base.Objects;
import com.strategy.api.HasDebugFlag;
import com.strategy.util.StoneColor;

/**
 * Provides methods to store and to restore bdds from a cache.
 * 
 * @author Ralph Dürig
 */
public interface BddCache extends HasDebugFlag {

	/**
	 * Returns a bdd that was cached before for the given {@link Position}s.
	 * Note that it's assumed that the bdd for q and p means the same as the bdd
	 * for p and q so the returned bdd may be the bdd for q and p.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @param i
	 *            recursion index i
	 * @return a previously cached BDD, is null if no BDD was found in the cache
	 */
	BDD restore(StoneColor color, Position p, Position q, int i);

	/**
	 * Stores a given bdd in the cache for the given {@link Position}s. The
	 * returning bdd is the same as the given one.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @param i
	 *            recursion index i
	 * @param bdd
	 *            the bdd to store
	 * @return the given and stored BDD
	 */
	BDD store(StoneColor color, Position p, Position q, int i, BDD bdd);

	/**
	 * Returns if a bdd is cached for the given {@link Position}s in given order
	 * or in reversed order.
	 * 
	 * @param p
	 *            Position p
	 * @param q
	 *            Position q
	 * @param i
	 *            recursion index i
	 * @return true if a bdd is cached for the given {@link Position}s, false
	 *         otherwise
	 */
	boolean isCached(StoneColor color, Position p, Position q, int i);

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
	public class BddCacheIndex {
		private StoneColor color;
		private Position p;
		private Position q;
		private Integer i;

		public static BddCacheIndex getIndex(StoneColor color, Position p,
				Position q, int i) {
			return new BddCacheIndex(color, p, q, i);
		}

		private BddCacheIndex(StoneColor color, Position p, Position q, int i) {
			this.color = color;
			this.p = p;
			this.q = q;
			this.i = i;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(color);
			sb.append("|");
			sb.append(p);
			sb.append("|");
			sb.append(q);
			sb.append("|");
			sb.append(i);
			return sb.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(color, p, q, i);
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
			if (color == null) {
				if (other.color != null) {
					return false;
				}
			} else if (!color.equals(other.color)) {
				return false;
			}
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
			if (i == null) {
				if (other.i != null) {
					return false;
				}
			} else if (!i.equals(other.i)) {
				return false;
			}
			return true;
		}

	}

	class BDDCacheStatus {
		private long stores;
		private long restores;

		public BDDCacheStatus() {
			stores = 0;
			restores = 0;
		}

		public void incrementStores() {
			stores++;
		}

		public void incrementRestores() {
			restores++;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Stores: ");
			sb.append(stores);
			sb.append(" | ");
			sb.append("Restores: ");
			sb.append(restores);
			return sb.toString();
		}
	}

}
