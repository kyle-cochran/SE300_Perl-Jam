
import java.lang.Object;

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
 
public class DisplayUI extends Pane {
	//create new borderpane 
	BorderPane borderpane = new BorderPane ();
	/*call methods to create a rectangle, button and vbox
	 *their return value is then set to a corresponding variable 
	 * so that the create objects can be displayed in a stage
	 */
	
	Rectangle r = addRectangle();
	Button PHbutton= buttonHistory();
	VBox vbox= addVbox();
	
	
	/* creates a new method that creates a new button that when clicked
	 * will display the parking history
	 */
	public Button buttonHistory(){
		Button buttonPHistory=new Button("Parking History");
		buttonPHistory.setPrefSize(100,20);
		
		return buttonPHistory;
	}
	
	/* creates a new method that creates a vbox that will
	 * display the data regarding the parking availability 
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
	/* creates a new method that creates a rectangle that will
	 * display the image of the parking lot
	 */

	public Rectangle addRectangle() {
		Rectangle rectangle = new Rectangle();
		r.setX(50);
		r.setY(50);
		r.setWidth(200);
		r.setHeight(100);
		r.setArcWidth(20);
		r.setArcHeight(20);
		r.setFill(null);
		// set background to parking lot
		
		return rectangle;
		
	} 
		
	
	public void start(Stage primaryStage) throws Exception{
		//creates a new scene 
		Scene scene= new Scene(borderpane);
		//sets the created button and vbox to a location within the border pane
		borderpane.setTop(PHbutton); 
		borderpane.setLeft(vbox);
		
		//creates a new pane that will display the parking lot with highlighted spots
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundImage(new Image("image directory string"), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, 
				new BackgroundSize(100,100,true,true,true,true))));
		pane.getChildren().add(r);
		
		//sets pane to the center of border pane
		borderpane.setCenter(pane);
		
		//displays the scene with the title
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.setTitle("Riddle Run Around Parking");
		primaryStage.show();
		
	}
	}
	
	
	


