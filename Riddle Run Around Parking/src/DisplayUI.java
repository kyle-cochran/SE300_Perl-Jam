
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
 
public class DisplayUI extends Region {
	BorderPane border = new BorderPane ();
	HBox hbox = addHBox();
	VBox vbox= addVbox();
	
	private HBox addHBox() {
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
		
		Text title= new Text("Parking Spots");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		vbox.getChildren().add(title);
		return vbox;
	} 
		
	
	
	
	
	

}
