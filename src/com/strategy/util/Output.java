package com.strategy.util;

import java.io.PrintStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.strategy.api.HasDebugFlag;

public class Output {

	private static Map<Class<? extends HasDebugFlag>, Boolean> debugProperties = Maps
			.newHashMap();
	private static PrintStream out = System.out;

	public static void setDebug(Class<? extends HasDebugFlag> classToDebug,
			boolean debug) {
		debugProperties.put(classToDebug, debug);
	}

	public static void print(String text,
			Class<? extends HasDebugFlag> classToDebug) {
		if (canDebug(classToDebug)) {
			out.println(text);
		}
	}

	public static void setPrintStream(PrintStream stream) {
		out = stream;
	}

	private static boolean canDebug(Class<? extends HasDebugFlag> classToDebug) {
		return debugProperties.containsKey(classToDebug)
				&& Boolean.TRUE.equals(debugProperties.get(classToDebug));
	}

}
