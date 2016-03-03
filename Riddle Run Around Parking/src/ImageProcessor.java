import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 * @author Austin, Kyle, Matt, Taylor
 * @version 1.0
 * @created 18-Feb-2016 11:36:20 AM
 */
public class ImageProcessor  {

	private Frame lotFrame;
	private IplImage lotIplImage;
	private IplImage lotIplImage_gray;
	private IplImage refPic;
	private IplImage diff;
	//private boolean[] spotMatrix;
	
	private CameraDriver camDrive = new CameraDriver();
	CanvasFrame canvasFrame = new CanvasFrame("Test");
	OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	

	public ImageProcessor(){
		refPic = cvLoadImage("media/frame1_edited_all_empty.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		cvSmooth(refPic, refPic, CV_GAUSSIAN, 9, 9, 2, 2);
	}
	
	public void Process(){
		
		//obtain frame and convert to Ipl image
		lotFrame = camDrive.getImage();
		lotIplImage = converter.convert(lotFrame);
		
		cvSmooth(lotIplImage, lotIplImage, CV_GAUSSIAN, 9, 9, 2, 2);
		
		lotIplImage_gray = IplImage.create(lotIplImage.width(), lotIplImage.height(), IPL_DEPTH_8U, 1);
		diff = IplImage.create(lotIplImage.width(), lotIplImage.height(), IPL_DEPTH_8U, 1);
		
	    cvCvtColor(lotIplImage, lotIplImage_gray, CV_RGB2GRAY);
		canvasFrame.showImage(converter.convert(refPic));
        cvAbsDiff(lotIplImage_gray, refPic, diff);

        cvThreshold(diff, diff, 25, 250, CV_THRESH_BINARY);
        
        
	
	}

//	private boolean[] generateSpotMatrix(){
//		return null;
//	}

	public int[][] getSpotMatrix(){
		int[][] lines = new int[31][4];
		
		lines[0][0] = 209;
		lines[0][1] = 165;
		lines[0][2] = 201;
		lines[0][3] = 201;
		
		lines[1][0] = 246;
		lines[1][1] = 165;
		lines[1][2] = 241;
		lines[1][3] = 204;
		
		lines[2][0] = 294;
		lines[2][1] = 168;
		lines[2][2] = 292;
		lines[2][3] = 206;
		
		lines[3][0] = 342;
		lines[3][1] = 160;
		lines[3][2] = 344;
		lines[3][3] = 210;
		
		lines[4][0] = 392;
		lines[4][1] = 175;
		lines[4][2] = 400;
		lines[4][3] = 212;
		
		//Grass area between these lines
		
		lines[5][0] = 436;
		lines[5][1] = 176;
		lines[5][2] = 452;
		lines[5][3] = 216;
		
		lines[6][0] = 485;
		lines[6][1] = 180;
		lines[6][2] = 505;
		lines[6][3] = 216;
		
		lines[7][0] = 533;
		lines[7][1] = 182;
		lines[7][2] = 555;
		lines[7][3] = 218;
		
		lines[8][0] = 580;
		lines[8][1] = 184;
		lines[8][2] = 597;
		lines[8][3] = 222;
		
		lines[9][0] = 620;
		lines[9][1] = 188;
		lines[9][2] = 644;
		lines[9][3] = 224;
		
		lines[10][0] = 661;
		lines[10][1] = 191;
		lines[10][2] = 680;
		lines[10][3] = 224;
		
		lines[11][0] = 700;
		lines[11][1] = 193;
		lines[11][2] = 725;
		lines[11][3] = 225;

		//New row
		
		lines[12][0] = 203;
		lines[12][1] = 228;
		lines[12][2] = 192;
		lines[12][3] = 276;

		lines[13][0] = 266;
		lines[13][1] = 230;
		lines[13][2] = 260;
		lines[13][3] = 295;
		
		lines[14][0] = 318;
		lines[14][1] = 233;
		lines[14][2] = 320;
		lines[14][3] = 300;
		
		lines[15][0] = 370;
		lines[15][1] = 236;
		lines[15][2] = 378;
		lines[15][3] = 300;
		
		lines[16][0] = 422;
		lines[16][1] = 238;
		lines[16][2] = 437;
		lines[16][3] = 301;
		
		lines[17][0] = 472;
		lines[17][1] = 240;
		lines[17][2] = 494;
		lines[17][3] = 301;
		
		lines[18][0] = 521;
		lines[18][1] = 242;
		lines[18][2] = 550;
		lines[18][3] = 303;
		
		lines[19][0] = 570;
		lines[19][1] = 244;
		lines[19][2] = 600;
		lines[19][3] = 303;
		
		lines[20][0] = 621;
		lines[20][1] = 244;
		lines[20][2] = 650;
		lines[20][3] = 301;
		
		lines[21][0] = 665;
		lines[21][1] = 246;
		lines[21][2] = 694;
		lines[21][3] = 303;
		
		lines[22][0] = 703;
		lines[22][1] = 247;
		lines[22][2] = 737;
		lines[22][3] = 301;
		
		lines[23][0] = 741;
		lines[23][1] = 247;
		lines[23][2] = 777;
		lines[23][3] = 300;
		
		lines[24][0] = 780;
		lines[24][1] = 251;
		lines[24][2] = 813;
		lines[24][3] = 299;
		
		lines[25][0] = 817;
		lines[25][1] = 250;
		lines[25][2] = 847;
		lines[25][3] = 295;
		
		//New line
		
		lines[26][0] = 98;
		lines[26][1] = 398;
		lines[26][2] = 70;
		lines[26][3] = 494;
		
		lines[27][0] = 180;
		lines[27][1] = 400;
		lines[27][2] = 165;
		lines[27][3] = 496;
		
		lines[28][0] = 250;
		lines[28][1] = 404;
		lines[28][2] = 246;
		lines[28][3] = 495;
		
		lines[29][0] = 323;
		lines[29][1] = 405;
		lines[29][2] = 327;
		lines[29][3] = 496;
		
		lines[30][0] = 394;
		lines[30][1] = 405;
		lines[30][2] = 410;
		lines[30][3] = 494;
		
		lines[31][0] = 464;
		lines[31][1] = 405;
		lines[31][2] = 490;
		lines[31][3] = 495;
		
		//End
		
		return lines;
	}
}//end ImageProcessor
