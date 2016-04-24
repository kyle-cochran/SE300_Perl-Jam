package jUnitTests;

import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Vector;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.HistoryHandler;
import src.ImageProcessor;
import src.MatToBinary;
import src.ProcessingManager;

public class Tester {
	
//	ProcessingManager pm;
	
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
//		pm = new ProcessingManager();
	}

	@After
	public void tearDown() throws Exception {
//		pm.endProcThread();
	}

	
	
	/**
	 * @author Kyle Cochran
	 */
	
//	@Test
//	public void testBeginProcThread() {
//		pm.beginProcThread();
//		assertTrue(pm.procOn);
//	}
//
//	
//	@Test
//	public void testEndProcThread(){
//		pm.beginProcThread();
//		pm.endProcThread();
//		assertFalse(pm.procOn);
//	}
	
	/**
	 * @author Austin Musser
	 */
	
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
	
	/**
	 * @author Matthew Caixeiro
	 */
	@Test
	public void test1HHPercents() {
		HistoryHandler hH = new HistoryHandler();
		double[][] percents = hH.getAllPercents();
		for(int i = 0; i <= 7; i++) {
			for(int j = 0; j <= 27; j++){
				if((percents[i][j] < 0) || (percents[i][j] > 100)){
					fail("The percent value is negative or greater than 100.");
				}
			}
		}
	}

	
	
	
}
