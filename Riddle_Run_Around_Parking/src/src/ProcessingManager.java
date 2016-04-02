package src;
import java.io.*;
import java.util.Calendar;

/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
public class ProcessingManager implements Runnable {

	private volatile int[] currentSpots;
	public int refreshFreq;
	public ImageProcessor imP;
	public HistoryHandler hH;
	private volatile boolean procOn;
	private volatile boolean timeToUpdate;
	private volatile boolean okayToUpdate=true;
	private Thread t;

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
					"Error: The thread was interrupted when trying to finish execution. Looks like it's slacking.");
			e.printStackTrace();
		}

		if (!t.isAlive()) {
			t = null;
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

			//get newest spot data
			updateSpots();
			
			
			//logic to update history at certain times of day-----------------------------------------------------------------------
			int minutes = Calendar.getInstance().getTime().getMinutes();
			
			//checks whether it's 00 or 30 minutes into the hour
			timeToUpdate = (minutes==0 || minutes==30);
			
			//if it's 00 or 30 minutes in an hour, and we haven't update yet, add the spots to history, deactivate update switch
			if(timeToUpdate && okayToUpdate){
				hH.appendCurrentTime(currentSpots);
				okayToUpdate = false;
			}
			
			//if we're past update time, turn the switch back on.
			if(minutes==1 || minutes==31){
				okayToUpdate = true;
			}
			//-----------------------------------------------------------------------------------------------------------------------
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
	
	public double getCurrentPercent(){
		
		int total=0;
		
		for(int i = 0; i<currentSpots.length; i++){
			total += currentSpots[i];
		}
		return 100*total/currentSpots.length;
	}


	public DataInputStream getLotVideoFeed() {
		return null;
	}

	public int[] getLotHistory() {
		return null;
	}

	public void setLotHistory(int[] newHist) {
	}
}// end ProcessigManager