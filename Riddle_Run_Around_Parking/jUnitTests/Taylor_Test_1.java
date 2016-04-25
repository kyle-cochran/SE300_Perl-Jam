package jUnitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.HistoryHandler;

public class Taylor_Test_1 {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHH_IMTS() {
		HistoryHandler hh = new HistoryHandler();
		int mat [] = {1, 2, 3};
		assertEquals( hh.intMatToStr(mat), "123");
		
	}
	@Test
	public void testHH_STIM(){
		HistoryHandler hh_1 = new HistoryHandler();
		String str = "456";
		int result[] = hh_1.strToIntMat(str);
		assertEquals(result[0], 4);
		assertEquals(result[1], 5);
		assertEquals(result[2], 6);
			
		}


}
