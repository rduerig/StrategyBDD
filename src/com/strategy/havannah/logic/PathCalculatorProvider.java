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
			private PathCalculator calc;

			@Override
			public PathCalculator provide(BDDFactory fac, Board board) {
				if (null == calc) {
					calc = new PathsRec(fac, board);
				}
				return calc;
			}
		},

		ITERATIVE("iter") {
			private PathCalculator calc;

			@Override
			public PathCalculator provide(BDDFactory fac, Board board) {
				if (null == calc) {
					calc = new PathsIter(fac, board);
				}
				return calc;
			}
		};

		private final String key;

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
