package com.strategy.util;

import java.io.PrintStream;

import com.strategy.util.preferences.Preferences;

public class Debug {
	private long start;
	private long stop;
	private String caption;

	public static Debug create(String caption) {
		return new Debug(caption);
	}

	private Debug(String caption) {
		this.caption = caption;
		this.start = System.nanoTime();
	}

	/**
	 * Logs the time in microseconds that elapsed since object creation. Does
	 * nothing if no output is set in {@link Preferences}.
	 */
	public void log() {
		PrintStream out = Preferences.getInstance().getOut();
		if (null == out) {
			return;
		}

		this.stop = System.nanoTime();
		double diff = (stop - start) / 1000;
		out.println(caption + " took: " + diff + " microsec");
	}
}
