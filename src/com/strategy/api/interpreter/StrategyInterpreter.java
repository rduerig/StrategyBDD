package com.strategy.api.interpreter;

import java.io.PrintStream;
import java.util.Scanner;

import com.google.common.base.CharMatcher;
import com.strategy.api.board.Board;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.util.RowConstant;
import com.strategy.util.StoneColor;

/**
 * @author Ralph Dürig
 */
public class StrategyInterpreter extends Thread {

	private static final String CMD_PREFIX = ":";
	private static final String CMD_REDO = ".";
	private static final String CMD_THINK = CMD_PREFIX + "think";
	private static final String CMD_WHITE = CMD_PREFIX
			+ StoneColor.WHITE.name();
	private static final String CMD_BLACK = CMD_PREFIX
			+ StoneColor.BLACK.name();
	private static final String CMD_EXIT = CMD_PREFIX + "exit";
	private static final String CMD_HELP = CMD_PREFIX + "help";
	private static final String CMD_NUMBERS = CMD_PREFIX + "numbers";
	private static final String CMD_COORDINATES = CMD_PREFIX + "coordinates";

	private static CharMatcher letterMatcher = CharMatcher.inRange('a', 'z')
			.or(CharMatcher.inRange('A', 'Z'));
	private static PrintStream out = System.out;

	private final Board board;
	private final Prediction p;

	private StoneColor cpuColor;
	private String lastLine = null;
	private Scanner scanner;

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

		out.println(board);

		if (p.isWinWhite()) {
			out.println("WHITE wins!");
			exit();
			return;
		}
		if (p.isWinBlack()) {
			out.println("BLACK wins!");
			exit();
			return;
		}

		out.print(cpuColor.getOpposite() + "s turn: ");
		line = scanner.nextLine();

		if (CMD_REDO.equals(line)) {
			line = lastLine;
		} else {
			lastLine = line;
		}

		if (null == line || line.trim().isEmpty()) {
			return;
		}

		if (line.startsWith(CMD_PREFIX)) {

			if (CMD_EXIT.equals(line)) {
				exit();
				return;
			}

			if (line.equalsIgnoreCase(CMD_BLACK)
					|| line.equalsIgnoreCase(CMD_WHITE)) {
				StoneColor parsed = StoneColor.parse(line.substring(1));
				if (!StoneColor.EMPTY.equals(parsed)) {
					cpuColor = parsed.getOpposite();
				}

				out.println("You are now " + cpuColor.getOpposite());
				return;
			}

			if (line.equals(CMD_THINK)) {
				int next = p.doTurn(cpuColor.getOpposite());
				printCpuTurn(next, board.getBoardSize(), cpuColor.getOpposite());
				cpuColor = cpuColor.getOpposite();
				return;
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

		} else {

			Integer fieldIndex = readIndex(line, board);
			if (null == fieldIndex) {
				printInputError();
				return;
			}

			if (!board.isEmptyField(fieldIndex)) {
				printFieldError();
				return;
			}

			int next = p.answerTurn(fieldIndex, cpuColor.getOpposite());
			printCpuTurn(next, board.getBoardSize(), cpuColor);
			return;
		}
	}

	// ************************************************************************

	private Integer readIndex(String line, Board board) {
		int limit = board.getRows() * board.getColumns();
		Integer fieldIndex;
		if (letterMatcher.matches(line.charAt(0))) {
			RowConstant coord = RowConstant.parseToConstant(line
					.substring(0, 1));
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
			fieldIndex = board.getField(coord, coordNumber).getIndex();
		} else {
			try {
				fieldIndex = Integer.parseInt(line);
				if (fieldIndex < 0 || fieldIndex >= limit) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}

		return fieldIndex;

	}

	private void exit() {
		out.println("Bye");
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
		out.println("Input must be either a possible field number on the board starting with '0' or the word 'exit'");
	}

	private void printFieldError() {
		out.println("The selected field is not emtpy. Please choose another one.");
	}

	private void printUsage() {
		out.println("Usage:");
		out.println("\t . \t executes the previous command");
		out.println("\t :exit \t quits the program");
		out.println("\t :help \t prints this text");
		out.println("\t :white \t switches the player's color to white");
		out.println("\t :black \t switches the player's color to black");
		out.println("\t :think \t makes the cpu doing your turn");
		out.println("\t :numbers \t prints the board with each field's number");
		out.println("\t :coordinates \t prints the board with each field's hgf-coordinate");
		out.println("\t [NUMBER] \t sets a stone to the field specified by the given number");
		out.println("\t [CHARACTER][NUMBER] \t sets a stone to the field specified by the given hgf-coordinate");
	}

}
