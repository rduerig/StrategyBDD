package com.strategy.havannah.logic;

import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;
import com.strategy.api.logic.PathCalculator;

/**
 * @author Ralph Dürig
 */
public class PathCalculatorProvider {

	public enum PathCalculatorKey {
		RECURSIVE("rec") {
			@Override
			public PathCalculator provide(BDDFactory fac, Board board) {
				return new PathsRec(fac, board);
			}
		},

		ITERATIVE("iter") {
			@Override
			public PathCalculator provide(BDDFactory fac, Board board) {
				return new PathsIter(fac, board);
			}
		};

		private String key;

		private PathCalculatorKey(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public static PathCalculatorKey parse(String key) {
			for (PathCalculatorKey val : values()) {
				if (val.getKey().equalsIgnoreCase(key)) {
					return val;
				}
			}

			return RECURSIVE;
		}

		public abstract PathCalculator provide(BDDFactory fac, Board board);
	}

}
