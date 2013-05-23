package com.strategy.util;

import java.text.NumberFormat;

public class EvaluationFormatter {

	private final static String SPACE = "    ";

	private final NumberFormat formatter;

	private int bestIndex;

	public EvaluationFormatter(int bestIndex) {
		this.bestIndex = bestIndex;
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(3);
		formatter.setMinimumFractionDigits(3);
		formatter.setMaximumIntegerDigits(3);
		formatter.setMinimumIntegerDigits(3);
		formatter.setGroupingUsed(false);
	}

	public String format(int index, double value) {
		int exp = Double.valueOf(Math.log10(value)).intValue();
		int power = exp > 1 ? exp - 1 : 0;
		double toFormat = value / Math.pow(10, power);
		if (index == bestIndex) {
			return "[" + formatter.format(toFormat) + "]";
		}
		return " " + formatter.format(toFormat) + " ";
	}

	public String space() {
		return SPACE;
	}

}
