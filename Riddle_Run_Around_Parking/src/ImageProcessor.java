import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Class that manages all image processing and comparison. Handles access and manipulation of frames for comparison.
 * 
 * @author Austin Musser, Kyle Cochran, Matthew Caixeiro
 * @version 1.0
 */
public class ImageProcessor  {
	private CanvasFrame canvas=new CanvasFrame("Test");
	private IplImage lotIplImage;
	private IplImage lotIplImage_gray;
	private IplImage refPic;
	private IplImage diff;
	private Mat matDiff;
	private OpenCVFrameConverter.ToIplImage iplConverter;
	private OpenCVFrameConverter.ToMat matConverter;

	private int[][] binaryArray = new int[1440][1080]; //these values may need to be change later if we crop the pic
	private CameraDriver cameraDriver = new CameraDriver();
	private MatToBinary matToBinary = new MatToBinary();

	
	/**
	 * Constructor initializes image converter objects and loads reference image.
	 */
	public ImageProcessor(){

		//initialize necessary image converters
		iplConverter = new OpenCVFrameConverter.ToIplImage();
		matConverter = new OpenCVFrameConverter.ToMat();
		
		//load reference image from file as greyscale
		refPic = cvLoadImage("src/media/frame1_edited_all_empty.jpg", CV_LOAD_IMAGE_GRAYSCALE);
	}

	
	/**
	 * Takes in the current lot frame as an image and compares it to the lot reference image. 
	 * Converts difference to a binary array that shows which pixels match.
	 * 
	 * @return binaryArray an array of integers that represent the state of each pixel (black/white) of the lot image difference with the reference image.
	 */
	public int[][] diffAsBinArray(){

		lotIplImage = iplConverter.convert(cameraDriver.getImage());

		//add a blur to lot image and reference image to eliminate jitter effects
		cvSmooth(lotIplImage, lotIplImage, CV_GAUSSIAN, 9, 9, 2, 2);
		cvSmooth(refPic, refPic, CV_GAUSSIAN, 9, 9, 2, 2);
		
		//create image containers for the greyscale lot picture and the b/w difference picture
		lotIplImage_gray = IplImage.create(lotIplImage.width(), lotIplImage.height(), IPL_DEPTH_8U, 1);
		diff = IplImage.create(lotIplImage.width(), lotIplImage.height(), IPL_DEPTH_8U, 1);

		//convert lot image to greyscale
		cvCvtColor(lotIplImage, lotIplImage_gray, CV_RGB2GRAY);
		
		//compare lot image with reference and store difference in diff
		cvAbsDiff(lotIplImage_gray, refPic, diff);
		
		
		//modify difference image to ignore some minor changes details
		cvThreshold(diff, diff, 25, 250, CV_THRESH_BINARY);
		
		canvas.showImage(iplConverter.convert(diff));
		
		//convert to mat object, then to custom binary array
		matDiff = matConverter.convert(iplConverter.convert(diff));
		binaryArray = matToBinary.toBinaryArray(matDiff);

		return binaryArray;

	}
	/**
	 * Cycles through binary array picture and looks for objects present in expected lot positions. If objects are present in a certain 
	 * threshold percentage of the lot, the spot is marked full. This state is saved in an ordered array of spot states in the lot.
	 * 
	 * @param binaryArray an array of integers that represents the two-tone lot difference image
	 * @param lines an array of ordered coordinate pairs that represent the parking spot divisor lines
	 * @return isEmpty an array the represents the state of each parking spot in a given lot
	 */
	public int[] generateIsEmptyMatrix(int[][] binaryArray, int[][] lines) {
		int[] isEmpty = new int[28];
		int[] count = new int[28];

		for (int i = 0; i <= count.length-1; i++){
			count[i]=0;
		}
		
		//For the first block of cars
		for (int i = 0; i <= 3; i++) {
			//Go from the lower x bound to the higher x bound
			for (int j = lines[i][0]; j <= lines[i+1][3]; j++){
				//And from the lower y bound to the higher y bounf
				for (int k = lines[i][2]; k <= lines[i+1][4]; k++) {
					//And add them up into count
					count[i] += binaryArray[j][k];
				}
			}

			//If that is less than 60% of the max value for spaces, the spot is empty
			if (count[i] < .6*((int) (Math.abs((double) (lines[i][0] - lines[i+1][3])) *
					Math.abs((double) (lines[i][2] - lines[i][4]))))){
				isEmpty[i] = 1; 
				//If that is greater than 60% of the max value for spaces, the spot is full
			} else {
				isEmpty[i] = 0;
			}
		}

		//Repeat for all other blocks of spots

		for (int i = 5; i <= 10; i++) {
			//Go from the lower x bound to the higher x bound
			for (int j = lines[i][0]; j <= lines[i+1][3]; j++){
				//And from the lower y bound to the higher y bounf
				for (int k = lines[i][2]; k <= lines[i+1][4]; k++) {
					//And add them up into count
					count[i-1] += binaryArray[j][k];
				}
			}

			//If that is less than 60% of the max value for spaces, the spot is empty
			if (count[i-1] < .6*((int) (Math.abs((double) (lines[i][0] - lines[i+1][3])) *
					Math.abs((double) (lines[i][2] - lines[i][4]))))){
				isEmpty[i-1] = 1; 
				//If that is greater than 60% of the max value for spaces, the spot is full
			} else {
				isEmpty[i-1] = 0;
			}
		}

				for (int i = 12; i <= 24; i++) {
					//Go from the lower x bound to the higher x bound
					for (int j = lines[i][0]; j <= lines[i+1][3]; j++){
						//And from the lower y bound to the higher y bounf
						for (int k = lines[i][2]; k <= lines[i+1][4]; k++) {
							//And add them up into count
							count[i-2] += binaryArray[j][k];
						}
					}
					
					//If that is less than 60% of the max value for spaces, the spot is empty
					if (count[i-3] < .6*((int) (Math.abs((double) (lines[i][0] - lines[i+1][3])) *
							Math.abs((double) (lines[i][2] - lines[i][4]))))){
						isEmpty[i-2] = 1; 
						//If that is greater than 60% of the max value for spaces, the spot is full
					} else {
						isEmpty[i-2] = 0;
					}
				}
				
				for (int i = 26; i <= 30; i++) {
					//Go from the lower x bound to the higher x bound
					for (int j = lines[i][0]; j <= lines[i+1][3]; j++){
						//And from the lower y bound to the higher y bounf
						for (int k = lines[i][2]; k <= lines[i+1][4]; k++) {
							//And add them up into count
							count[i-3] += binaryArray[j][k];
						}
					}
					
					//If that is less than 60% of the max value for spaces, the spot is empty
					if (count[i-3] < .6*((int) (Math.abs((double) (lines[i][0] - lines[i+1][3])) *
							Math.abs((double) (lines[i][2] - lines[i][4]))))){
						isEmpty[i-3] = 1; 
						//If that is greater than 60% of the max value for spaces, the spot is full
					} else {
						isEmpty[i-3] = 0;
					}
				}
				
		return isEmpty;
	}

	/**
	 * @ignore
	 */
		private boolean[] generateSpotMatrix(){
			boolean[] temp = {false,false,false};
			return temp;
		}

	/**
	 * Identify where divisor lines are in current lot view.
	 * 
	 * @return lines an array of coordinate pairs that represents the pixel location of parking spots divisor lines
	 */
	public int[][] getSpotMatrix(){
		int[][] lines = new int[32][4];

		int offset = 179;
		
		lines[0][0] = 209;
		lines[0][1] = 165+offset;
		lines[0][2] = 201;
		lines[0][3] = 201+offset;

		lines[1][0] = 246;
		lines[1][1] = 165+offset;
		lines[1][2] = 241;
		lines[1][3] = 204+offset;

		lines[2][0] = 294;
		lines[2][1] = 168+offset;
		lines[2][2] = 292;
		lines[2][3] = 206+offset;

		lines[3][0] = 342;
		lines[3][1] = 160+offset;
		lines[3][2] = 344;
		lines[3][3] = 210+offset;

		lines[4][0] = 392;
		lines[4][1] = 175+offset;
		lines[4][2] = 400;
		lines[4][3] = 212+offset;

		//Grass area between these lines

		lines[5][0] = 436;
		lines[5][1] = 176+offset;
		lines[5][2] = 452;
		lines[5][3] = 216+offset;

		lines[6][0] = 485;
		lines[6][1] = 180+offset;
		lines[6][2] = 505;
		lines[6][3] = 216+offset;

		lines[7][0] = 533;
		lines[7][1] = 182+offset;
		lines[7][2] = 555;
		lines[7][3] = 218+offset;

		lines[8][0] = 580;
		lines[8][1] = 184+offset;
		lines[8][2] = 597;
		lines[8][3] = 222+offset;

		lines[9][0] = 620;
		lines[9][1] = 188+offset;
		lines[9][2] = 644;
		lines[9][3] = 224+offset;

		lines[10][0] = 661;
		lines[10][1] = 191+offset;
		lines[10][2] = 680;
		lines[10][3] = 224+offset;

		lines[11][0] = 700;
		lines[11][1] = 193+offset;
		lines[11][2] = 725;
		lines[11][3] = 225+offset;

		//New row

		lines[12][0] = 203;
		lines[12][1] = 228+offset;
		lines[12][2] = 192;
		lines[12][3] = 276+offset;

		lines[13][0] = 266;
		lines[13][1] = 230+offset;
		lines[13][2] = 260;
		lines[13][3] = 295+offset;

		lines[14][0] = 318;
		lines[14][1] = 233+offset;
		lines[14][2] = 320;
		lines[14][3] = 300+offset;

		lines[15][0] = 370;
		lines[15][1] = 236+offset;
		lines[15][2] = 378;
		lines[15][3] = 300+offset;

		lines[16][0] = 422;
		lines[16][1] = 238+offset;
		lines[16][2] = 437;
		lines[16][3] = 301+offset;

		lines[17][0] = 472;
		lines[17][1] = 240+offset;
		lines[17][2] = 494;
		lines[17][3] = 301+offset;

		lines[18][0] = 521;
		lines[18][1] = 242+offset;
		lines[18][2] = 550;
		lines[18][3] = 303+offset;

		lines[19][0] = 570;
		lines[19][1] = 244+offset;
		lines[19][2] = 600;
		lines[19][3] = 303+offset;

		lines[20][0] = 621;
		lines[20][1] = 244+offset;
		lines[20][2] = 650;
		lines[20][3] = 301+offset;

		lines[21][0] = 665;
		lines[21][1] = 246+offset;
		lines[21][2] = 694;
		lines[21][3] = 303+offset;

		lines[22][0] = 703;
		lines[22][1] = 247+offset;
		lines[22][2] = 737;
		lines[22][3] = 301+offset;

		lines[23][0] = 741;
		lines[23][1] = 247+offset;
		lines[23][2] = 777;
		lines[23][3] = 300+offset;

		lines[24][0] = 780;
		lines[24][1] = 251+offset;
		lines[24][2] = 813;
		lines[24][3] = 299+offset;

		lines[25][0] = 817;
		lines[25][1] = 250+offset;
		lines[25][2] = 847;
		lines[25][3] = 295+offset;

		//New line

		lines[26][0] = 98;
		lines[26][1] = 398+offset;
		lines[26][2] = 70;
		lines[26][3] = 494+offset;

		lines[27][0] = 180;
		lines[27][1] = 400+offset;
		lines[27][2] = 165;
		lines[27][3] = 496+offset;

		lines[28][0] = 250;
		lines[28][1] = 404+offset;
		lines[28][2] = 246;
		lines[28][3] = 495+offset;

		lines[29][0] = 323;
		lines[29][1] = 405+offset;
		lines[29][2] = 327;
		lines[29][3] = 496+offset;

		lines[30][0] = 394;
		lines[30][1] = 405+offset;
		lines[30][2] = 410;
		lines[30][3] = 494+offset;

		lines[31][0] = 464;
		lines[31][1] = 405+offset;
		lines[31][2] = 490;
		lines[31][3] = 495+offset;

		//End

		return lines;
	}
	
	/**
	 * Converts a JavaCV IPLImage object to a JavaFX WritableImage object to allow compatibility with JavaFX graphics.
	 * Code is a hybrid from the following sources:
	 * 
	 * https://blog.idrsolutions.com/2012/11/convert-bufferedimage-to-javafx-image/
 	 * http://stackoverflow.com/questions/31873704/javacv-how-to-convert-iplimage-tobufferedimage 
	 * 
	 * @param src an IPLImage from the OpenCV/JavaCV library
	 * @return wr a WritableImage object (child of Java Image object) from the JavaFX library
	 */
	
	public WritableImage IplImageToWritableImage(Frame framesrc) {

		Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		BufferedImage bf = paintConverter.getBufferedImage(framesrc,1);

		WritableImage wr = null;

		if (bf != null) {
			wr = new WritableImage(bf.getWidth(), bf.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < bf.getWidth(); x++) {
				for (int y = 0; y < bf.getHeight(); y++) {
					pw.setArgb(x, y, bf.getRGB(x, y));
				}
			}
		}

		return wr;
	}
}//end ImageProcessor
