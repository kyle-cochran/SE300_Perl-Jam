package src;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	private volatile int[] currentSpots;
	public int refreshFreq;
	public ImageProcessor imP;
	public HistoryHandler hH;
	public volatile boolean procOn;
	private volatile boolean timeToUpdate;
	private volatile boolean okayToUpdate = true;
	private Thread t;
	HistoryHandler history = new HistoryHandler();
	ImageProcessor ip = new ImageProcessor();
	int[][] lines = ip.getSpotMatrix();
	private int count = 0;
	Vector<Polygon> polyVec = new Vector<Polygon>();
	Calendar cal = Calendar.getInstance();

	/**
	 * Default constructor. Auto-sets refresh frequency to 1 per second.
	 */
	public ProcessingManager() {
		refreshFreq = 1; // indicates that analysis should refresh once per
		// second
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

		procOn = true;

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
			int minutes = Calendar.getInstance().getTime().getMinutes();

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

	public void getCurrentPercent() {

		int total = 0;

		for (int i = 0; i < currentSpots.length; i++) {
			total += currentSpots[i];
		}
		int percent = 100 * total / currentSpots.length;

		//Update UI with cool stuff
		DisplayUI.parkingPercent.setText(String.format(percent + "%% of the spots in this lot are currently full."));

		// get current date time with Calendar
		DisplayUI.timeText.setText(String.format("Time: " + cal.getTime()));
	}



	public DataInputStream getLotVideoFeed() {
		return null;
	}

	public int[] getLotHistory() {
		return null;
	}

	public void setLotHistory(int[] newHist) {
	}

	public synchronized void lineColor(){

		int[][] lines = new ImageProcessor().getSpotMatrix();
		int[] percentFull = getCurrentSpots();
		if (count == 0){
			count = 1;
			for (int i = 0;  i <= 3; i++) {
				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});
				if ((percentFull[i] == 0) ) {
					temp.setFill(Color.YELLOW);
				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				DisplayUI.pane.getChildren().add(polyVec.elementAt(i)); 
			}
			for (int i = 5;  i <= 10; i++) {
				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});
				if ((percentFull[i] == 0) ) {
					temp.setFill(Color.YELLOW);
				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				DisplayUI.pane.getChildren().add(polyVec.elementAt(i)); 
			}
			for (int i = 12;  i <= 24; i++) {

				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});

				if ((percentFull[i] == 0) ) {
					temp.setFill(Color.YELLOW);
				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				DisplayUI.pane.getChildren().add(polyVec.elementAt(i)); 
			}
			for (int i = 26;  i <= 30; i++) {
				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});
				if ((percentFull[i] == 0) ) {
					temp.setFill(Color.YELLOW);

				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				DisplayUI.pane.getChildren().add(polyVec.elementAt(i)); 
			}
		} else {
			for (int i = 0;  i <= 30; i++) {

				if ((percentFull[i] == 0) ) {
					polyVec.elementAt(i).setFill(Color.YELLOW);
				} else {
					polyVec.elementAt(i).setFill(null);
				}
			} 
		}
	}

		//			Line temp = new Line(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
		//			if ((percentFull[i] == 0) ) {
		//				temp.setStroke(Color.YELLOW);
		//				temp.setStroke(Color.YELLOW);
		//				temp.setStrokeWidth(2.5);
		//				temp.setStrokeLineCap(StrokeLineCap.SQUARE);
		//			} else {
		//				temp.setStroke(Color.WHITE);
		//				temp.setStrokeWidth(2.5);
		//				temp.setStrokeLineCap(StrokeLineCap.SQUARE); 
		//			}
		//&& (percentFull[i + 1] >= 60)
		// DisplayUI.pane.getChildren().add(DisplayUI.rectangle);

}// end ProcessigManager
