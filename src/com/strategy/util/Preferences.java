package com.strategy.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
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
		instance = new Preferences(parGenerateFiles, parBoardSize);
		return instance;

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
		Optional<String> opt = Iterables.tryFind(params,
				new ParameterGenerateFilesPredicate());
		if (opt.isPresent()) {
			return true;
		} else {
			return defaultGenerateFiles;
		}
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
