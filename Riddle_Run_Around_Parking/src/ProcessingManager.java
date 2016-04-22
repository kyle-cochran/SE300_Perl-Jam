package src;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;


/**
 * @author Kyle Cochran
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
public class ProcessingManager implements Runnable {

	private volatile int[] currentSpots;
	public volatile int refreshFreq;
	public volatile int uiRefresh;
	public ImageProcessor imP;
	public HistoryHandler hH;
	public volatile boolean procOn;
	private volatile boolean timeToUpdate;
	private volatile boolean okayToUpdate = true;
	private Thread t;
	HistoryHandler history = new HistoryHandler();
	ImageProcessor ip = new ImageProcessor();
	int[][] lines = ip.getSpotMatrix();


	/**
	 * Default constructor. Auto-sets refresh frequency to 1 per second.
	 */
	public ProcessingManager() {
		refreshFreq = 1; // indicates that analysis should refresh once per
		// second
		uiRefresh = 2;//amount of seconds between UI refreshes
		procOn = false;
		imP = new ImageProcessor();
		hH = new HistoryHandler();
	}

	/**
	 * Constructs with a custom refresh frequency.
	 * 
	 * @param rf
	 *            an integer. (refreshes per second)
	 */
	public ProcessingManager(int rf) {
		refreshFreq = rf;

		procOn = false;
		imP = new ImageProcessor();
		hH = new HistoryHandler();
	}

	/**
	 * Method used to begin thread. Checks for pre-existing threads, then
	 * initializes and begins.
	 */
	public void beginProcThread() {

		if (t == null) {
			t = new Thread(this, "proc-thread");
			t.start();
		}else{
		System.out.println("Error: The processing thread failed to initialize. This was likely caused by the presence of a pre-existing processing thread");
		}
	}

	/**
	 * Method to terminate thread. Changes boolean to end processing loop, waits
	 * to let the thread finish current loop, then attempts to erase the thread
	 * from memory.
	 */
	public void endProcThread() {

		procOn = false; // make the processing loop able to end

		try {
			t.join(); // waits for the thread to die naturally
		} catch (InterruptedException e) {
			System.out.println(
					"Error: The thread was interrupted when trying to finish execution. How rude.");
			e.printStackTrace();
		}

		if (!t.isAlive()) {
			t = null; // if the thread died successfully, clear the variable
		} else {
			System.out.println("Error: killing the thread failed. Try harder next time.");
		}
	}

	/**
	 * The entry point for the thread. Must be implemented when class implements
	 * "Runnable". Updates "currentSpots" in a timed loop.
	 */
	public void run() {

		updateSpots();
		
		int minutes;
		
		procOn = true;

		//this will loop to make the processing continuous
		while (procOn) {
			try {
				Thread.sleep(1000 / refreshFreq);
			} catch (InterruptedException e) {
				System.out.println("Yo dude, the thread got interupted");
				e.printStackTrace();
			}

			// get newest spot data
			updateSpots();
			

			//should update the highlight
			lineColor();
			
			//Update UI
			getCurrentPercent();

			// logic to update history at certain times of
			// day-----------------------------------------------------------------------
			minutes = GregorianCalendar.getInstance().getTime().getMinutes();

			// checks whether it's 00 or 30 minutes into the hour
			timeToUpdate = (minutes == 0 || minutes == 30);

			// if it's 00 or 30 minutes in an hour, and we haven't updated yet,
			// add the spots to history, deactivate update switch
			if (timeToUpdate && okayToUpdate) {
				hH.appendCurrentTime(currentSpots);
				okayToUpdate = false;
			}

			// if we're past update time, turn the switch back on.
			if (minutes == 1 || minutes == 31) {
				okayToUpdate = true;
			}
			// -----------------------------------------------------------------------------------------------------------------------
		
			
				//Update UI
				updateUIPercent(getCurrentPercent());
				updateUILiveFeed(imP.IplImageToWritableImage(imP.returnCurrentFrame()));
				
		}
	}

	/**
	 * Wrapper method to update currentSpots variable.
	 */
	public void updateSpots() {
		currentSpots = imP.returnCurrentSpots();
	}

	/**
	 * Wrapper method to return currentSpots variable.
	 * 
	 * @return currentSpots an array of integers that represents the current
	 *         state of the lot.
	 */
	public int[] getCurrentSpots() {
		return currentSpots;
	}

	public int getCurrentPercent() {

		int total = 0;

		for (int i = 0; i < currentSpots.length; i++) {
			total += currentSpots[i];
		}
		return 100 * total / currentSpots.length;
	}

	public void updateUIPercent(int percent){
		//Update UI with cool stuff
		DisplayUI.parkingPercent.setText(String.format(percent + "%% of the spots in this lot are currently full."));

		// get current date time with Calendar
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		DisplayUI.timeText.setText(String.format("Time: " + cal.getTime()));
	}
	
	public void updateUILiveFeed(WritableImage wr){
				DisplayUI.pane.setBackground(
				new Background(new BackgroundImage(wr, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.DEFAULT, new BackgroundSize(100, 100, true, true, true, true))));
	}
	public void lineColor(){
		
	int[] spotStates = getCurrentSpots();
	for (int i = 0; i < 28; i++) {
		Line temp = new Line(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
		if ((spotStates[i] == 0) ) {
			temp.setStroke(Color.YELLOW);
			temp.setStroke(Color.YELLOW);
			temp.setStrokeWidth(30);
			temp.setStrokeLineCap(StrokeLineCap.SQUARE);
		} else {
			temp.setStroke(Color.WHITE);
			temp.setStrokeWidth(2.5);
			temp.setStrokeLineCap(StrokeLineCap.SQUARE); 
		}
//&& (percentFull[i + 1] >= 60)
		DisplayUI.pane.getChildren().add(temp);
		// DisplayUI.pane.getChildren().add(DisplayUI.rectangle);
	}
	}
	
	
	public ImageProcessor returnImProcRef(){
		return imP;
	}
}// end ProcessigManager
