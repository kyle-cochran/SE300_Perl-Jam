
import java.lang.Object;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle; 
import javafx.scene.shape.*;
 
public class DisplayUI extends Pane {
	BorderPane borderpane = new BorderPane ();
	Rectangle r = addRetangle();
	
	HBox hbox= addHBox();
	VBox vbox= addVbox();
	
	//help here please if you know what do, I am not sure why
	//it wont add :( 
	//borderpane.setTop(hbox); 
	//borderpane.setLeft(vbox);
	//borderpane.setCenter(r);
	
	
	public HBox addHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");
		
		Button buttonPHistory=new Button("Parking History");
		buttonPHistory.setPrefSize(100,20);

		return hbox;
	}	
	
	public VBox addVbox() {
		VBox vbox=new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(8);
		
		Text title= new Text("Available Parking Spots");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		vbox.getChildren().add(title);
		
		//insert data regarding parking availability here
		return vbox;
		
	}

	public Rectangle addRetangle() {
		Rectangle rectangle = new Rectangle();
		r.setX(50);
		r.setY(50);
		r.setWidth(200);
		r.setHeight(100);
		r.setArcWidth(20);
		r.setArcHeight(20);
		
		// set background to parking lot
		
		return rectangle;
		
	} 
		
	
	
	
	
	

}
