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
	public void test1GetSpotMatirx() {
		
		ImageProcessor ip = new ImageProcessor();
		
		// Getting the return value from the method
		int[][] array = ip.getSpotMatrix();
		
		// Looping though every spot in the array
		for (int i = 0; i < 32; i++)
		{
			for ( int j = 0; j < 4; j++)
			{
				// Check the X range of the pixel values in the array
				if ((j == 0) || (j == 2))
				{
					if ((array[i][j] < 0) || (array[i][j] > 800))
					{
						//If the X value is less than 0 or greater than 800 then it fails
						fail("This pixel is not in the X range of the imaage");
					}
				}
				// Check the Y range of the pixel values in the array
				else if ((j == 1) || (j == 3))
				{
					//If the Y value is less than 0 or greater than 500 then it fails
					if ((array[i][j] < 0) || (array[i][j] > 500))
					{
						fail("This pixel is not in the Y range of the imaage");
					}
				}
			}
		}
	}

}
