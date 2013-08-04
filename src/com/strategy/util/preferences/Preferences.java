package com.strategy.util.preferences;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.strategy.havannah.logic.PathCalculatorProvider.PathCalculatorKey;
import com.strategy.util.GameParser;
import com.strategy.util.GameParser.GameParserException;
import com.strategy.util.Turn;

/**
 * Stores the program's preferences that can be set through command line
 * parameters.
 * 
 * @author Ralph Dürig
 */
public class Preferences {

	private static Preferences instance;
	private static boolean defaultGenerateFiles = false;
	private static boolean defaultAvoidFiles = false;
	private static int defaultBoardSize = 4;
	private static List<Turn> defaultTurns = null;
	private static boolean defaultModeInterpreter = true; // defaults to
															// interpreter mode
	private static PrintStream defaultOut = null;
	private static final String STDOUT = "stdout";

	private static PathCalculatorKey defaultAlg = PathCalculatorKey.RECURSIVE;

	private boolean generateFiles;
	private boolean avoidFiles;
	private final int boardSize;
	private final List<Turn> turns;
	private boolean modeInterpreter;
	private PrintStream out;
	private PathCalculatorKey alg;
	private boolean help;

	public static Preferences createInstance(String[] args) {
		if (null == args || 0 == args.length) {
			instance = getDefault();
			return instance;
		}
		ArrayList<String> params = Lists.newArrayList(args);

		if (parseHelp(params)) {
			instance = getDefault();
			instance.setHelp(true);
			return instance;
		}

		boolean parGenerateFiles = parseGenerateFiles(params);
		boolean parAvoidFiles = parseAvoidFiles(params);
		boolean parModeInterpreter = parseMode(params);
		int parBoardSize = parseBoardSize(params);
		List<Turn> parTurns = defaultTurns;
		try {
			GameParser parser = getParser(params);
			if (null != parser) {
				parBoardSize = parser.getBoardSize();
				parTurns = parser.getTurns();
			} else {
				parser = getParserWithStrings(params);
				if (null != parser) {
					parBoardSize = parser.getBoardSize();
					parTurns = parser.getTurns();
				}
			}
		} catch (FileNotFoundException e) {
		} catch (GameParserException e) {
		}

		PrintStream out = parseOut(params);
		PathCalculatorKey alg = parseAlg(params);

		instance = new Preferences(
				null == parTurns || parTurns.isEmpty() ? parGenerateFiles
						: false, parAvoidFiles, parBoardSize, parTurns, parModeInterpreter,
				out, alg);

		return instance;

	}

	public static Preferences getInstance() {
		if (null == instance) {
			instance = getDefault();
		}
		return instance;
	}

	private Preferences(boolean generateFiles, boolean avoidFiles, int boardSize, List<Turn> turns,
			boolean modeInterpreter, PrintStream out, PathCalculatorKey alg) {
		this.generateFiles = generateFiles;
		this.avoidFiles = avoidFiles;
		this.boardSize = boardSize;
		this.turns = turns;
		this.modeInterpreter = modeInterpreter;
		this.out = out;
		this.alg = alg;
		this.help = false;
	}

	void setHelp(boolean help) {
		this.help = help;
	}

	public boolean isHelp() {
		return help;
	}

	public boolean isGenerateFiles() {
		return generateFiles;
	}

	public void setGenerateFiles(boolean generateFiles) {
		this.generateFiles = generateFiles;
	}

	public boolean isAvoidFiles() {
		return avoidFiles;
	}

	public boolean isModeInterpreter() {
		return modeInterpreter;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public List<Turn> getTurns() {
		return turns;
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public PathCalculatorKey getAlg() {
		return alg;
	}

	public void setAlg(PathCalculatorKey alg){
		this.alg = alg;
	}

	// ************************************************************************

	private static Preferences getDefault() {
		return new Preferences(defaultGenerateFiles, defaultAvoidFiles, defaultBoardSize,
				defaultTurns, defaultModeInterpreter, defaultOut, defaultAlg);
	}

	private static boolean parseHelp(List<String> params) {
		Optional<String> opt = Iterables.tryFind(params,
				new ParameterPredicate(ArgumentStrings.PAR_HELP));
		if (opt.isPresent()) {
			printHelp();
			return true;
		} else {
			return false;
		}
	}

	private static boolean parseGenerateFiles(List<String> params) {
		Optional<String> opt = Iterables.tryFind(params,
				new ParameterPredicate(ArgumentStrings.PAR_GENERATE_FILES));
		if (opt.isPresent()) {
			return true;
		} else {
			return defaultGenerateFiles;
		}
	}

	private static boolean parseAvoidFiles(List<String> params) {
		Optional<String> opt = Iterables.tryFind(params,
				new ParameterPredicate(ArgumentStrings.PAR_AVOID_FILES));
		if (opt.isPresent()) {
			return true;
		} else {
			return defaultAvoidFiles;
		}
	}

	private static boolean parseMode(List<String> params) {
		Optional<String> opt = Iterables.tryFind(params,
				new ParameterPredicate(ArgumentStrings.PAR_MODE));
		if (opt.isPresent()) {
			return false;
		} else {
			return defaultModeInterpreter;
		}
	}

	private static int parseBoardSize(List<String> params) {
		int parBoardSizeIndex = Iterables.indexOf(params,
				new ParameterPredicate(ArgumentStrings.PAR_BOARD_SIZE));
		if (parBoardSizeIndex < 0 || parBoardSizeIndex >= params.size() - 1) {
			return defaultBoardSize;
		}
		String value = Iterables.<String> get(params, parBoardSizeIndex + 1);
		return Integer.parseInt(value);
	}

	private static PrintStream parseOut(List<String> params) {
		int parOutIndex = Iterables.indexOf(params, new ParameterPredicate(
				ArgumentStrings.PAR_OUT));
		if (parOutIndex < 0 || parOutIndex >= params.size() - 1) {
			return defaultOut;
		}
		String value = Iterables.<String> get(params, parOutIndex + 1);
		if (STDOUT.equals(value)) {
			return System.out;
		}

		try {
			return new PrintStream(value);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file " + value
					+ " for output. No debug information will be available.");
			return defaultOut;
		}
	}

	private static PathCalculatorKey parseAlg(List<String> params) {
		int parAlgIndex = Iterables.indexOf(params, new ParameterPredicate(
				ArgumentStrings.PAR_ALG));
		if (parAlgIndex < 0 || parAlgIndex >= params.size() - 1) {
			return defaultAlg;
		}
		String value = Iterables.<String> get(params, parAlgIndex + 1);
		return PathCalculatorKey.parse(value);
	}

	private static GameParser getParser(List<String> params)
			throws FileNotFoundException, GameParserException {
		int parTurnsIndex = Iterables.indexOf(params, new ParameterPredicate(
				ArgumentStrings.PAR_TURNS));
		if (parTurnsIndex < 0 || parTurnsIndex >= params.size() - 1) {
			return null;
		}
		String value = Iterables.<String> get(params, parTurnsIndex + 1);
		File hgfFile = new File(value);
		if (null == hgfFile || !hgfFile.exists()) {
			return null;
		}

		InputStream in = new FileInputStream(hgfFile);

		GameParser parser = new GameParser(in);

		return parser;
	}

	private static GameParser getParserWithStrings(List<String> params)
			throws GameParserException {
		int parTurnsIndex = Iterables.indexOf(params, new ParameterPredicate(
				ArgumentStrings.PAR_TURNS_STRING));
		if (parTurnsIndex < 0 || parTurnsIndex >= params.size() - 1) {
			return null;
		}
		String value = Iterables.<String> get(params, parTurnsIndex + 1);
		if (null == value || value.trim().isEmpty()) {
			return null;
		}
		InputStream in = new ByteArrayInputStream(value.getBytes());

		GameParser parser = new GameParser(in);

		return parser;
	}

	private static void printHelp() {
		System.out.println("Available arguments:");
		System.out
				.println("-alg ['rec' / 'iter'] - select the algorithm to compute paths (transitive closure of connectivity)");
		System.out
				.println("-f - BDD files are always generated with this flag present, works not with -hgf");
		System.out.println("-h - prints this help message");
		System.out.println("-hgf [FILENAME] - parse FILENAME as hgf file");
		System.out
				.println("-m [HGF MOVES ...] - parses the given string as hgf move string");
		System.out
				.println("-out [FILENAME / 'stdout'] - prints debug output to given FILENAME or STDOUT");
		System.out.println("-s [NUMBER] - use NUMBER as boardsize");
	}

	// ************************************************************************

	private static class ParameterPredicate implements Predicate<String> {

		private String argString;

		public ParameterPredicate(String argString) {
			this.argString = argString;
		}

		@Override
		public boolean apply(String input) {
			return input.equals(argString);
		}

	}
}
