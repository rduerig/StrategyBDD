package com.strategy.util;

import java.text.NumberFormat;

public class FieldIndexFormatter {

	private final static String SPACE = "     ";

	private final NumberFormat formatter;

	public FieldIndexFormatter() {
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(0);
		formatter.setMaximumIntegerDigits(3);
		formatter.setMinimumFractionDigits(0);
		formatter.setMinimumIntegerDigits(3);
		formatter.setParseIntegerOnly(true);
	}

	public String format(int value) {
		return formatter.format(value);
	}

	public String space() {
		return SPACE;
	}

}
