package jUnitTests;

import static org.junit.Assert.*;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.ImageProcessor;


public class MatToBinaryTest {


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
	public void test() {
		
		//testing the mat to binary method by calling a method in ImageProcessor.
		ImageProcessor ip = new ImageProcessor();
		
		int[][] array = ip.diffAsBinArray();
		
		for (int i = 0; i < 800; i++)
		{
			for ( int j = 0; j < 500; j++)
			{
				if ((array[i][j] != 0) || (array[i][j] != 1))
				{
					fail("The binary array is not properly created");
				}
			}
		}
	}

}
