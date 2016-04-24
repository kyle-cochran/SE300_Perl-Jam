package jUnitTests;

import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.junit.Assert.fail;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.MatToBinary;


public class MatToBinaryTest {
	
	private static IplImage image;
	private static Mat mat;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		// Load a test image
		image = cvLoadImage("src/media/frame1_edited_all_empty.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		
		// Create the converters
		OpenCVFrameConverter.ToIplImage iplConverter= new OpenCVFrameConverter.ToIplImage();
		OpenCVFrameConverter.ToMat matConverter = new OpenCVFrameConverter.ToMat();
		
		// Convert the IplImage to a Mat
		mat = matConverter.convert(iplConverter.convert(image));
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
	public void test1ToBinaryArray() {
		
		MatToBinary mtb = new MatToBinary();
		
		// Getting the return value from the method
		int[][] array = mtb.toBinaryArray(mat);
		
		// Looping though every spot in the array
		for (int i = 0; i < 800; i++)
		{
			for ( int j = 0; j < 500; j++)
			{
				// Checking if any spot in the array is not a 0 or 1
				if ((array[i][j] != 0) && (array[i][j] != 1))
				{
					// If a pot in the array is not 1 or 0 then it fails
					fail("The binary array is not properly created");
				}
			}
		}
	}

}
