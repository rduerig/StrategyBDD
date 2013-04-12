package com.strategy.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.strategy.api.field.Field;

/**
 * This class parses the information in a BDD string that can be accessed by
 * {@link BDD#toString()}
 * 
 * @author Ralph Dürig
 */
public class BDDSatParser {

	private final BDD someResult;
	private final BDDFactory fac;
	private final Map<Integer, Field> allFields;
	private List<Field> satFields;

	public BDDSatParser(BDD someResult, BDDFactory fac,
			Collection<Field> allFields) {
		this.someResult = someResult;
		this.fac = fac;
		this.allFields = initAllFields(allFields);
	}

	public List<Field> getSatFields() {
		if (null == satFields) {
			satFields = createSatFields();
		}
		return satFields;
	}

	// ************************************************************************

	private Map<Integer, Field> initAllFields(Collection<Field> fields) {
		Map<Integer, Field> result = Maps.uniqueIndex(fields,
				getFieldIndexFunction());
		return result;
	}

	private Function<Field, Integer> getFieldIndexFunction() {
		Function<Field, Integer> keyFunction = new Function<Field, Integer>() {
			@Override
			public Integer apply(Field input) {
				return input.getIndex();
			}
		};

		return keyFunction;
	}

	private List<Field> createSatFields() {
		List<Field> result = Lists.newLinkedList();
		// List<RawField> rawFields = createRawFields();
		// for (RawField rawField : rawFields) {
		// result.add(allFields.get(rawField.getIndex()));
		// }

		List<byte[]> allsat = someResult.allsat();
		byte[] sat = Iterables.getFirst(allsat, new byte[0]);
		for (int i = 0; i < sat.length; i++) {
			if (sat[i] == 1) {
				result.add(allFields.get(i));
			}
		}

		return result;
	}

	private List<RawField> createRawFields() {
		List<RawField> result = Lists.newLinkedList();
		int[] set = new int[fac.varNum()];
		createSatFieldsRec(fac, result, someResult, set);
		return result;
	}

	/**
	 * @see BDD#bdd_printset_rec (originally used to create a bdd string)
	 */
	private void createSatFieldsRec(BDDFactory f, List<RawField> rawList,
			BDD r, int[] set) {

		if (r.isZero())
			return;
		else if (r.isOne()) {
			for (int n = 0; n < set.length; n++) {
				if (set[n] > 0) {
					rawList.add(new RawField(f.level2Var(n), (set[n] == 2 ? 1
							: 0)));
				}
			}
		} else {
			set[f.var2Level(r.var())] = 1;
			BDD rl = r.low();
			createSatFieldsRec(f, rawList, rl, set);
			rl.free();

			set[f.var2Level(r.var())] = 2;
			BDD rh = r.high();
			createSatFieldsRec(f, rawList, rh, set);
			rh.free();

			set[f.var2Level(r.var())] = 0;
		}
	}

	// ************************************************************************

	private class RawField {
		private int index;
		private int set;

		public RawField(int index, int set) {
			this.index = index;
			this.set = set;
		}

		/**
		 * @return the index of the field
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @return 1 if the field should set with white, 0 otherwise
		 */
		public int getSet() {
			return set;
		}
	}
}
