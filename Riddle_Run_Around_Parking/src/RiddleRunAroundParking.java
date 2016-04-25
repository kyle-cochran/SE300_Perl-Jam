package src;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class
 * 
 * @author Taylor Hester, Matthew Caixeiro
 * @version 1.0
 */
public class RiddleRunAroundParking extends Application {

	//Make the two main objects. The UI and the background processor
	private static ProcessingManager pm = new ProcessingManager(20);
	public static DisplayUI ui;

	/**
	 * Main method: initializes the image processor and UI display.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {

		//begin the two main object threads
		pm.beginProcThread();
		launch(args);
	}

	/**
	 * Starts the UI display.
	 * 
	 * @param primaryStage
	 *            The stage containing the main application window
	 */
	public void start(Stage primaryStage) throws Exception {
		//part of the UI thread. Initializes the UI with a reference to the processor,
		//the gives the processor a reference to the UI. This allows communication
		//between the two main threads.
		ui = new DisplayUI(pm);
		ui.start(primaryStage);
		pm.setUIRef(ui);
	}
}// end RiddleRunAroundParking