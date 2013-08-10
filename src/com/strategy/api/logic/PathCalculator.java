package com.strategy.api.logic;

import net.sf.javabdd.BDD;

import com.strategy.util.StoneColor;

public interface PathCalculator {
	BDD getPath(Position p, Position q, StoneColor color);

	void log();

	void done();
}