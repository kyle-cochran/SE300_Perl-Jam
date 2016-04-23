package src;

import org.bytedeco.javacpp.avutil;
import java.io.File;
import javax.imageio.ImageIO;

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
@SuppressWarnings("all")
public class CameraDriver {

	private Frame lotFrame;
	private FrameGrabber frameGrabber;
	private int sampleRate = 10;

	public CameraDriver() {

		// create a grabber object to extract frames from this camera
		frameGrabber = new FFmpegFrameGrabber("http://construction1.db.erau.edu/mjpg/video.mjpg");

		// grab a frame from the video file
		try {
			frameGrabber.start();
		} catch (Exception e) {
			frameGrabber = new FFmpegFrameGrabber("src/media/blackImage.png");

			try {
				frameGrabber.start();
			} catch (Exception e1) {
				System.err.println("No internet and no black image...");
			}
			
//			e.printStackTrace();
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ImageProcessor.IplImageToWritableImage(lotFrame);
		return lotFrame;
	}

}// end CameraDriver
