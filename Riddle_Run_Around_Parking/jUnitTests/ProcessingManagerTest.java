package jUnitTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.ProcessingManager;

public class ProcessingManagerTest {

	ProcessingManager pm;

	@Before
	public void setUp() throws Exception {
		pm = new ProcessingManager();
	}

	@After
	public void tearDown() throws Exception {
		pm.endProcThread();
	}

	@Test
	public void testBeginProcThread_1() {
		pm.beginProcThread();
		assertTrue(pm.procOn);
	}

	
	@Test
	public void testEndProcThread(){
		pm.beginProcThread();
		pm.endProcThread();
		assertFalse(pm.procOn);
	}
}
