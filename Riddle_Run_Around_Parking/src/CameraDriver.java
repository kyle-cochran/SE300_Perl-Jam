import java.io.File;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;


/**
 * Class responsible for access the video feed/file
 * 
 * @author Austin Musser
 * @version 1.0
 * @created 18-Feb-2016 11:36:16 AM
 */
public class CameraDriver {

	private Frame lotFrame;
	private File videoFile;
	private FrameGrabber frameGrabber;
	
	
	public CameraDriver(){
		
		//Find the lot video file and saves as a File object
		videoFile = new File("src/media/parking_lot_1.mp4");
		
		//create a grabber object to extract frames from the video file
		frameGrabber = new FFmpegFrameGrabber(videoFile);
		
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lotFrame;
	}

	//	private boolean acquireFeed(){
	//		return false;
	//	}

	//	public DataInputStream streamFeed(){
	//		return null;
	//	}

	//	private void takeImage(){
	//
	//	}

}//end CameraDriver