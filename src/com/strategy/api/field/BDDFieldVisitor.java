/**
 * 
 */
package com.strategy.api.field;

import net.sf.javabdd.BDD;

/**
 * Visits a field and derives an according BDD.
 * @author Ralph Dürig
 */
public interface BDDFieldVisitor extends FieldVisitor {

	BDD getBDD();
	
}
