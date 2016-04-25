package src;

import org.bytedeco.javacpp.avutil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
 * Class responsible for access of the video feed
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
	Java2DFrameConverter paintConverter;
	BufferedImage bf;
	WritableImage wr;
	PixelWriter pw;

	public CameraDriver() {

		
		// create a grabber object to extract frames from this camera
		frameGrabber = new FFmpegFrameGrabber("http://construction1.db.erau.edu/mjpg/video.mjpg");
		frameGrabber.setSampleRate(20);
		
		try {
			frameGrabber.start();
		} catch (Exception e) {
			//if we can't access the video feed, attempt to replace it with a solid black image
			frameGrabber = new FFmpegFrameGrabber("src/media/blackImage.png");
			try {
				frameGrabber.start();
			} catch (Exception e1) {
				//if we can't access the backup image, just print an error
				System.err.println("No internet and no black image...");
			}
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
		}finally{

		}
		return lotFrame;
	}
	
	/**
	 * Converts a grabbed frame to a writable image that can be used as a background by JavaFX
	 * @return a WritableImage that can be posted to the UI
	 */
	public WritableImage getWritableImage() {
		framesrc = getImage();
		paintConverter = new Java2DFrameConverter();
		bf = paintConverter.getBufferedImage(framesrc, 1);
		wr = null;

		if (bf != null) {
			wr = new WritableImage(bf.getWidth(), bf.getHeight());
			pw = wr.getPixelWriter();
			for (int x = 0; x < bf.getWidth(); x++) {
				for (int y = 0; y < bf.getHeight(); y++) {
					pw.setArgb(x, y, bf.getRGB(x, y));
				}
			}
		}
		return wr;
	}
	
	/*
	 * gets a writable image and posts it to the UI
	 */
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
