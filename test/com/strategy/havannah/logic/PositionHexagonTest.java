package com.strategy.havannah.logic;

import junit.framework.Assert;

import org.junit.Test;

import com.strategy.api.logic.Position;

/**
 * @author Ralph Dürig
 */
public class PositionHexagonTest {

	@Test
	public void test() {
		Position root = PositionHexagon.get(1, 1);

		// not neighbors to (1,1): (2,0),(0,2)
		Position west = PositionHexagon.get(2, 0);
		Assert.assertFalse(root.isNeighbour(west));
		Position east = PositionHexagon.get(0, 2);
		Assert.assertFalse(root.isNeighbour(east));

		// neighbors to (1,1): (0,0), (0,1), (1,0), (2,1), (1,2), (2,2)
		Position north = PositionHexagon.get(0, 0);
		Assert.assertTrue(root.isNeighbour(north));
		Position northWest = PositionHexagon.get(1, 0);
		Assert.assertTrue(root.isNeighbour(northWest));
		Position northEast = PositionHexagon.get(0, 1);
		Assert.assertTrue(root.isNeighbour(northEast));
		Position south = PositionHexagon.get(2, 2);
		Assert.assertTrue(root.isNeighbour(south));
		Position southWest = PositionHexagon.get(2, 1);
		Assert.assertTrue(root.isNeighbour(southWest));
		Position southEast = PositionHexagon.get(1, 2);
		Assert.assertTrue(root.isNeighbour(southEast));
	}

}
