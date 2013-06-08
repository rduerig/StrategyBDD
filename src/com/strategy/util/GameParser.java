package com.strategy.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

/**
 * @author Ralph Dürig
 */
public class GameParser {

	private static final String PREFIX_SIZE = "SZ";
	private static final String PREFIX_B = "B";
	private static final String PREFIX_W = "W";
	private static final String VALUE_SWAP = "swap";
	private static final String VALUE_RESIGN = "resign";

	private static final String NONE_OF = ";()";
	private static final String SPLIT_CONTENT_ON = "]";
	private static final String SPLIT_ATTRIBUTE_ON = "[";

	private Integer size;
	private List<Turn> fields;

	/**
	 * @param in
	 * @throws IOException
	 */
	public GameParser(InputStream in) throws GameParserException {
		this.size = null;
		this.fields = Lists.newArrayList();
		init(in);
	}

	/**
	 * @return
	 */
	public Integer getBoardSize() {
		return size;
	}

	/**
	 * @return
	 */
	public List<Turn> getTurns() {
		return fields;
	}

	// ************************************************************************

	private void init(InputStream in) throws GameParserException {
		BufferedInputStream buf = new BufferedInputStream(in);
		InputStreamReader reader = new InputStreamReader(buf, Charsets.UTF_8);
		String raw;
		try {
			raw = CharStreams.toString(reader);
			parseRaw(raw);
			reader.close();
		} catch (IOException e) {
			throw new GameParserException(e.getMessage(), e);
		}
	}

	private void parseRaw(String raw) throws GameParserException {
		String cleanedRaw = CharMatcher.noneOf(NONE_OF).retainFrom(raw);
		Iterable<String> split = Splitter.on(SPLIT_CONTENT_ON)
				.omitEmptyStrings().trimResults().split(cleanedRaw);
		for (String s : split) {
			if (s.startsWith(PREFIX_SIZE)) {
				parseBoardSize(s);
			}
			if (s.startsWith(PREFIX_W)) {
				parseField(s, StoneColor.BLACK);
			}
			if (s.startsWith(PREFIX_B)) {
				parseField(s, StoneColor.WHITE);
			}
		}
	}

	private void parseBoardSize(String s) throws GameParserException {
		try {
			String valueStr = s.substring(s.indexOf(SPLIT_ATTRIBUTE_ON) + 1);
			int value = Integer.parseInt(valueStr);
			size = value;

		} catch (IndexOutOfBoundsException e) {
			throw new GameParserException("Could not read value for attribute "
					+ s, e);
		} catch (NumberFormatException e) {
			throw new GameParserException(
					"Could not parse value for attribute " + s, e);
		}
	}

	private void parseField(String s, StoneColor color)
			throws GameParserException {
		// TODO resign
		if (!s.contains(VALUE_SWAP) && !s.contains(VALUE_RESIGN)) {
			String valueStr;
			try {
				valueStr = s.substring(s.indexOf(SPLIT_ATTRIBUTE_ON) + 1);
			} catch (IndexOutOfBoundsException e) {
				throw new GameParserException(
						"Could not read value for attribute " + s, e);
			}

			if (valueStr.length() < 2) {
				throw new GameParserException("Value for attribute (" + s
						+ ") not correct.");
			}

			String coordLetterStr = valueStr.substring(0, 1);
			String coordNumberStr = valueStr.substring(1);
			int coordNumber = Integer.parseInt(coordNumberStr);

			RowConstant coord = RowConstant.parseToConstant(coordLetterStr);

			Turn turn = new Turn(coord, coordNumber, color);
			fields.add(turn);
		}

		if (s.contains(VALUE_SWAP)) {
			if (fields.isEmpty() || fields.size() > 1) {
				// no swap allowed
				return;
			}
			Turn last = Iterables.getLast(fields);
			StoneColor newColor;
			if (last.getColor().equals(StoneColor.WHITE)) {
				newColor = StoneColor.BLACK;
			} else {
				newColor = StoneColor.WHITE;
			}

			fields.set(0, new Turn(last.getCoord(), last.getCoordNumber(),
					newColor));
		}

	}

	// ************************************************************************

	public class GameParserException extends Exception {
		public GameParserException() {
			super();
		}

		public GameParserException(String msg) {
			super(msg);
		}

		public GameParserException(String msg, Throwable t) {
			super(msg, t);
		}
	}

}
