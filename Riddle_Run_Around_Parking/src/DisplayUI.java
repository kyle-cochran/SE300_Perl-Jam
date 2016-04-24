package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
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
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

/**
 * Class responsible for displaying main window and children objects.
 * DisplayUI extends Pane so that the objects created within Display 
 * UI can be seen by the user. Many different Javafx instances were
 * used to create the final product. 
 * 
 * @author Taylor Hester, Matthew Caixeiro, Austin Musser
 * @version 2.0
 */
@SuppressWarnings("rawtypes")
public class DisplayUI extends Pane {
	BorderPane borderpane;
	Rectangle r;
	Button PHbutton;
	VBox infoPanel;
	HBox graphsBox;
	HBox title;
	VBox spacing;
	VBox vboxTop;
	MenuBar menuBar;
	Menu menuAbout;
	MenuItem myAbout;
	File parkingHistoryFile = new File("Parking_History.txt");
	Pane pane = new Pane();
	Rectangle rectangle;
	Label parkingPercent = new Label("Default Text");
	Label timeText = new Label();
	ProcessingManager pm;
	HistoryHandler history;
	private int count = 0;
	Vector<Polygon> polyVec = new Vector<Polygon>();
	Calendar cal;
	ImageProcessor ip;

	String[] timeOfDay = { "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM",
			"11:00 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM",
			"4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM",
			"8:30 PM", "9:00 PM" };
	/**
	 * Display UI calls methods to create a rectangle, vbox their return value is then
	 * set to a corresponding variable so that the created objects can be displayed 
	 * in the stage. Display UI is passed Processing Manager so the parking lines
	 * and highlights can be updated.
	 * 
	 * @return void 
	 */

	public DisplayUI(ProcessingManager pm) {
		borderpane = new BorderPane();
		r =  addRectangleNoI();
		//PHbutton = buttonHistory();
		//infoPanel = addInfoPanel();
		//spacing = addSpacing();
		//graphsBox = addDummyGraphs();
		//title = addTitle();
		this.pm = pm;
		history = pm.hH;
		//addMenu();
	}
	
	public MenuBar addMenu(){
		menuAbout = new Menu("Directions");
		myAbout = new MenuItem("About This Program");
		menuBar = new MenuBar();
		menuAbout.getItems().add(myAbout);
		menuBar.getMenus().addAll(menuAbout);

		myAbout.setOnAction(e -> showAbout());
		return menuBar;

	}
	private void showAbout(){
		final String aboutText = "Welcome to the Riddle Run Around Parking Application "
				+"The yellow highlights show where there are open spots. The graphs shown "
				+ "at the bottom of the screen displays historic parking data. Please do not "
				+"use this application and drive. Thank you.";

		Label aboutLabel = new Label();
		aboutLabel.setWrapText(true);
		aboutLabel.setTextAlignment(TextAlignment.CENTER);
		aboutLabel.setFont(Font.font("Comic Sans MS", 20));
		aboutLabel.setText(aboutText);

		StackPane pane = new StackPane();
		pane.getChildren().add(aboutLabel);

		Scene scene = new Scene(pane, 550, 300);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("About");
		stage.setResizable(false);
		stage.show();
	}	
	
	public LineChart lastWeekToday() {


		int[] percentFull = history.getDaysAgoPercents(7); //Get parking
		for(int i=0;i<percentFull.length;i++){
			//System.out.println(percentFull[i]+"\n");
		}
		// data 7 days ago
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Percent Full");
		xAxis.setLabel("Time");

		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle("Last Week Parking");

		XYChart.Series series = new XYChart.Series();

		for (int i = 0; i < 28; i++) {
			series.getData().add(new XYChart.Data(timeOfDay[i], percentFull[i]));
		}

		lineChart.getData().add(series);

		return lineChart;
	}

	/**
	 * Creates a new method that creates a new line chart that will
	 * display the parking data from last week yesterday. This is done 
	 * with an array and line chart features.
	 * 
	 * @return lineChart
	 */
	public LineChart lastWeekYesterday() {
		int[] percentFull = history.getDaysAgoPercents(8); //Get parking
		// data 8 days ago

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Percent Full");
		xAxis.setLabel("Time");

		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle("Yesterday Last Week");

		XYChart.Series series = new XYChart.Series<>();

		for (int i = 0; i < 28; i++) {
			series.getData().add(new XYChart.Data(timeOfDay[i], percentFull[i]));
		}

		lineChart.getData().add(series);
		return lineChart;

	}
	/**
	 * Creates a new method that creates a new line chart that will
	 * display the data from a week ago tomorrow. This is done with an array
	 * and line chart features.
	 * 
	 * @return lineChart
	 */
	public LineChart lastWeekTomorrow() {
		int[] percentFull = history.getDaysAgoPercents(6); //Get parking
		// data 6 days ago
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Percent Full");
		xAxis.setLabel("Time");

		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle("Tomorrow of Last Week");

		XYChart.Series series = new XYChart.Series<>();

		for (int i = 0; i < 28; i++) {
			series.getData().add(new XYChart.Data(timeOfDay[i], percentFull[i]));
		}

		lineChart.getData().add(series);
		return lineChart;
	}

	/**
	 * Creates a new method that creates a new button that when clicked will
	 * display the parking history.
	 * 
	 * @return buttonPHistory A button to access the parking history
	 */
	/*
	public Button buttonHistory() {
		Button button = new Button("Parking History");
		button.setPrefSize(200, 20);
		button.setOnAction(e -> {
			// try throw catch for the file I/O
			try {
				readHistory();
			} catch (Exception e1) {
			//	e1.printStackTrace();
			}
		});

		return button;
	}

	*//**
	 * the readHistory uses the buffered reader to read the high score file and
	 * print it out into a text field. With its own label and pane
	 *//*

	/*private void readHistory() {
		BufferedReader buffRead = null;
		try {
			// tries to read parking history file
			buffRead = new BufferedReader(new FileReader(parkingHistoryFile));

			String currentLine = "", fileText = "";

			while ((currentLine = buffRead.readLine()) != null) {
				fileText += currentLine + '\n';
			}
			// closes the buffered reader when all of the text has been read and
			// printed
			buffRead.close();

			// create a text label
			Label historyLabel = new Label();
			historyLabel.setWrapText(true);
			historyLabel.setTextAlignment(TextAlignment.CENTER);
			historyLabel.setFont(Font.font("Comic Sans MS", 14));
			historyLabel.setText(fileText);

			// Add label to stack pane

			StackPane pane = new StackPane();
			pane.getChildren().add(historyLabel);

			// a new stage, and a scene with a pane inside it.
			Scene phscene = new Scene(pane, 550, 100);
			Stage phstage = new Stage();

			// create and display the pane in a new stage
			phstage.setScene(phscene);
			phstage.setTitle("Parking Spot History");
			phstage.setResizable(false);
			phstage.show();

			// if the file can not be read the error is caught and given an
			// exception
		} catch (IOException e) {
			//e.printStackTrace();
		}

	}*/

	/**
	 * Creates a vbox that will display the data regarding the parking
	 * availability
	 * 
	 * @return vbox a vbox to show available spots
	 */
	public VBox addInfoPanel() {
	VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(20);
		vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));
		vbox.setAlignment(Pos.CENTER);

		Text title = new Text("Available Parking Spots");
		title.maxWidth(500);
		title.minWidth(500);
		title.setWrappingWidth(200);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		title.setStyle("-fx-font-size: 42");

		parkingPercent.setWrapText(true);
		parkingPercent.setMaxWidth(500);
		parkingPercent.setMinWidth(500);
		parkingPercent.setAlignment(Pos.CENTER);
		parkingPercent.setTextAlignment(TextAlignment.CENTER);
		parkingPercent.setFont(Font.font("Arial", 14));
		parkingPercent.setStyle("-fx-font-size: 18");

		Rectangle fill = new Rectangle();
		fill.setWidth(500);
		fill.setWidth(500);
		fill.setFill(Color.WHITE);
		fill.setStroke(null);

		// date and calender not sure how to add to UI
		// TODO: add these to UI if not already there

		Text dateTimeTitle = new Text();
		Label dateText = new Label();

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();

		dateTimeTitle.setText("Date and Time");
		dateTimeTitle.maxWidth(500);
		dateTimeTitle.minWidth(500);
		dateTimeTitle.setWrappingWidth(200);
		dateTimeTitle.setTextAlignment(TextAlignment.CENTER);
		dateTimeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		dateTimeTitle.setStyle("-fx-font-size: 42");

		dateText.setText(String.format("Date: " + dateFormat.format(date)));
		dateText.setWrapText(true);
		dateText.setMaxWidth(500);
		dateText.setMinWidth(500);
		dateText.setAlignment(Pos.CENTER);
		dateText.setTextAlignment(TextAlignment.CENTER);
		dateText.setFont(Font.font("Arial", 14));
		dateText.setStyle("-fx-font-size: 18");

		timeText.setWrapText(true);
		timeText.setMaxWidth(500);
		timeText.setMinWidth(500);
		timeText.setAlignment(Pos.CENTER);
		timeText.setTextAlignment(TextAlignment.CENTER);
		timeText.setFont(Font.font("Arial", 14));
		timeText.setStyle("-fx-font-size: 18");

		vbox.getChildren().addAll(title, parkingPercent, fill, dateTimeTitle, dateText, timeText);
		// add all does not accept date and calendar types

		// insert data regarding parking availability here
		return vbox;
	}

	/**
	 * 
	 * Method to make a white rectabgle on the right side of the UI for visua appeal
	 * 
	 * @return VBox - a vbox that provides spacing
	 */
	public VBox addSpacing() {
		VBox vbox = new VBox();

		Rectangle placeholder = new Rectangle(800, 500);
		placeholder.setFill(Color.WHITE);
		vbox.getChildren().add(placeholder);

		return vbox;
	}

	public LineChart generateDummyGraph(){
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Percent Full");
		xAxis.setLabel("Time");

		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle("Loading");

		XYChart.Series series = new XYChart.Series<>();
		
		return lineChart;
	}

	public HBox addDummyGraphs(){
		HBox hbox = new HBox(200);
		hbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));

		LineChart graph1 = generateDummyGraph();
		LineChart graph2 = generateDummyGraph();
		LineChart graph3 = generateDummyGraph();

		hbox.getChildren().addAll(graph1, graph2, graph3);

		return hbox;
	}
	/**
	 *
	 * Method to create an HBox that is initialized with the three graphs of parking lot data
	 * 
	 * @return Hbox - an Hbox that contains parking lot data in the form of graphs 
	 */
	public HBox addHBox() {

		HBox hbox = new HBox(200);
		hbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));

		LineChart graph1 = lastWeekToday();
		LineChart graph2 = lastWeekTomorrow();
		LineChart graph3 = lastWeekYesterday();

		hbox.getChildren().addAll(graph1, graph2, graph3);

		return hbox;
	}

	/**
	 * Creates a rectangle that will display the image of the parking lot
	 * 
	 * @return rectangle a rectangle of a set size to fit in the application
	 *         window
	 */


	public static Rectangle addRectangleNoI() {
		Rectangle rectangle = new Rectangle();
		rectangle.setX(50);
		rectangle.setY(50);
		rectangle.setWidth(200);
		rectangle.setHeight(100);
		rectangle.setArcWidth(20);
		rectangle.setArcHeight(20);
		rectangle.setFill(null);

		return rectangle;

	}

	/**
	 * A method that creates an Hbox that contains the title of the project
	 * 
	 * @return Hbox - Hbox containing the title 
	 */
	public HBox addTitle() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));

		Label title = new Label("Riddle Run Around Parking");
		title.setAlignment(Pos.CENTER);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 80");

		hbox.getChildren().add(title);

		return hbox;
	}

	public VBox addTop(){

		VBox vboxTop = new VBox();
		vboxTop.getChildren().addAll(menuBar, title);

		return vboxTop;
	}

	/**
	 * Initializes the main window and sets all display sub-components
	 * 
	 * @param primaryStage
	 *            a stage to host children objects
	 */
	public void start(Stage primaryStage) throws Exception {
		// creates a new scene
		Scene scene = new Scene(borderpane);
		// sets the created button and hbox to a location within the border pane
		borderpane.setTop(vboxTop);
		borderpane.setLeft(infoPanel);
		borderpane.setBottom(graphsBox);
		borderpane.setRight(spacing);

		// creates a new pane that will display the parking lot with highlighted
		// spots
		pane.getChildren().add(r);
		pane.setMinSize(800, 500);

		// Create image processor class so the lines can be created
		//int[][] lines = ip.getSpotMatrix();

		/*
		for (int i = 0; i <= 31; i++){
			Line temp = new Line(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
			temp.setStroke(Color.WHITE);
			temp.setStrokeWidth(2.5);
			temp.setStrokeLineCap(StrokeLineCap.SQUARE);
			pane.getChildren().add(temp);
		}*/

		// sets pane to the center of border pane
		borderpane.setCenter(pane);

		// Closes the thread and the project when the x is clicked
		/*primaryStage.setOnCloseRequest(e -> {
			pm.endProcThread();
			System.exit(0);
		});*/

		// displays the scene with the title
		primaryStage.setScene(scene);
		primaryStage.setTitle("Riddle Run Around Parking");
		primaryStage.show();

	}
	
	public synchronized void updateUIPercent(int percentFull){
		//Update UI with cool stuff
		parkingPercent.setText(String.format(percentFull + "%% of the spots in this lot are currently full."));

		// get current date time with Calendar
		cal = Calendar.getInstance();
		timeText.setText(String.format("Time: " + cal.getTime()));
	}

	public synchronized void updateUILiveFeed(WritableImage bkg){
		try{
			pane.setBackground(
					new Background(new BackgroundImage(bkg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
							BackgroundPosition.DEFAULT, new BackgroundSize(100, 100, true, true, true, true))));
		}catch(NullPointerException e){
			System.out.println("laggy internet");
		}
	}

	public synchronized void addGraphs(){
		graphsBox.getChildren().clear();
		graphsBox.getChildren().addAll(addHBox().getChildren());
	}


	public synchronized void paintLines(){
		int[][] lines = pm.ip.getSpotMatrix();
		Line temp;

		int[] percentFull = pm.getCurrentSpots();

		for (int i = 0; i < 28; i++) {
			temp = new Line(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
			if ((percentFull[i] == 0) ) {
				temp.setStroke(Color.YELLOW);
				temp.setStrokeWidth(30);
				temp.setStrokeLineCap(StrokeLineCap.SQUARE);

			} else {
				temp.setStroke(Color.WHITE);
				temp.setStrokeWidth(2.5);
				temp.setStrokeLineCap(StrokeLineCap.SQUARE);
			}

			pane.getChildren().add(temp);
		}
	}

	/**
	 * Creates polygons to visually represent the spots and highlights the ones
	 * that are empty according to the method getCurrentSpots()
	 */
	public synchronized void lineColor(){
		int[][] lines = new ImageProcessor().getSpotMatrix();
		int[] percentFull = pm.getCurrentSpots();
		if (count == 0){
			count = 1;
			for (int i = 0;  i <= 3; i++) {
				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});
				if ((percentFull[i] == 0) ) {
					temp.setFill(Color.YELLOW);
				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				pane.getChildren().add(polyVec.elementAt(i)); 
			}
			for (int i = 5;  i <= 10; i++) {
				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});
				if ((percentFull[i-1] == 0) ) {
					temp.setFill(Color.YELLOW);
				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				pane.getChildren().add(polyVec.elementAt(i-1)); 
			}
			for (int i = 12;  i <= 24; i++) {

				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});

				if ((percentFull[i-2] == 0) ) {
					temp.setFill(Color.YELLOW);
				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				pane.getChildren().add(polyVec.elementAt(i-2)); 
			}
			for (int i = 26;  i <= 30; i++) {
				Polygon temp = new Polygon(new double[]{
						(double) lines[i][0],(double) lines[i][1],(double) lines[i][2],(double) lines[i][3],
						(double) lines[i+1][2],(double) lines[i+1][3],(double) lines[i+1][0],(double) lines[i+1][1]
				});
				if ((percentFull[i-3] == 0) ) {
					temp.setFill(Color.YELLOW);

				} else {
					temp.setFill(null);
				}
				polyVec.addElement(temp);
				pane.getChildren().add(polyVec.elementAt(i-3)); 
			}
		} else {
			for (int i = 0;  i <= 27; i++) {

				if ((percentFull[i] == 0) ) {
					polyVec.elementAt(i).setFill(Color.YELLOW);
				} else {
					polyVec.elementAt(i).setFill(null);
				}
			} 
		}
	}

}

