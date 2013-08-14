/**
 * 
 */
package com.strategy.havannah.logic;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.strategy.api.logic.Position;

/**
 * Represents a position on a hexagonal board and provides the according
 * neighborhood calculations. <br>
 * One field on the hexagonal board has a position consisting of a row and a
 * column and has the following appearance:<br>
 * <br>
 * <code>
 * <table cellpadding="0" cellspacing="0">
 * <tr>
 * <td> </td>
 * <td>__</td>
 * <td> </td>
 * </tr>
 * <tr>
 * <td>/</td>
 * <td>  </td>
 * <td>\</td>
 * </tr>
 * <tr>
 * <td>\</td>
 * <td>__</td>
 * <td>/</td>
 * </tr>
 * </table></code><br>
 * <br>
 * A field has 6 edges that can be addressed by one of the directions north,
 * south, northwest, southwest, northeast, southeast. These directions can be
 * accessed by the following operations:<br>
 * <table>
 * <th>Direction</th>
 * <th>Operations on row and column</th>
 * <tr>
 * <td>N</td>
 * <td><code>row + 1, column + 1</code></td>
 * </tr>
 * <tr>
 * <td>S</td>
 * <td><code>row - 1, column - 1</code></td>
 * </tr>
 * <tr>
 * <td>NW</td>
 * <td><code>row + 1</code></td>
 * </tr>
 * <tr>
 * <td>SW</td>
 * <td><code>column - 1</code></td>
 * </tr>
 * <tr>
 * <td>NE</td>
 * <td><code>column + 1</code></td>
 * </tr>
 * <tr>
 * <td>SE</td>
 * <td><code>row - 1</code></td>
 * </tr>
 * </table>
 * 
 * @author Ralph Dürig
 */
public class PositionHexagon implements Position {

	private final int row;
	private final int col;
	private boolean seen;

	public static Position get(int row, int col) {
		return new PositionHexagon(row, col);
	}

	public PositionHexagon(int row, int col) {
		this.row = row;
		this.col = col;
		seen = false;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getCol() {
		return col;
	}

	@Override
	public boolean isSeen() {
		return seen;
	}

	@Override
	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	@Override
	public void setVisited() {
		setSeen(true);
	}

	@Override
	public boolean isNeighbour(Position other) {
		return other.equals(this.getNorth()) || other.equals(this.getSouth())
				|| other.equals(this.getNorthWest())
				|| other.equals(this.getSouthWest())
				|| other.equals(this.getNorthEast())
				|| other.equals(this.getSouthEast()); // || other.equals(this);
	}

	@Override
	public Position getNorth() {
		return new PositionHexagon(row + 1, col + 1);
	}

	@Override
	public Position getSouth() {
		return new PositionHexagon(row - 1, col - 1);
	}

	@Override
	public Position getNorthWest() {
		return new PositionHexagon(row + 1, col);
	}

	@Override
	public Position getSouthWest() {
		return new PositionHexagon(row, col - 1);
	}

	@Override
	public Position getNorthEast() {
		return new PositionHexagon(row, col + 1);
	}

	@Override
	public Position getSouthEast() {
		return new PositionHexagon(row - 1, col);
	}

	@Override
	public Position getEast() {
		return null;
	}

	@Override
	public Position getWest() {
		return null;
	}

	@Override
	public List<Position> getNeighbors() {
		return Lists.newArrayList(getNorth(), getNorthEast(), getSouthEast(),
				getSouth(), getSouthWest(), getNorthWest());
	}

	// ************************************************************************

	@Override
	public String toString() {
		return "(" + row + ", " + col + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(row, col);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		if (col != other.getCol()) {
			return false;
		}
		if (row != other.getRow()) {
			return false;
		}
		return true;
	}

}
