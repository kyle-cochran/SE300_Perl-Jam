package jUnitTests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.ImageProcessor;

public class ImageProcessorTest {

	
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
	public void test1IsEmptyMatrix() {
		ImageProcessor ip = new ImageProcessor();
		int[][] binaryArray = new int[800][500];
		
		for (int i = 0; i <= 499; i++){
			for (int j = 0; j <= 799; j++){
				binaryArray[j][i] = 1;
			}
		}
		
		int[][] lines = ip.getSpotMatrix();
		
		int[] isEmpty = ip.generateIsEmptyMatrix(binaryArray, lines);
		
		for (int k = 0; k <= isEmpty.length - 1; k++){
			if (isEmpty[k] == 1){
				fail("The matrix contains an incorrect value");
			}
		}
	}

}
