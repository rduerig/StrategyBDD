package com.strategy.util;

import com.strategy.api.field.BlackStone;
import com.strategy.api.field.EmptyField;
import com.strategy.api.field.Field;
import com.strategy.api.field.WhiteStone;
import com.strategy.api.logic.Position;

public class FieldGenerator {

	/**
	 * Creates a new field with the given index depending on the given number.<br>
	 * Mapping is as follows ('*' means any number except '1' or '2'):<br>
	 * <table>
	 * <th>Number</th>
	 * <th>Resulting field type</th>
	 * <tr>
	 * <td>*</td>
	 * <td>{@link EmptyField}</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>{@link WhiteStone}</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>{@link BlackStone}</td>
	 * </tr>
	 * </table>
	 * 
	 * @param primitive
	 *            the number that should be converted into a {@link Field}
	 * @param index
	 *            the index on the board the field should represent
	 * @return
	 */
	public static Field create(Integer primitive, Position pos, Integer index) {
		switch (primitive) {
		case 1:
			return new WhiteStone(pos, index);
		case 2:
			return new BlackStone(pos, index);
		default:
			return new EmptyField(pos, index);
		}
	}

}
