package com.strategy.util.formatter;

public class EvaluationFormatter {

	private final static String SPACE = "      ";

	private int bestIndex;

	public EvaluationFormatter(int bestIndex) {
		this.bestIndex = bestIndex;
	}

	public String format(int index, double value) {
		// if (index == bestIndex) {
		// return "[" + String.format("%e", value) + "]";
		// }
		// return " " + String.format("%e", value) + " ";
		if (index == bestIndex) {
			return "[" + value + "]";
		}
		return " " + value + " ";
	}

	public String space() {
		return SPACE;
	}

}
