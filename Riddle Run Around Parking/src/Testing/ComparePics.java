package test;




import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class ComparePics
{
	   public static void main(String[] args) throws Exception{

		    //grab a frame from the video file
			FrameGrabber grabber = new FFmpegFrameGrabber("/home/kyle/Documents/SE_300_Files/parking_lot_1.mp4");
		    
			//make a canvas to show it in
			
			CanvasFrame canvasFrame1 = new CanvasFrame("Pic");
			//CanvasFrame canvasFrame2 = new CanvasFrame("Pic");
			CanvasFrame canvasFrame3 = new CanvasFrame("Pic");
		  
		    
		    grabber.start();
		    
		    
		    //initialize image objects
		    IplImage frame = null;
		    IplImage image = null;
		    IplImage image2 = null;
		    IplImage diff = null;
		    
		    
		    //make converter object
		    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		    
		    
		    //convert first frame to grayscale
		    frame = converter.convert(grabber.grab());
		    
		    //save
		    //cvSaveImage("/home/kyle/Documents/SE_300_Files/frame1.jpg",frame);
		    cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
		    image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
		    cvCvtColor(frame, image, CV_RGB2GRAY);
		    canvasFrame1.showImage(converter.convert(image));
		    
		    

		    image2 = cvLoadImage("/home/kyle/Documents/SE_300_Files/frame1_edited.jpg", CV_LOAD_IMAGE_GRAYSCALE);
		    
		    cvSmooth(image2, image2, CV_GAUSSIAN, 9, 9, 2, 2);
		    
		    //LOADING THE IMAGE AS IT'S TRUE COLORS THEN CONVERTING IT WILL NOT WORK FOR SOME REASON
		    //cvCvtColor(emptyFrame, image2, CV_RGB2GRAY);
		    
		    //canvasFrame2.showImage(converter.convert(image2));
		    
		    
		    //find difference between grayscale images
		    diff = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            cvAbsDiff(image, image2, diff);
            // do some threshold for wipe away useless details
            cvThreshold(diff, diff, 25, 250, CV_THRESH_BINARY);
            cvSaveImage("/home/kyle/Documents/SE_300_Files/diff.jpg", diff);
            canvasFrame3.showImage(converter.convert(diff));
   }
}
