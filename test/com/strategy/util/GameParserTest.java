package com.strategy.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.strategy.util.GameParser.GameParserException;

/**
 * @author Ralph Dürig
 */
public class GameParserTest {

	@Test
	public void test() {
		// Preferences.createInstance(new String[] { "-f" });
		String hgf = "(;FF[4]EV[havannah.in.size4.4]PB[mickgraham]PW[kiwibill]SZ[4]RU[LG]RE[B]GC[ game #1541446]SO[http://www.littlegolem.com];"
				+ "W[E1];B[swap];W[F2];B[D3];W[G1];B[E4];W[G4];B[F3];W[E5];B[D6];W[D5];B[C2];W[G3];B[G2];W[E6];B[E3];W[D7];B[resign])";
		InputStream in = new ByteArrayInputStream(hgf.getBytes());
		GameParser parser;
		try {
			parser = new GameParser(in);
		} catch (GameParserException e) {
			Assert.fail("Got an Exception: " + e.getMessage());
			return;
		}

		// Board b = BoardHavannah.createInstance(
		// TestBoardProvider.getBoard(parser.getBoardSize()),
		// parser.getBoardSize(), parser.getFields());
		// System.out.println(b);

		Integer expectedSize = 4;
		Assert.assertEquals(expectedSize, parser.getBoardSize());

		int expectedTurns = 16;
		Assert.assertEquals(expectedTurns, parser.getTurns().size());
	}

}
