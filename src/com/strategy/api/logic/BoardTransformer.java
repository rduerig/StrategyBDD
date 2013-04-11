package com.strategy.api.logic;

import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import com.strategy.api.board.Board;

/**
 * Transforms a given {@link Board} to a map between {@link Position}s and
 * {@link BDD}s using a given {@link BDDFactory}.<br>
 * 
 * @author Ralph Dürig
 */
public interface BoardTransformer {

	public Map<Position, BDD> getBDDBoard();

}
