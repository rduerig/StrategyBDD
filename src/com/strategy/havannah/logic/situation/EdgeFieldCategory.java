package com.strategy.havannah.logic.situation;

import com.strategy.api.board.Board;
import com.strategy.api.logic.Position;

/**
 * Describes the six edges of a hexagon clockwise beginning with the bottom left
 * (namely E1).
 * 
 * @author Ralph Dürig
 */
public enum EdgeFieldCategory {
	/**
	 * The fields of this edge have no valid neighbours in south and southwest
	 * direction on the board.
	 */
	E1,

	/**
	 * The fields of this edge have no valid neighbours in southwest and
	 * northwest direction on the board.
	 */
	E2,

	/**
	 * The fields of this edge have no valid neighbours in northwest and north
	 * direction on the board.
	 */
	E3,

	/**
	 * The fields of this edge have no valid neighbours in north and northeast
	 * direction on the board.
	 */
	E4,

	/**
	 * The fields of this edge have no valid neighbours in northeast and
	 * southeast direction on the board.
	 */
	E5,

	/**
	 * The fields of this edge have no valid neighbours in southeast and south
	 * direction on the board.
	 */
	E6;

	/**
	 * Checks if a given {@link Position} belongs to this edge.
	 * 
	 * @param p
	 *            {@link Position} to check
	 * @param board
	 *            the board that knows which positions are valid and which are
	 *            not
	 * @return true if the given position belongs to the edge
	 */
	public boolean contains(Position p, Board board) {
		switch (this) {
		case E1:
			return !board.isValidField(p.getSouth())
					&& !board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case E2:
			return board.isValidField(p.getSouth())
					&& !board.isValidField(p.getSouthWest())
					&& !board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case E3:
			return board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& !board.isValidField(p.getNorthWest())
					&& !board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case E4:
			return board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& !board.isValidField(p.getNorth())
					&& !board.isValidField(p.getNorthEast())
					&& board.isValidField(p.getSouthEast());
		case E5:
			return board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& !board.isValidField(p.getNorthEast())
					&& !board.isValidField(p.getSouthEast());
		case E6:
			return !board.isValidField(p.getSouth())
					&& board.isValidField(p.getSouthWest())
					&& board.isValidField(p.getNorthWest())
					&& board.isValidField(p.getNorth())
					&& board.isValidField(p.getNorthEast())
					&& !board.isValidField(p.getSouthEast());
		default:
			return false;
		}
	}

}
