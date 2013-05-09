package com.strategy.util;

import java.text.AttributedCharacterIterator;
import java.text.NumberFormat;

public class EvaluationFormatter {

	private final static String SPACE = "        ";

	private final NumberFormat formatter;

	public EvaluationFormatter() {
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		formatter.setMaximumIntegerDigits(3);
		formatter.setMinimumFractionDigits(2);
		formatter.setMinimumIntegerDigits(3);
	}

	public String format(double value) {
		AttributedCharacterIterator charIt = formatter
				.formatToCharacterIterator(value);
		int end = charIt.getEndIndex();
		return formatter.format(value / Math.pow(10, end - 2));
	}

	public String space() {
		return SPACE;
	}

}
