import java.io.*;

/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
public class ProcessingManager implements Runnable {

	private volatile int[] currentSpots;
	public int refreshFreq;
	public ImageProcessor imP;
	private volatile boolean procOn;
	private Thread t;

	/**
	 * Default constructor. Auto-sets refresh frequency to 1 per second.
	 */
	public ProcessingManager() {
		refreshFreq = 1; // indicates that analysis should refresh once per
							// second
		procOn = false;
		imP = new ImageProcessor();
	}

	/**
	 * Construcs with a custom refresh frequency.
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

			updateSpots();
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

	public DataInputStream getLotVideoFeed() {
		return null;
	}

	public boolean[] getLotHistory() {
		return null;
	}

	public void setLotHistory(boolean[] array) {
	}
}// end ProcessigManager