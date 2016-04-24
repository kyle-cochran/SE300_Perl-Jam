package src;

import org.bytedeco.javacpp.avutil;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

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
	private WritableImage bkg;
	Frame framesrc;

	public CameraDriver() {

		// create a grabber object to extract frames from this camera
		frameGrabber = new FFmpegFrameGrabber("http://construction1.db.erau.edu/mjpg/video.mjpg");
		frameGrabber.setSampleRate(10);
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
		
		return lotFrame;
	}
	
	public WritableImage getWritableImage() {

		framesrc = getImage();
		
		Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		BufferedImage bf = paintConverter.getBufferedImage(framesrc, 1);

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
	
	public synchronized void updateUILiveFeed(){
		bkg = getWritableImage();
		try{
			RiddleRunAroundParking.ui.pane.setBackground(
					new Background(new BackgroundImage(bkg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
							BackgroundPosition.DEFAULT, new BackgroundSize(100, 100, true, true, true, true))));
		}catch(NullPointerException e){
			System.out.println("laggy internet");
		}
	}

}// end CameraDriver
