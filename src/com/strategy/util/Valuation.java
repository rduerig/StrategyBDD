package com.strategy.util;

import net.sf.javabdd.BDD;

import com.strategy.util.operation.Logging;

/**
 * Provides methods for evaluation.
 * 
 * @author Ralph Dürig
 */
public class Valuation {

	/**
	 * Computes a value for two BDDs using the formula (#w - #b) / (2^s) where s
	 * denotes the amount of board cells.
	 * 
	 * @param w
	 *            BDD for white
	 * @param b
	 *            BDD for black
	 * @param s
	 *            amount of cells
	 * @return a value according to the situation represented by the given
	 *         arguments
	 */
	public static double compute(BDD w, BDD b, int s) {
		Logging logSat = Logging.create("valuation sat count");
		double result = (logSat.satCountLog(w) - logSat.satCountLog(b))
				/ Math.pow(2, s);
		logSat.log();
		return result;
	}

}
