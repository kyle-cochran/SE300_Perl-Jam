import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle; 
import javafx.scene.shape.*;
 

/**
 * Class responsible for displaying main window and children objects
 * 
 * @author Taylor Hester, Matthew Caixeiro
 * @version 1.0
 */


public class DisplayUI extends Pane {
	//create new border pane 
	BorderPane borderpane;
	
	/*call methods to create a rectangle, button and vbox
	 *their return value is then set to a corresponding variable 
	 * so that the create objects can be displayed in a stage
	 */
	
	Rectangle r;
	Button PHbutton;
	VBox vbox;
	
	public DisplayUI(){
		borderpane = new BorderPane ();
		r = addRectangle();
		PHbutton = buttonHistory();
		vbox = addVbox();
	}
	
	
	
	/** 
	 * Creates a new method that creates a new button that when clicked
	 * will display the parking history.
	 * 
	 * @return buttonPHistory A button to access the parking history
	 */
	public Button buttonHistory(){
		Button buttonPHistory=new Button("Parking History");
		buttonPHistory.setPrefSize(500,20);
		
		return buttonPHistory;
	}
	
	/** 
	 * Creates a vbox that will
	 * display the data regarding the parking availability 
	 * 
	 * @return vbox a vbox to show available spots
	 */
	public VBox addVbox() {
		VBox vbox=new VBox();
		//vbox.setPadding(new Insets(10));
		//vbox.setSpacing(8);
		
		Text title= new Text("Available Parking Spots");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		vbox.getChildren().add(title);
		
		//insert data regarding parking availability here
		return vbox;
		
	}
	/** 
	 * Creates a rectangle that will
	 * display the image of the parking lot
	 * 
	 * @return rectangle a rectangle of a set size to fit in the application window
	 */
	public Rectangle addRectangle() {
		Rectangle rectangle = new Rectangle();
		rectangle.setX(50);
		rectangle.setY(50);
		rectangle.setWidth(200);
		rectangle.setHeight(100);
		rectangle.setArcWidth(20);
		rectangle.setArcHeight(20);
		rectangle.setFill(null);
		// set background to parking lot
		
		return rectangle;
		
	} 
		
	/**
	 * Initializes the main window and sets all display sub-components
	 * 
	 * @param primaryStage a stage to host children objects
	 */
	public void start(Stage primaryStage) throws Exception{
		//creates a new scene 
		Scene scene= new Scene(borderpane);
		//sets the created button and vbox to a location within the border pane
		borderpane.setTop(PHbutton); 
		borderpane.setLeft(vbox);
		
		//creates a new pane that will display the parking lot with highlighted spots
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundImage(new Image("file:src/media/frame1.jpg"), //TODO: un-hardcode this path
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, 
				new BackgroundSize(100,100,true,true,true,true))));
		pane.getChildren().add(r);
		//TODO: MMMMMAAAAAAATTTTTTTTTTTT
		
		
		//sets pane to the center of border pane
		borderpane.setCenter(pane);
		
		//displays the scene with the title
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.setTitle("Riddle Run Around Parking");
		primaryStage.show();
		
	}
	}