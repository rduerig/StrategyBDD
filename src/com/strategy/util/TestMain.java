package com.strategy.util;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.MicroFactory;

/**
 * @author Ralph Dürig
 */
public class TestMain {

	public static void main(String[] args) {
		BDDFactory fac = MicroFactory.init(1000, 1000);
		fac.setVarNum(2);

		BDD bdd = fac.ithVar(0).orWith(fac.nithVar(0)).orWith(fac.ithVar(1));

		System.out.println(bdd.satCount());
		System.out.println(bdd.nodeCount());

		System.out.println(bdd);

	}

}
