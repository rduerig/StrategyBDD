package com.strategy.api.interpreter;

import static com.strategy.api.interpreter.InterpreterCommands.CMD_BDD;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_BLACK;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_COORDINATES;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_EXIT;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_HELP;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_MEM;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_NODES;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_NUMBERS;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_PREDICT;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_PREFIX;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_RATING;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_REDO;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_SOLUTIONS;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_SWAP;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_SWITCH;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_THINK;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_VALUE;
import static com.strategy.api.interpreter.InterpreterCommands.CMD_WHITE;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.javabdd.BDD;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.logic.prediction.PredictionHavannah;
import com.strategy.util.Debug;
import com.strategy.util.FieldGenerator;
import com.strategy.util.PredictedMove;
import com.strategy.util.RowConstant;
import com.strategy.util.StoneColor;
import com.strategy.util.Turn;
import com.strategy.util.operation.Logging;
import com.strategy.util.predicates.EmptyPositionFilter;
import com.strategy.util.predicates.ValidPositionFilter;

/**
 * @author Ralph Dürig
 */
public class StrategyInterpreter extends Thread {

	private static final String REGEX_HGFINDEX = "[a-zA-Z]\\d+\\d*";
	private static final String REGEX_INDEX = "\\d+\\d*";

	private static PrintStream out = System.out;

	private final Board board;
	private Prediction p;

	private StoneColor cpuColor;
	private String lastLine = null;
	private Scanner scanner;
	private Pattern isManualTurnHgf = Pattern.compile(CMD_PREFIX
			+ REGEX_HGFINDEX);
	private Pattern isManualTurnIndex = Pattern.compile(CMD_PREFIX
			+ REGEX_INDEX);
	private Pattern isTurnHgf = Pattern.compile(REGEX_HGFINDEX);
	private Pattern isTurnIndex = Pattern.compile(REGEX_INDEX);

	public StrategyInterpreter(Board board, StoneColor cpuColor, Prediction p) {
		super("Interpreter");
		this.board = board;
		this.cpuColor = cpuColor;
		this.p = p;
		this.scanner = new Scanner(System.in);
	}

	public void run() {
		out.println("You are playing " + cpuColor.getOpposite());
		String line = null;

		out.println(board.toMarkLastTurnString(p.getLastTurn()));

		if (p.isWinWhite()) {
			win(StoneColor.WHITE);
			return;
		}
		if (p.isWinBlack()) {
			win(StoneColor.BLACK);
			return;
		}

		out.print(cpuColor.getOpposite() + "s turn: ");
		line = scanner.nextLine();

		line = checkCmdRedo(line);

		if (null == line || line.trim().isEmpty()) {
			return;
		}

		if (line.startsWith(CMD_PREFIX)) {

			if (CMD_EXIT.equals(line)) {
				cmdExit();
				return;
			}

			if (CMD_MEM.equals(line)) {
				printMem();
				return;
			}

			if (InterpreterCommands.CMD_SWITCH.equals(line)) {
				cmdSwitch();
				return;
			}

			if (InterpreterCommands.CMD_SWAP.equals(line)) {
				Integer lastTurn = p.getLastTurn();
				if (null == lastTurn) {
					return;
				}
				cmdSwap(lastTurn);
				if (p.isWinWhite()) {
					win(StoneColor.WHITE);
					return;
				}
				if (p.isWinBlack()) {
					win(StoneColor.BLACK);
					return;
				}
				return;
			}

			if (line.equalsIgnoreCase(CMD_BLACK)
					|| line.equalsIgnoreCase(CMD_WHITE)) {
				cmdChangeColor(line);
				return;
			}

			if (line.equals(CMD_THINK)) {
				Integer next = p.doCalculatedTurn(cpuColor.getOpposite());
				if (null != next) {
					printCpuTurn(next, board.getBoardSize(),
							cpuColor.getOpposite());
					cmdSwitch();
					return;
				} else {
					if (p.isWinWhite()) {
						win(StoneColor.WHITE);
						return;
					}
					if (p.isWinBlack()) {
						win(StoneColor.BLACK);
						return;
					}
				}
			}

			if (CMD_HELP.equals(line)) {
				printUsage();
				return;
			}

			if (CMD_NUMBERS.equals(line)) {
				out.println(board.toIndexString());
				return;
			}

			if (CMD_COORDINATES.equals(line)) {
				out.println(board.toRowConstantString());
				return;
			}

			if (CMD_RATING.equals(line)) {
				// out.println("Rating WHITE:");
				// printEvaluation(p.getEvaluationWhite());
				// out.println("Rating BLACK:");
				// printEvaluation(p.getEvaluationBlack());
				out.println("Rating: ");
				printEvaluation(p.getEvaluation(cpuColor.getOpposite()));
				return;
			}

			if (CMD_NODES.equals(line)) {
				printNodes(p.getWhite());
				printNodes(p.getBlack());
				return;
			}

			if (CMD_BDD.equals(line)) {
				printBdd(p.getWhite());
				printBdd(p.getBlack());
				return;
			}

			if (CMD_VALUE.equals(line)) {
				printValue(p.getWhite(), p.getBlack());
				// printValue(p.getBlack());
				return;
			}

			if (CMD_SOLUTIONS.equals(line)) {
				printSolutions(p.getWhite());
				printSolutions(p.getBlack());
				return;
			}

			if (CMD_PREDICT.equals(line)) {
				printPrediction(p.getPrediction(cpuColor.getOpposite()));
				return;
			}

			Integer fieldIndex = null;
			Matcher turnHgfMatcher = isManualTurnHgf.matcher(line);
			Matcher turnIndexMatcher = isManualTurnIndex.matcher(line);
			if (turnHgfMatcher.matches() || turnIndexMatcher.matches()) {
				if (turnHgfMatcher.matches()) {
					fieldIndex = readHgfIndex(line.substring(1), board);
				}
				if (turnIndexMatcher.matches()) {
					fieldIndex = readIndex(line.substring(1), board);
				}

				if (null == fieldIndex || !board.isEmptyField(fieldIndex)
						|| !board.isValidField(fieldIndex)) {
					printFieldError();
					return;
				}

				p.doManualTurn(fieldIndex, cpuColor.getOpposite());
				// cmdSwitch();
				if (p.isWinWhite()) {
					win(StoneColor.WHITE);
					return;
				}
				if (p.isWinBlack()) {
					win(StoneColor.BLACK);
					return;
				}
				return;
			} else {
				printInputError();
			}

		} else {

			Integer fieldIndex = null;
			Matcher turnHgfMatcher = isTurnHgf.matcher(line);
			Matcher turnIndexMatcher = isTurnIndex.matcher(line);
			if (turnHgfMatcher.matches() || turnIndexMatcher.matches()) {
				if (turnHgfMatcher.matches()) {
					fieldIndex = readHgfIndex(line, board);
				}
				if (turnIndexMatcher.matches()) {
					fieldIndex = readIndex(line, board);
				}

				if (null == fieldIndex || !board.isEmptyField(fieldIndex)
						|| !board.isValidField(fieldIndex)) {
					printFieldError();
					return;
				}

				Integer next;
				Debug answerlog = Debug.create("answering turn");
				next = p.answerTurn(fieldIndex, cpuColor.getOpposite());
				answerlog.log();

				if (null != next) {
					printCpuTurn(next, board.getBoardSize(), cpuColor);
				} else {
					if (p.isWinWhite()) {
						win(StoneColor.WHITE);
						return;
					}
					if (p.isWinBlack()) {
						win(StoneColor.BLACK);
						return;
					}
				}
				return;
			} else {
				printInputError();
			}
		}
	}

	// ************************************************************************

	private void cmdChangeColor(String line) {
		StoneColor parsed = StoneColor.parse(line.substring(1));
		if (!StoneColor.EMPTY.equals(parsed)) {
			cpuColor = parsed.getOpposite();
		}

		out.println("You are now " + cpuColor.getOpposite());
	}

	private void cmdSwap(Integer lastTurn) {
		Field lastField = board.getField(lastTurn);
		// overwrite the last field
		board.setField(FieldGenerator.create(cpuColor.getOpposite()
				.getPrimitive(), lastField.getPosition(), lastField.getIndex()));
		// overwrite the last turn
		List<Turn> turns = p.getTurnsSoFar();
		if (null != turns && turns.size() > 0) {
			turns.set(
					turns.size() - 1,
					new Turn(RowConstant.parse(lastTurn, board.getBoardSize()),
							RowConstant.parseToCoordNumber(lastTurn,
									board.getBoardSize()), cpuColor
									.getOpposite()));
		}
		p = new PredictionHavannah(board, lastTurn, turns);
		cmdSwitch();
	}

	private void cmdSwitch() {
		cpuColor = cpuColor.getOpposite();
	}

	private void cmdExit() {
		exit();
	}

	private String checkCmdRedo(String line) {
		if (CMD_REDO.equals(line)) {
			return lastLine;
		} else {
			lastLine = line;
			return line;
		}
	}

	private Integer readIndex(String line, Board board) {
		int limit = board.getRows() * board.getColumns();
		Integer fieldIndex;
		try {
			fieldIndex = Integer.parseInt(line);
			if (fieldIndex < 0 || fieldIndex >= limit) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			return null;
		}

		return fieldIndex;
	}

	private Integer readHgfIndex(String line, Board board) {
		Integer fieldIndex = null;
		RowConstant coord = RowConstant.parseToConstant(line.substring(0, 1));
		if (null == coord) {
			return null;
		}
		Integer coordNumber;
		try {
			coordNumber = Integer.parseInt(line.substring(1));
			if (coordNumber < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			return null;
		}
		Field field = board.getField(coord, coordNumber);
		if (null != field) {
			fieldIndex = field.getIndex();
		}

		return fieldIndex;
	}

	private void win(StoneColor color) {
		out.println(color + " wins!");
		exit();
	}

	private void exit() {
		out.println("Bye");
		out.println(board.toMarkLastTurnString(p.getLastTurn()));
		interrupt();
		scanner.close();
		InterpreterManager.exit();
	}

	private void printCpuTurn(int turnIndex, int boardSize, StoneColor cpuColor) {
		RowConstant coord = RowConstant.parse(turnIndex, boardSize);
		Integer coordNumber = RowConstant.parseToCoordNumber(turnIndex,
				boardSize);
		out.println(cpuColor + "s turn: " + turnIndex + " - " + coord
				+ coordNumber);
	}

	private void printInputError() {
		out.println("Invalid input. Use ':help' to get a list of valid commands.");
	}

	private void printFieldError() {
		out.println("The selected field is either not valid or not empty. Please choose another one.");
	}

	private void printEvaluation(Evaluation eval) {
		out.println(board.toRatingString(eval.getRating(), eval.getBestIndex()));
		String coord = RowConstant.parseToCoordString(eval.getBestIndex(),
				board.getBoardSize());
		out.println("Best index: " + eval.getBestIndex() + " - " + coord);
		eval.log();
	}

	private void printNodes(Situation sit) {
		Logging l = Logging.create("nodes computing");
		double value = l.nodeCountLog(sit.getWinningCondition());
		out.println("Nodes " + sit.getStoneColor().name() + ": " + value);
		out.println("Factory active nodes: "
				+ sit.getWinningCondition().getFactory().getNodeNum());
		out.println("Factory node table size: "
				+ sit.getWinningCondition().getFactory().getNodeTableSize());
		l.log();
	}

	private void printBdd(Situation sit) {
		out.println("BDD " + sit.getStoneColor().name() + ": ");
		sit.getWinningCondition().printDot();
	}

	private void printValue(Situation sit, Situation sitBlack) {
		// Logging l = Logging.create("sat count computing");
		Collection<Position> valid = Collections2.filter(board.getPositions(),
				new ValidPositionFilter(board));
		Collection<Position> validEmpty = Collections2.filter(valid,
				new EmptyPositionFilter(board));
		ArrayList<Position> filtered = Lists.newArrayList(valid);

		int[] varset = new int[filtered.size()];
		int count = 0;
		for (Position p : filtered) {
			varset[count++] = board.getField(p.getRow(), p.getCol()).getIndex();
		}
		BDD v = sit.getWinningCondition().getFactory().makeSet(varset);

		double total = Math.pow(2, filtered.size());
		double satCountWhite = sit.getWinningCondition().satCount(v);
		double satCountBlack = sitBlack.getWinningCondition().satCount(v);
		double value = (satCountWhite - satCountBlack)
				/ Math.pow(2, filtered.size());
		out.println("var num: "
				+ sit.getWinningCondition().getFactory().varNum());
		out.println("pos num: " + board.getPositions().size());
		out.println("Sat Count " + sit.getStoneColor().name() + ": "
				+ String.format("%e", satCountWhite) + " satisfying out of "
				+ String.format("%e", total));
		out.println("Sat Count " + sitBlack.getStoneColor().name() + ": "
				+ String.format("%e", satCountBlack) + " satisfying out of "
				+ String.format("%e", total));
		out.println("Value: " + value);
	}

	private void printPrediction(List<PredictedMove> moves) {
		out.println("predicted moves:");
		for (PredictedMove m : moves) {
			out.println(m);
		}
	}

	private void printSolutions(Situation sit) {
		Logging l = Logging.create("solutions computing");
		double value = l.pathCountLog(sit.getWinningCondition());
		out.println("Solutions " + sit.getStoneColor().name() + ": "
				+ String.format("%e", value));
		l.log();
	}

	private void printMem() {
		int mb = 1024 * 1024;
		Runtime runtime = Runtime.getRuntime();

		// Print used memory
		out.println("Used Memory:"
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb + " MB");

		// Print free memory
		out.println("Free Memory:" + runtime.freeMemory() / mb + " MB");

		// Print total available memory
		out.println("Total Memory:" + runtime.totalMemory() / mb + " MB");

		// Print Maximum available memory
		out.println("Max Memory:" + runtime.maxMemory() / mb + " MB");
	}

	private void printUsage() {
		out.println("Usage:");
		out.println("\t " + CMD_REDO + " \t\t executes the previous command");
		out.println("\t " + CMD_EXIT + " \t\t quits the program");
		out.println("\t " + CMD_HELP + " \t\t prints this text");
		out.println("\t " + CMD_MEM + " \t\t prints memory information");
		out.println("\t " + CMD_WHITE
				+ " \t switches the player's color to white");
		out.println("\t " + CMD_BLACK
				+ " \t switches the player's color to black");
		out.println("\t " + CMD_THINK + " \t makes the cpu doing your turn");
		out.println("\t " + CMD_SWAP
				+ " \t\t swaps the color of the last set stone");
		out.println("\t " + CMD_SWITCH
				+ " \t switches the player's and the cpu's color");
		out.println("\t " + CMD_NUMBERS
				+ " \t prints the board with each field's number");
		out.println("\t " + CMD_COORDINATES
				+ " \t prints the board with each field's hgf-coordinate");
		out.println("\t "
				+ CMD_RATING
				+ " \t prints the board with each field's rating according to the computed evaluation");
		out.println("\t " + CMD_NODES
				+ " \t prints information about the nodes the BDDs are using");
		out.println("\t " + CMD_BDD
				+ " \t\t prints the currently used BDDs in dot graph notation");
		out.println("\t "
				+ CMD_VALUE
				+ " \t prints the value (= number of satisfying variable assignments) for the current situation");
		out.println("\t "
				+ CMD_SOLUTIONS
				+ " \t prints the solutions (= number of paths to the BDD's true terminal) for the current situation");
		out.println("\t "
				+ CMD_PREDICT
				+ " \t prints the next 5 best moves that are predicted for the current situation");
		out.println("\t :[NUMBER] \t sets a stone to the field specified by the given number");
		out.println("\t :[CHARACTER][NUMBER] \t sets a stone to the field specified by the given hgf-coordinate");
		out.println("\t [NUMBER] \t sets a stone to the field specified by the given number and let the cpu answer");
		out.println("\t [CHARACTER][NUMBER] \t sets a stone to the field specified by the given hgf-coordinate and let the cpu answer");
	}

}
