package com.strategy.util;

import java.math.BigInteger;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.MicroFactory;

/**
 * @author Ralph Dürig
 */
public class TestMain {

	public static void main(String[] args) {
		BDDFactory fac = MicroFactory.init(1000, 1000);
		fac.setVarNum(3);

		BDD bdd = fac.ithVar(0).orWith(fac.ithVar(1)).orWith(fac.ithVar(2));
		fac.extDomain(BigInteger.valueOf(3));
		BDD bdd2 = fac.getDomain(0).set();

		BDD exist = fac.ithVar(0).exist(bdd);

		System.out.println(bdd.satCount());
		System.out.println(bdd.nodeCount());

		System.out.println(bdd);
		System.out.println();

		System.out.println(exist.satCount());
		System.out.println(exist.nodeCount());

		System.out.println(exist);
		System.out.println();

		System.out.println(bdd2.satCount());
		System.out.println(bdd2.nodeCount());

		System.out.println(bdd2);

	}

}
