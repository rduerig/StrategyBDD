package com.strategy.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Stores the program's preferences that can be set through command line
 * parameters.
 * 
 * @author Ralph Dürig
 */
public class Preferences {

	private static Preferences instance;
	private static boolean defaultGenerateFiles = false;
	private static int defaultBoardSize = 4;

	private final boolean generateFiles;
	private final int boardSize;

	public static Preferences createInstance(String[] args) {
		if (null == args || 0 == args.length) {
			instance = getDefault();
			return instance;
		}
		ArrayList<String> params = Lists.newArrayList(args);
		boolean parGenerateFiles = parseGenerateFiles(params);
		int parBoardSize = parseBoardSize(params);
		return new Preferences(parGenerateFiles, parBoardSize);

	}

	public static Preferences getInstance() {
		if (null == instance) {
			instance = getDefault();
		}
		return instance;
	}

	private Preferences(boolean generateFiles, int boardSize) {
		this.generateFiles = generateFiles;
		this.boardSize = boardSize;
	}

	/**
	 * @return the generateFiles
	 */
	public boolean isGenerateFiles() {
		return generateFiles;
	}

	/**
	 * @return the boardSize
	 */
	public int getBoardSize() {
		return boardSize;
	}

	// ************************************************************************

	private static Preferences getDefault() {
		return new Preferences(defaultGenerateFiles, defaultBoardSize);
	}

	private static boolean parseGenerateFiles(List<String> params) {
		int parGenerateFilesIndex = Iterables.indexOf(params,
				new ParameterGenerateFilesPredicate());
		if (parGenerateFilesIndex < 0
				|| parGenerateFilesIndex >= params.size() - 1) {
			return defaultGenerateFiles;
		}
		String value = Iterables
				.<String> get(params, parGenerateFilesIndex + 1);
		return Boolean.parseBoolean(value);
	}

	private static int parseBoardSize(List<String> params) {
		int parBoardSizeIndex = Iterables.indexOf(params,
				new ParameterBoardSizePredicate());
		if (parBoardSizeIndex < 0 || parBoardSizeIndex >= params.size() - 1) {
			return defaultBoardSize;
		}
		String value = Iterables.<String> get(params, parBoardSizeIndex + 1);
		return Integer.parseInt(value);
	}

	// ************************************************************************

	private static class ParameterGenerateFilesPredicate implements
			Predicate<String> {

		private static final String PAR_GENERATE_FILES = "-f";

		@Override
		public boolean apply(String input) {
			return input.equals(PAR_GENERATE_FILES);
		}

	}

	private static class ParameterBoardSizePredicate implements
			Predicate<String> {

		private static final String PAR_BOARD_SIZE = "-s";

		@Override
		public boolean apply(String input) {
			return input.equals(PAR_BOARD_SIZE);
		}

	}
}
