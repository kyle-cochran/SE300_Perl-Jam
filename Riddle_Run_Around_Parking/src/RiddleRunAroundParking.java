import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class
 * 
 * @author Taylor Hester, Matthew Caixeiro
 * @version 1.0
 */
public class RiddleRunAroundParking extends Application{
	
	private static ImageProcessor imageProc = new ImageProcessor();
	
	public RiddleRunAroundParking(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * Main method: initializes the image processor and UI display.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args){
		//imageProc.Process();
		//launch(args);
		DisplayUI ui = new DisplayUI();
		try {
			ui.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the UI display.
	 * 
	 * @param primaryStage The stage containing the main application window
	 */
	public void start(Stage primaryStage) throws Exception{
		DisplayUI ui = new DisplayUI();
		ui.start(primaryStage);
	}
}//end RiddleRunAroundParking