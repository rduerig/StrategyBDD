package com.strategy.api.interpreter;

import com.strategy.util.StoneColor;

public class InterpreterCommands {

	static final String CMD_PREFIX = ":";
	static final String CMD_REDO = ".";
	static final String CMD_THINK = CMD_PREFIX + "think";
	static final String CMD_WHITE = CMD_PREFIX + StoneColor.WHITE.name();
	static final String CMD_BLACK = CMD_PREFIX + StoneColor.BLACK.name();
	static final String CMD_EXIT = CMD_PREFIX + "exit";
	static final String CMD_HELP = CMD_PREFIX + "help";
	static final String CMD_NUMBERS = CMD_PREFIX + "numbers";
	static final String CMD_COORDINATES = CMD_PREFIX + "coordinates";
	static final String CMD_RATING = CMD_PREFIX + "rating";
	static final String CMD_SWAP = CMD_PREFIX + "swap";
	static final String CMD_SWITCH = CMD_PREFIX + "switch";
	static final String CMD_NODES = CMD_PREFIX + "nodes";

}
