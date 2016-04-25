package src;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Platform;

/**
 * @author Kyle Cochran
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
@SuppressWarnings("deprecation")
public class ProcessingManager implements Runnable {

	private volatile DisplayUI ui;
	private volatile int[] currentSpots;
	public volatile float bkgRefreshFreq;
	public volatile float paintRefreshFreq;
	public volatile float infoRefreshFreq;
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

	//Runnable objects allow scheduling tasks to the UI to prevent thread errors
	//They are method calls run by using: Platform.runLater(RunnableObject)
	Runnable scheduledBkgUpdate = new Runnable() {
		@Override
		public void run() {updateUIBkg();}
	};
	Runnable scheduledSpotDrawing = new Runnable() {
		@Override
		public void run() {updateUISpots();}
	};
	Runnable scheduledInfoChange = new Runnable() {
		@Override
		public void run() {updateUIInfo();}
	};
	Runnable scheduledAddGraphs = new Runnable() {
		@Override
		public void run() {addGraphs();}
	};


	/**
	 * Default constructor. Auto-sets refresh frequency to 1 per second.
	 */
	public ProcessingManager() {
		bkgRefreshFreq = 1; // indicates that analysis should refresh once per
		// second
		bkgRefreshFreq = 20;
		paintRefreshFreq = (float) 0.2;
		infoRefreshFreq = 1;
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
		bkgRefreshFreq = rf;

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
		//Process a frame to start with initial data. Results stored in "currentSpots" variable
		updateSpots();

		int procCount = 0;
		int minutes;
		procOn = true;
		boolean waiting = true;

		//Waiting for the UI to boot up so that we can reference and update UI objects
		while (waiting){
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
		Platform.runLater(scheduledAddGraphs);

		//Enter the continuous processing loop
		while (procOn) {
			try {
				//pause between loops
				Thread.sleep((long) (1000 / bkgRefreshFreq));
			} catch (InterruptedException e) {
				System.out.println("Yo dude, the thread got interupted");
				e.printStackTrace();
			}

			//The background image will update every loop (loop timing defined by: bkgRefreshFreq)
			updateUIBkg();

			//within the loop, if the timing also coincides with the timing for repainting spots or info on UI, then run those operations
			if(paintRefreshFreq*procCount/bkgRefreshFreq>=1){
				updateSpots();//Process a frame. Results stored in "currentSpots" variable
				Platform.runLater(scheduledSpotDrawing);//using new data, repaint spots
			}else if(infoRefreshFreq*procCount/bkgRefreshFreq>=1){
				Platform.runLater(scheduledInfoChange);//refresh clock
			}else if(procCount>100){
				procCount=0;//can't let the counter get too high
			}

			// logic to update history at certain times of day-------------------
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
			// ------------------------------------------------------------------

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

	/**
	 * Calculates the current percent full of the lot
	 * 
	 * @return an int that represents the current percent full of the lot
	 */
	public int getCurrentPercent() {
		int total = 0;
		for (int i = 0; i < currentSpots.length; i++) {
			total += currentSpots[i];
		}
		return 100 * total / currentSpots.length;
	}

	/**
	 * An access method that allows a UI reference to be set. This gives the ProcessingManager
	 * accesss to UI elements.
	 * 
	 * @param ui a DisplayUI object that runs in tandem with the processing loop
	 */
	public void setUIRef(DisplayUI ui){
		this.ui = ui;
	}

	/**
	 * Returns a reference to the ImageProcessor object. This allows methods inside the UI to access image
	 * processing functionality.
	 * @return
	 */
	public ImageProcessor returnImProcRef() {
		return imP;
	}

	/**
	 * An error catching wrapper method that updates the lot background image
	 */
	public synchronized void updateUIBkg(){
		try{
			cd.updateUILiveFeed();
		}catch(NullPointerException e){
			System.out.println("there was a null pointer when updating UI background from PM"); 

		}
	}
	
	/**
	 * An error catching wrapper method that updates the UI info panel
	 */
	public synchronized void updateUIInfo(){
		try{
			ui.updateUIPercent(getCurrentPercent());
		}catch(NullPointerException e){
			System.out.println("there was a null pointer when updating UI info panel from PM");
		}
	}

	/**
	 * An error catching wrapper method that repaints spots on the UI
	 */
	public synchronized void updateUISpots(){
		try{			ui.lineColor();
		}catch(NullPointerException e){
			System.out.println("there was a null pointer when painting new UI spots from PM"); 

		}
	}

	/**
	 * An error catching wrapper method that
	 */
	public synchronized void addGraphs(){
		ui.addGraphs();
	}
}

// end ProcessigManager

