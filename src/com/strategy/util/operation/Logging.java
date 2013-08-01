package com.strategy.util.operation;

import java.io.PrintStream;

import net.sf.javabdd.BDD;

import com.strategy.util.preferences.Preferences;

/**
 * @author Ralph Dürig
 */
public class Logging {

	private PrintStream out;
	private String caption;
	private int sumTimes;
	private double sumDurations;
	private long sumNodes;

	/**
	 * Creates a {@link Logging} that logs to {@link Preferences#getOut()}. If
	 * no debug stream was given the logging object performs no output. No
	 * warning or error is thrown in this case.
	 */
	public static Logging create(String caption) {
		return new Logging(Preferences.getInstance().getOut(), caption);
	}

	// ************************************************************************

	private Logging(PrintStream out, String caption) {
		this.out = out;
		this.caption = caption;
		sumDurations = 0;
		sumNodes = 0;
		sumTimes = 0;
	}

	public BDD andLog(BDD x, BDD y) {
		return applyOp(x, y, Op.AND);
	}

	public BDD orLog(BDD x, BDD y) {
		return applyOp(x, y, Op.OR);
	}

	public BDD restrictLog(BDD x, BDD y) {
		return applyOp(x, y, Op.RESTRICT);
	}

	public BDD id(BDD x) {
		return applyOp(x, null, Op.ID);
	}

	public double satCountLog(BDD x) {
		return applyOp(x, UnaryOp.SATS);
	}

	public double pathCountLog(BDD x) {
		return applyOp(x, UnaryOp.PATHS);
	}

	public double nodeCountLog(BDD x) {
		return applyOp(x, UnaryOp.NODES);
	}

	public void log() {
		// ignore if no output stream was given
		if (null == out) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(caption);
		sb.append(": ");
		sb.append(sumTimes);
		sb.append(" times, ");
		sb.append(sumDurations);
		sb.append(" microsec, ");
		sb.append(sumNodes);
		sb.append(" nodes");

		out.println(sb.toString());
	}

	// ************************************************************************

	private BDD applyOp(BDD x, BDD y, Op op) {
		sumTimes++;
		int nodesBefore = x.nodeCount();
		long millisBefore = System.nanoTime();
		BDD result = op.apply(x, y);
		long millisAfter = System.nanoTime();
		int nodesAfter = result.nodeCount();
		sumDurations += (millisAfter - millisBefore) / 1000;
		sumNodes += nodesAfter - nodesBefore;
		return result;
	}

	private double applyOp(BDD x, UnaryOp op) {
		sumTimes++;
		long millisBefore = System.nanoTime();
		double result = op.apply(x);
		long millisAfter = System.nanoTime();
		sumDurations += (millisAfter - millisBefore) / 1000;
		return result;
	}
}
