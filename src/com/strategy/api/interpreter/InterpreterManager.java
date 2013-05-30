package com.strategy.api.interpreter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ralph Dürig
 */
public class InterpreterManager {

	private static ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor();

	public static void scheduleInterpreter(Thread interpreter) {
		scheduler.scheduleWithFixedDelay(interpreter, 100, 100,
				TimeUnit.MILLISECONDS);
	}

	static void exit() {
		scheduler.shutdown();
	}

}
