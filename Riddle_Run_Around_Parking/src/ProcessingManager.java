
package src;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.GregorianCalendar;

import javafx.application.Platform;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import java.util.Vector;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

/**
 * @author Kyle Cochran
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
public class ProcessingManager implements Runnable {

	private volatile DisplayUI ui;
	private volatile int[] currentSpots;
	public volatile int refreshFreq;
	public volatile int uiRefresh;
	public volatile ImageProcessor imP;
	public HistoryHandler hH;
	public volatile boolean procOn;
	private volatile boolean timeToUpdate;
	private volatile boolean okayToUpdate = true;
	private Thread t;
	HistoryHandler history;
	ImageProcessor ip = new ImageProcessor();
	CameraDriver cd = new CameraDriver();
	int[][] lines = ip.getSpotMatrix();

	
	
	Calendar cal = Calendar.getInstance();

	Runnable scheduledBkgUpdate = new Runnable() {
		@Override
		public void run() {updateUIBkg();}
	};
	
	Runnable scheduledUIUpdate = new Runnable() {
		@Override
		public void run() {updateUI();}
	};

	Runnable addGraphs = new Runnable() {
		@Override
		public void run() {addGraphs();}
	};


	private int count = 0;
	Vector<Polygon> polyVec = new Vector<Polygon>();


	
	
	/**
	 * Default constructor. Auto-sets refresh frequency to 1 per second.
	 */
	public ProcessingManager() {
		refreshFreq = 1; // indicates that analysis should refresh once per
		// second
		uiRefresh = 2;// amount of seconds between UI refreshes
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
		} else {
			System.out.println(
					"Error: The processing thread failed to initialize. This was likely caused by the presence of a pre-existing processing thread");
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
			System.out.println("Error: The thread was interrupted when trying to finish execution. How rude.");
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
		int procCount = 0;
		int minutes;

		procOn = true;
		boolean waiting = true;

		while (waiting){
			//Waiting for the UI to boot up so that we can reference and update UI objects

			try{

				if(!RiddleRunAroundParking.ui.equals(null)){
					waiting = false;
				}

			}catch(NullPointerException e){
				System.out.println("Waiting for UI to fully initialize.....");
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Yo dude, the thread got interupted");
			}
		}
		//after we're sure that the UI is loaded, we'll replace the dummy graphs with real ones
		Platform.runLater(addGraphs);

		// this will loop to make the processing continuous
		while (procOn) {
			try {
				Thread.sleep(1000 / refreshFreq);
			} catch (InterruptedException e) {
				System.out.println("Yo dude, the thread got interupted");
				e.printStackTrace();
			}


			// get newest spot data
			if(procCount>100){
			updateSpots();
			Platform.runLater(scheduledUIUpdate);
			procCount=0;
			
			}
			
			Platform.runLater(scheduledBkgUpdate);
			//updateUIBkg();
			// get newest spot data


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
			procCount++;
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

	public void setUIRef(DisplayUI ui){
		this.ui = ui;
	}

	public ImageProcessor returnImProcRef() {
		return imP;
	}

	public void updateUIBkg(){
	try{
		cd.updateUILiveFeed();

	}catch(NullPointerException e){
		System.out.println("there was a null pointer when updating UI (changing elements) from PM"); 

	}
}

	public synchronized void updateUI(){
		try{
			ui.updateUIPercent(getCurrentPercent());
			ui.lineColor();
		}catch(NullPointerException e){
			System.out.println("there was a null pointer when updating UI (changing elements) from PM"); 

		}
	}

	public synchronized void addGraphs(){
		ui.addGraphs();
	}
}