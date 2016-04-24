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
 * Class responsible for displaying main window and children objects
 * 
 * @author Taylor Hester, Matthew Caixeiro, Austin Musser
 * @version 2.0
 */

public class DisplayUI extends Pane {
	BorderPane borderpane;
	Rectangle r;
	Button PHbutton;
	VBox infoPanel;
	HBox hbox;
	HBox title;
	VBox spacing;
	Menu menu; 
	MenuBar menuBar;
	Menu menuAbout;
	MenuItem myAbout;
	File parkingHistoryFile = new File("Parking Spot History.txt");
	static Pane pane = new Pane();
	static Rectangle rectangle;
	static Label parkingPercent = new Label("Default Text");
	static Label timeText = new Label();
	ProcessingManager pm;
	HistoryHandler history = new HistoryHandler();

	/*
	 * call methods to create a rectangle, button and vbox their return value is
	 * then set to a corresponding variable so that the create objects can be
	 * displayed in a stage
	 */

	String[] timeOfDay = { "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM",
			"11:00 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM",
			"4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM",
			"8:30 PM", "9:00 PM" };
	 
	public DisplayUI(ProcessingManager pm) {
		borderpane = new BorderPane();
		r =  addRectangleNoI();
		PHbutton = buttonHistory();
		infoPanel = addInfoPanel();
		spacing = addSpacing();
		hbox = addHBox();
		title = addTitle();
		this.pm = pm;
		addMenu();
	}
	
	public void addMenu(){
		menuAbout = new Menu("Directions");
		myAbout = new MenuItem("About This Program");
		menuBar = new MenuBar();
		menuAbout.getItems().add(myAbout);
		menuBar.getMenus().addAll(menuAbout);
		
		myAbout.setOnAction(e -> showAbout());
		
	}
	private void showAbout(){
		final String aboutText = "Welcome to the Riddle Run Around Parking Application"
				+"The yellow highlights show where there are open spots. Please do not"
				+"use this application and drive. Thank you.";
		
		Label aboutLabel = new Label();
		aboutLabel.setWrapText(true);
		aboutLabel.setTextAlignment(TextAlignment.CENTER);
		aboutLabel.setFont(Font.font("Comic Sans MS", 14));
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

		// TODO call a method to get these values
		int[] percentFull = { 10, 50, 80, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
				100, 100, 100, 100, 100, 100, 100, 100, 100 };// = new int[27];
		// double[] percentFull = history.getDaysAgoPercents(7); //Get parking
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

	public LineChart lastWeekYesterday() {
		// TODO call a method to get these values
		int[] percentFull = { 10, 50, 80, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
				100, 100, 100, 100, 100, 100, 100, 100, 100 };
		// double[] percentFull = history.getDaysAgoPercents(8); //Get parking
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

	public LineChart lastWeekTomorrow() {
		// TODO call a method to get these values
		int[] percentFull = { 10, 50, 80, 70, 90, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
				100, 100, 100, 100, 100, 100, 100, 100, 100 };
		// double[] percentFull = history.getDaysAgoPercents(6); //Get parking
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

	/**
	 * the readHistory uses the buffered reader to read the high score file and
	 * print it out into a text field. With its own label and pane
	 */

	private void readHistory() {
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

	}
	

	


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

		vbox.getChildren().addAll(title, parkingPercent, PHbutton, fill, dateTimeTitle, dateText, timeText);
		// add all does not accept date and calendar types

		// insert data regarding parking availability here
		return vbox;

	}

	public VBox addSpacing() {
		VBox vbox = new VBox();

		Rectangle placeholder = new Rectangle(800, 500);
		placeholder.setFill(Color.WHITE);
		vbox.getChildren().add(placeholder);

		return vbox;
	}

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

	/**
	 * Initializes the main window and sets all display sub-components
	 * 
	 * @param primaryStage
	 *            a stage to host children objects
	 */
	public void start(Stage primaryStage) throws Exception {
		// creates a new scene
		Scene scene = new Scene(borderpane);
		// sets the created button and vbox to a location within the border pane
		borderpane.setTop(title);
		borderpane.setLeft(infoPanel);
		borderpane.setBottom(hbox);
		borderpane.setRight(spacing);

		// creates a new pane that will display the parking lot with highlighted
		// spots
		pane.getChildren().add(r);
		pane.setMinSize(800, 500);

		// Create image processor class so the lines can be created
		ImageProcessor ip = new ImageProcessor();
		int[][] lines = ip.getSpotMatrix();

		for (int i = 0; i <= 31; i++){
			Line temp = new Line(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
			temp.setStroke(Color.WHITE);
			temp.setStrokeWidth(2.5);
			temp.setStrokeLineCap(StrokeLineCap.SQUARE);
			pane.getChildren().add(temp);
		}
		// Create the lines in a looplines.length
//		int[] percentFull = pm.getCurrentSpots();
//		for (int i = 0; i < 28; i++) {
//			Line temp = new Line(lines[i][0], lines[i][1], lines[i][2], lines[i][3]);
//			if ((percentFull[i] == 0) ) {
//				temp.setStroke(Color.YELLOW);
//				temp.setStrokeWidth(2.5);
//				temp.setStrokeLineCap(StrokeLineCap.SQUARE);
//				
//			} else {
//				temp.setStroke(Color.WHITE);
//				temp.setStrokeWidth(2.5);
//				temp.setStrokeLineCap(StrokeLineCap.SQUARE);
//			}
//			pane.getChildren().add(temp);
//		
//		}

		// sets pane to the center of border pane
		borderpane.setCenter(pane);

		// displays the scene with the title
		primaryStage.setScene(scene);
		primaryStage.setTitle("Riddle Run Around Parking");
		primaryStage.show();

	}
}
