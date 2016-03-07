import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
public class RiddleRunAroundParking extends Application{
	
	private static ImageProcessor imageProc = new ImageProcessor();
	
	public RiddleRunAroundParking(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		imageProc.Process();
		launch(args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		DisplayUI ui = new DisplayUI();
		ui.start(primaryStage);
	}
}//end RiddleRunAroundParking