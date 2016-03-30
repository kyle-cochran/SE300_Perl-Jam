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

	
	public ProcessingManager() {
		refreshFreq = 1; // indicates that analysis should refresh once per
							// second
		procOn = false;
		imP = new ImageProcessor();
	}
	
	public ProcessingManager(int rf) {
		refreshFreq = rf; // indicates that analysis should refresh once per
							// second
		procOn = false;
		imP = new ImageProcessor();
	}

	public void beginProcThread() {

		if (t == null) {
			t = new Thread(this, "proc-thread");
			t.start();
		}
	}

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

	public void run() {

		updateSpots();

		procOn = true;

		while (procOn) {
			try {
				Thread.sleep(1000/refreshFreq);
			} catch (InterruptedException e) {
				System.out.println("Yo dude, the thread got interupted");
				e.printStackTrace();
			}

			updateSpots();
		}
	}

	public void updateSpots() {
		currentSpots = imP.returnNewestStates();
	}

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