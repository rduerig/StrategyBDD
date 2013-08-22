package com.strategy.api.interpreter;

import java.io.PrintStream;
import java.util.Scanner;

import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.evaluation.Evaluation;
import com.strategy.api.logic.prediction.Prediction;
import com.strategy.util.RowConstant;
import com.strategy.util.StoneColor;

public class GtpInterpreter extends Thread {

	private static final String REGEX_HGFINDEX = "[a-zA-Z]\\d+\\d*";
	private static final String REGEX_INDEX = "\\d+\\d*";

	private static PrintStream out = System.out;

	private Board board;
	private Prediction p;

	private StoneColor cpuColor;
	private String lastLine = null;
	private Scanner scanner;

	public GtpInterpreter() {
		super("GtpInterpreter");
		this.scanner = new Scanner(System.in);
	}

	public void run() {
		System.err.println(scanner.nextLine());
	}

	// ************************************************************************

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

	private void pringEvaluation(Evaluation eval) {
		out.println(board.toRatingString(eval.getRating(), eval.getBestIndex()));
	}

	// private void printUsage() {
	// out.println("Usage:");
	// out.println("\t " + CMD_REDO + " \t executes the previous command");
	// out.println("\t " + CMD_EXIT + " \t quits the program");
	// out.println("\t " + CMD_HELP + " \t prints this text");
	// out.println("\t " + CMD_WHITE
	// + " \t switches the player's color to white");
	// out.println("\t " + CMD_BLACK
	// + " \t switches the player's color to black");
	// out.println("\t " + CMD_THINK + " \t makes the cpu doing your turn");
	// out.println("\t " + CMD_SWAP
	// + " \t swaps the color of the last set stone");
	// out.println("\t " + CMD_SWITCH
	// + " \t switches the player's and the cpu's color");
	// out.println("\t " + CMD_NUMBERS
	// + " \t prints the board with each field's number");
	// out.println("\t " + CMD_COORDINATES
	// + " \t prints the board with each field's hgf-coordinate");
	// out.println("\t "
	// + CMD_RATING
	// +
	// " \t prints the board with each field's rating according to the computed evaluation");
	// out.println("\t :[NUMBER] \t sets a stone to the field specified by the given number");
	// out.println("\t :[CHARACTER][NUMBER] \t sets a stone to the field specified by the given hgf-coordinate");
	// out.println("\t [NUMBER] \t sets a stone to the field specified by the given number and let the cpu answer");
	// out.println("\t [CHARACTER][NUMBER] \t sets a stone to the field specified by the given hgf-coordinate and let the cpu answer");
	// }

}
