package com.strategy.havannah.logic.situation;

import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

/**
 * Describes the six corners of a hexagon clockwise beginning with the bottom
 * (namely C1).
 * 
 * @author Ralph Dürig
 */
public enum CornerFieldCategory {
	/**
	 * The fields of this corner have no valid neighbors in south, southwest and
	 * southeast direction on the board.
	 */
	C1,

	/**
	 * The fields of this corner have no valid neighbors in south, southwest and
	 * northwest direction on the board.
	 */
	C2,

	/**
	 * The fields of this corner have no valid neighbors in southwest, northwest
	 * and north direction on the board.
	 */
	C3,

	/**
	 * The fields of this corner have no valid neighbors in northwest, north and
	 * northeast direction on the board.
	 */
	C4,

	/**
	 * The fields of this corner have no valid neighbors in north, northeast and
	 * southeast direction on the board.
	 */
	C5,

	/**
	 * The fields of this corner have no valid neighbors in northeast, southeast
	 * and south direction on the board.
	 */
	C6;

	/**
	 * Checks if a given {@link Position} belongs to this corner.
	 * 
	 * @param p
	 *            {@link Position} to check
	 * @param board
	 *            the board that knows which positions are valid and which are
	 *            not
	 * @return true if the given position belongs to the corner
	 */
	public boolean contains(Position p, Board board) {
		switch (this) {
		case C1:
			return !board.isValidField(p.getSouth())
					&& !board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& !board.isValidField(p.getSouthEast());
		case C2:
			return !board.isValidField(p.getSouth())
					&& !board.isValidField(p.getSouthWest())
					&& !board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case C3:
			return board.isValidField(p.getSouth())
					&& !board.isValidField(p.getSouthWest())
					&& !board.isValidField(p.getNorthWest())
					&& !board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case C4:
			return board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& !board.isValidField(p.getNorthWest())
					&& !board.isValidField(p.getNorth())
					&& !board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case C5:
			return board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& !board.isValidField(p.getNorth())
					&& !board.isValidField(p.getNorthEast())
					&& !board.isValidField(p.getSouthEast());
		case C6:
			return !board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& !board.isValidField(p.getNorthEast())
					&& !board.isValidField(p.getSouthEast());
		default:
			return false;
		}
	}

}
