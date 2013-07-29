package com.strategy.havannah.logic.situation;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.HasDebugFlag;
import com.strategy.api.board.Board;
import com.strategy.api.logic.BoardAnalyzer;
import com.strategy.api.logic.Position;
import com.strategy.api.logic.situation.ConditionCalculator;
import com.strategy.havannah.logic.PositionHexagon;
import com.strategy.util.Output;
import com.strategy.util.StoneColor;
import com.strategy.util.operation.Logging;

public class BridgeConditionCalculator implements ConditionCalculator,
		HasDebugFlag {

	private BDD result;
	private StoneColor color;

	public BridgeConditionCalculator(BoardAnalyzer analyzer, Board board,
			StoneColor color) {
		this.color = color;
		calculateResult(analyzer, board);
	}

	@Override
	public BDD getBdd() {
		return result;
	}

	// ************************************************************************

	private void calculateResult(BoardAnalyzer analyzer, Board board) {
		/**
		 * corners in havannah board are as follows (let b = board size):<br>
		 * - i=0, j=0<br>
		 * - i=0, j=b-1<br>
		 * - i=b-1, j=0<br>
		 * - i=b-1, j=2b-2<br>
		 * - i=2b-2, j=b-1<br>
		 * - i=2b-2, j=2b-2
		 */

		int b = board.getBoardSize();
		Position[] corners = new PositionHexagon[6];
		corners[0] = PositionHexagon.get(0, 0);
		corners[1] = PositionHexagon.get(0, b - 1);
		corners[2] = PositionHexagon.get(b - 1, 0);
		corners[3] = PositionHexagon.get(b - 1, 2 * b - 2);
		corners[4] = PositionHexagon.get(2 * b - 2, b - 1);
		corners[5] = PositionHexagon.get(2 * b - 2, 2 * b - 2);
		/**
		 * corners for a 5x5 board:<br>
		 * |1|| ||2| <br>
		 * | || || || | <br>
		 * |3|| || || ||4|<br>
		 * ___| || || || |<br>
		 * ______|5|| ||6|<br>
		 */

		Logging logBridgePath = Logging.create("or bridge path");
		result = analyzer.getFactory().zero();
		for (int i = 0; i < corners.length; i++) {
			for (int j = i + 1; j < corners.length; j++) {
				Output.print("path from " + corners[i] + " to " + corners[j],
						BridgeConditionCalculator.class);
				BDD path = analyzer.getPath(corners[i], corners[j], color);
				// result = result.orWith(path);
				result = logBridgePath.orLog(result, path);
			}
		}

		logBridgePath.log();
		analyzer.getFactory().reorder(BDDFactory.REORDER_SIFT);
	}

}
