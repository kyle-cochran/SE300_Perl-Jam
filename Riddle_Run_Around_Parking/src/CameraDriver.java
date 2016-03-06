import java.net.URL;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;


/**
 * @author Kyle, Taylor, Matt and Austin
 * @version 1.0
 * @created 18-Feb-2016 11:36:16 AM
 */
public class CameraDriver {

	private Frame lotFrame;
	//	private DataInputStream lotVideoFeed;
	private FrameGrabber frameGrabber;
	
	
	public CameraDriver(){
		
		
        URL location = CameraDriver.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location.getFile());
	   
	    //grab a frame from the video file
        String videoPath = location.getFile()+"../src/SE_300_Files/parking_lot_1.mp4";
		
	
		
		frameGrabber = new FFmpegFrameGrabber(videoPath);
		
		try {
			frameGrabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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