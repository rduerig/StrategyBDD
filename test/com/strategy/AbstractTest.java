package com.strategy;

import org.junit.After;
import org.junit.Before;

import com.strategy.util.BddFactoryProvider;
import com.strategy.util.preferences.Preferences;

/**
 * Abstract test class. Sets the buddy library and the force generate files flag
 * before each test and resets the bdd factory after each test.
 * 
 * @author Ralph Dürig
 * 
 */
public abstract class AbstractTest {

	@Before
	public void before() {
		System.setProperty("bdd", "bdd");
		Preferences.getInstance().setGenerateFiles(true);
	}

	@After
	public void after() {
		BddFactoryProvider.reset();
	}

}
