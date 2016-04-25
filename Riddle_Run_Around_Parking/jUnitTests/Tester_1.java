package jUnitTests;

import static org.junit.Assert.*;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.HistoryHandler;
import src.ImageProcessor;
import src.MatToBinary;
import src.ProcessingManager;

public class Tester_1 {
	
	private static IplImage image;
	private static Mat mat;
	ProcessingManager pm = new ProcessingManager();

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
		pm.endProcThread();
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
	
	@Test
	public void testBeginProcThread() {
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
