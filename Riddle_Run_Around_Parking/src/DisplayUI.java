import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
	VBox infoPanel;
	VBox spacing;
	HBox hbox;
	HBox title;
	
	File parkingHistoryFile = new File("Parking Spot History.txt");
	
	
	public DisplayUI(){
		borderpane = new BorderPane ();
		r = addRectangle();
		PHbutton = buttonHistory();
		infoPanel = addInfoPanel();
		spacing = addSpacing();
		hbox = addHBox();
		title = addTitle();
	}
	
	
	
	/** 
	 * Creates a new method that creates a new button that when clicked
	 * will display the parking history.
	 * 
	 * @return buttonPHistory A button to access the parking history
	 */
	public Button buttonHistory(){
		Button button = new Button("Parking History");
		button.setPrefSize(200,20);
		button.setOnAction(e ->{
			//try throw catch for the file I/O
			try{
				readHistory();
			}catch (Exception e1){
				e1.printStackTrace();
			}
		});
		
		return button;
	}
	/** the readHistory uses the buffered reader to read the high score file and
	 * print it out into a text field. With its own label and pane*/
	
	private void readHistory(){
		BufferedReader buffRead = null;
		try{
			//tries to read parking history file
			buffRead = new BufferedReader(new FileReader(parkingHistoryFile));
		
			String currentLine= "", fileText = "";
		
			while((currentLine = buffRead.readLine()) != null){
				fileText += currentLine+ '\n';
		}
		//closes the buffered reader when all of the text has been read and printed
		buffRead.close();
		
		//create a text label
		Label historyLabel = new Label();
		historyLabel.setWrapText(true);
		historyLabel.setTextAlignment(TextAlignment.CENTER);
		historyLabel.setFont(Font.font("Comic Sans MS", 14));
		historyLabel.setText(fileText);
		
		//Add label to stack pane
		
		StackPane pane = new StackPane();
		pane.getChildren().add(historyLabel);
		
		//a new stage, and a scene with a pane inside it. 
		Scene phscene = new Scene( pane, 550, 100);
		Stage phstage = new Stage();
		
		//create and display the pane in a new stage
		phstage.setScene(phscene);
		phstage.setTitle("Parking Spot History");
		phstage.setResizable(false);
		phstage.show();
		
		//if the file can not be read the error is caught and given an exception
		}catch (IOException e){
			e.printStackTrace();
		}
		
		
		}
//This method allows data to be written to a file, this might not go here
	//but i thought i would create it then we could move it
	//TODO: find out what we want in the parkingHistoryFile
	//TODO: create a parkingHistoryFile
/*private void writeHistorytoFile(String parkingdate) throws IOException{
	
	File parkingHistoryFile = new File("Parking Spot History.txt");
	parkingHistoryFile.createNewFile();
	
	BufferedWriter buffWriter = new  BufferedWriter(new FileWriter(parkingHistoryFile, true));
	//data we want in the file put here
	//buffWriter.write(String.format());
	//buffWriter.write(String.format("[%s] %s: Number of Clicks to Victory: %d\n", new Date().toString(), playerName, gridPaneOpponent.getnClicks())); 
//closes the buffered writer
	buffWriter.close();
} */
		
		
	
	/** 
	 * Creates a vbox that will
	 * display the data regarding the parking availability 
	 * 
	 * @return vbox a vbox to show available spots
	 */
	public VBox addInfoPanel() {
		VBox vbox=new VBox();
		//vbox.setPadding(new Insets(10));
		//vbox.setSpacing(8);
		
		Text title= new Text("Available Parking Spots");
		title.maxWidth(200);
		title.setWrappingWidth(200);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		title.setStyle("-fx-font-size: 42");
		
		Label parkingInfo = new Label("Future Parking information text goes here");
		parkingInfo.setWrapText(true);
		parkingInfo.setMaxWidth(200);
		parkingInfo.setTextAlignment(TextAlignment.CENTER);
		
		
		vbox.getChildren().addAll(title, parkingInfo, PHbutton);
		
		//insert data regarding parking availability here
		return vbox;
		
	}
	
	
	
	public VBox addSpacing() {
		VBox vbox = new VBox();
		
		Rectangle placeholder = new Rectangle(200,1000);
		placeholder.setFill(Color.WHITE);
		vbox.getChildren().add(placeholder);
		
		return vbox;
	}
	
	public HBox addHBox() {
		HBox hbox = new HBox(100);
		
		Rectangle graph1 = new Rectangle(200,300);
		Rectangle graph2 = new Rectangle(200,300);
		Rectangle graph3 = new Rectangle(200,300);
		
		graph1.setFill(Color.BLACK);
		graph2.setFill(Color.BLACK);
		graph3.setFill(Color.BLACK);
		
		hbox.getChildren().addAll(graph1,graph2,graph3);
		
		return hbox;
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
	
	public HBox addTitle(){
		HBox hbox = new HBox();
		
		Label title = new Label("Riddle Run Around Parking");
		title.setAlignment(Pos.CENTER);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 80");
		title.setTextFill(Color.HOTPINK);
	
		hbox.getChildren().add(title);
		
		return hbox;
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
		borderpane.setTop(title); 
		borderpane.setLeft(infoPanel);
		borderpane.setBottom(hbox);
		borderpane.setRight(spacing);
		
		//creates a new pane that will display the parking lot with highlighted spots
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundImage(new ImageProcessor().IplImageToWritableImage(new
				CameraDriver().getImage()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, 
				new BackgroundSize(100,100,true,true,true,true))));
		pane.getChildren().add(r);
		pane.setMaxSize(500, 500);
		//TODO: MMMMMAAAAAAATTTTTTTTTTTT
		//Create image processor class so the lines can be created
		ImageProcessor ip = new ImageProcessor();
		int[][] lines = ip.getSpotMatrix();
		
		//Create the lines in a loop
		for (int i = 0; i <= lines.length - 1; i++){
			Line temp = new Line(lines[i][0],lines[i][1],lines[i][2],lines[i][3]);
			temp.setStroke(Color.WHITE);
			pane.getChildren().add(temp);
		}
		
		
		
		
		//sets pane to the center of border pane
		borderpane.setCenter(pane);
		
		//displays the scene with the title
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.setTitle("Riddle Run Around Parking");
		primaryStage.show();
		
	}
	}