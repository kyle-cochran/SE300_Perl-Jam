package src;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;


/**
 * Class responsible for access the video feed
 * 
 * @author Austin Musser
 * @version 2.0
 */
public class CameraDriver {

	private Frame lotFrame;
	private FrameGrabber frameGrabber;
	
	ImageProcessor imageP = new ImageProcessor(); 
	
	
	public CameraDriver(){
		
		//create a grabber object to extract frames from this camera
		frameGrabber = new FFmpegFrameGrabber("http://construction1.db.erau.edu/mjpg/video.mjpg");
		
	    //grab a frame from the video file
		try {
			frameGrabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempts to grab the next frame of the input video feed/file.
	 * 
	 * @return lotFrame the next frame of the acquired video feed/file
	 */
	
	public Frame getImage() {
		try {
			lotFrame = frameGrabber.grab();
			imageP.IplImageToWritableImage(lotFrame);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lotFrame;
	}

}//end CameraDriver