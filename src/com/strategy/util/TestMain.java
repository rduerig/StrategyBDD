package com.strategy.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDPairing;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.strategy.api.board.Board;
import com.strategy.api.field.Field;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.Situation;
import com.strategy.havannah.board.BoardHavannah;
import com.strategy.havannah.logic.BoardAnalyzerHavannah;
import com.strategy.havannah.logic.situation.SituationHavannah;
import com.strategy.util.predicates.ValidPositionFilter;
import com.strategy.util.preferences.Preferences;

/**
 * @author Ralph Dürig
 */
public class TestMain {

	private static Joiner joiner = Joiner.on(", ");
	private static int SIZE = 2;

	public static void main(String[] args) throws FileNotFoundException {

		// profilingStrategyBdd();

		Board b = BoardHavannah.createInstance(
				PrimitiveBoardProvider.getBoard(SIZE), SIZE);
		System.out.println(b.toRowColString());
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(b);

		// // Situation sit = new SituationHavannah(analyzer, b,
		// StoneColor.WHITE);
		// // BDD path = sit.getWinningCondition();

		Position pos1 = b.getField(1).getPosition();
		Position pos2 = b.getField(7).getPosition();
		BDDFactory fac = BddFactoryProvider.getOrCreateBddFactory(b);
		// fac.setVarOrder(new int[]{3,7,8,5,1,0,2,6,4});
		fac.setVarOrder(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });
		System.out.println("computing path from " + pos1 + " to " + pos2);
		BDD path = analyzer.getPath(pos1, pos2, StoneColor.WHITE);
		// fac.reorder(BDDFactory.REORDER_SIFT);
		System.out.println(path.pathCount());

		Collection<Position> valid = Collections2.filter(b.getPositions(),
				new ValidPositionFilter(b));
		ArrayList<Position> filtered = Lists.newArrayList(valid);
		int[] varset = new int[filtered.size()];
		int count = 0;
		for (Position p : filtered) {
			varset[count++] = b.getField(p.getRow(), p.getCol()).getIndex();
		}
		BDD v = path.getFactory().makeSet(varset);

		System.out.println(path.satCount(v));

		// System.out.println(b.toIndexString());
		// // System.out.println(path);
		// System.out.println("nodes\t\t: " + path.nodeCount());
		// System.out.println("sat count\t: " + path.satCount());
		// System.out.println("path count\t: " + path.pathCount());

		// path.restrictWith(analyzer.getFactory().ithVar(4));
		// System.out.println("nodes\t\t: " + path.nodeCount());
		// System.out.println("sat count\t: " + path.satCount());
		// System.out.println("path count\t: " + path.pathCount());

		analyzer.done();
		// PrintStream out = new PrintStream("win"+SIZE+"-3.dot");
		// System.setOut(out);
		// path.printDot();

	}

	// ************************************************************************
	// profiling strategybdd

	private static void profilingStrategyBdd() throws FileNotFoundException {
		Preferences.getInstance().setGenerateFiles(true);
		Preferences.getInstance().setOut(
				new PrintStream("operations" + SIZE + ".log"));
		// System.setOut(new PrintStream("operations2.log"));
		int[][] rawBoard = PrimitiveBoardProvider.getBoard(SIZE);
		Board board = BoardHavannah.createInstance(rawBoard, SIZE);
		BoardAnalyzer analyzer = new BoardAnalyzerHavannah(board);
		Situation sit = new SituationHavannah(analyzer, board, StoneColor.WHITE);
		analyzer.done();
		// BDD win = sit.getWinningCondition();
		// System.out.println("win:" + win);
		// System.out.println("satCount: " + win.satCount());
		// System.out.println("pathCount: " + win.pathCount());
		// System.out.println("nodeCount: " + win.nodeCount());

		// human readable output of solutions (playouts)
		// printBoards(win, size);

		// latex output of solutions
		// printLatex(win, size);
	}

	private static void printBoards(BDD win, int size) {
		int[][] rawBoard = PrimitiveBoardProvider.getBoard(size);
		for (Object o : win.allsat()) {
			byte[] solution = (byte[]) o;
			// System.out.println(Arrays.toString(solution));
			Board cpBoard = BoardHavannah.createInstance(rawBoard, size);
			for (int i = 0; i < solution.length; i++) {
				if (solution[i] == 1) {
					Field field = cpBoard.getField(i);
					cpBoard.setField(FieldGenerator.create(1,
							field.getPosition(), field.getIndex()));
				}
			}
			System.out.println(cpBoard);
		}
	}

	private static void printLatex(BDD win, int size) {
		StringBuilder sb;
		for (Object o : win.allsat()) {
			byte[] solution = (byte[]) o;
			List<String> fields = Lists.newArrayList();
			for (int i = 0; i < solution.length; i++) {
				if (solution[i] == 1) {
					fields.add(RowConstant.parseToCoordString(i, size));
				}
			}
			sb = new StringBuilder();
			sb.append("\\begin{HavannahBoard}[board size=2]\n");
			sb.append("\\HStoneGroup[color=white]{");
			sb.append(joiner.join(fields));
			sb.append("}\n");
			sb.append("\\end{HavannahBoard}");
			System.out.println(sb.toString());
		}
	}

	// ************************************************************************
	// profiling sample bdds

	private static void profilingSampleBdds() throws FileNotFoundException {
		BDDFactory fac = BDDFactory.init(100, 100);
		fac.setVarNum(4);
		fac.reorderVerbose(-1);

		// BDD f = fac.ithVar(0).orWith(fac.ithVar(1));
		// printDot(f, "tmp/f.dot");
		// BDD g = fac.ithVar(2).orWith(fac.ithVar(3));
		// printDot(g, "tmp/g.dot");
		// BDD fAg = f.andWith(g);
		// printDot(fAg, "tmp/fAg.dot");
		// BDD fOg = f.orWith(g);
		// printDot(fOg, "tmp/fOg.dot");

		fac.setVarOrder(new int[] { 0, 1, 2, 3 });
		BDD bdd = fac.ithVar(0).orWith(fac.ithVar(1)).orWith(fac.ithVar(2))
				.orWith(fac.ithVar(3).orWith(fac.nithVar(3)));
		System.out.println("sat: " + bdd.satCount());
		System.out.println("before: " + bdd);
		fac.printAll();
		fac.printOrder();
		// printDot(bdd, "tmp/reorderBefore.dot");
		fac.setVarOrder(new int[] { 3, 1, 2, 0 });
		fac.printAll();
		fac.printOrder();
		System.out.println("after: " + bdd);
		// printDot(bdd, "tmp/reorderAfter.dot");
	}

	private static void printDot(BDD bdd, String filename)
			throws FileNotFoundException {
		PrintStream out = new PrintStream(filename);
		System.setOut(out);
		bdd.printDot();
	}

	// ************************************************************************
	// buddy example

	private static void example() {
		BDDFactory fact = BDDFactory.init(10, 10);
		fact.setVarNum(2);

		BDD True = fact.one();
		BDD False = fact.zero();

		// the expression x0
		BDD x_0 = fact.ithVar(0);

		// the expression not x1
		BDD nx_1 = fact.nithVar(1);

		// the expression (not x1 or x0) and (True or false)
		BDD b = nx_1.or(x_0).and(True.or(False));

		// Checks whether or not expression is unsat
		System.out.println("b is unsat? : " + b.isZero());

		// checks whether expression is tautology
		System.out.println("b is tautology? : " + b.isOne());

		// In order to restrict or quantify the expression to a given assignment
		// we give the assignment as a conjunction where positive variables
		// indicate that the variable should be restricted to false, and vice
		// versa.
		BDD restriction_x1_true_x0_false = fact.ithVar(1).and(fact.nithVar(0));

		BDD restricted = b.restrict(restriction_x1_true_x0_false);
		BDD existed = b.exist(x_0);

		// Exist. should be tautology
		System.out
				.println("Existiential quant. cause taut: " + existed.isOne());

		// Restriction shoule be unsat:
		System.out.println("Restriction caused unsat: " + restricted.isZero());

		// how to perform replacement
		BDDPairing replacement = fact.makePair();
		int[] from = { 1 };
		int[] to = { 0 };
		replacement.set(from, to);

		BDD b_replaced = existed.replace(replacement);
	}

}
