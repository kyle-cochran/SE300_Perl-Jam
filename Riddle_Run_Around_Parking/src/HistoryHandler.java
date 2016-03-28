import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;





/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:18 AM
 */
public class HistoryHandler {

	private File historyFile;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	
	
	public HistoryHandler(){
		historyFile = new File("src/7_day_history.txt");

	}


	public void commitData(boolean[] spotStates){
	}

	
	
	public void retrieveData(boolean[] array){
		try {	
	         dbFactory = DocumentBuilderFactory.newInstance();
	         dBuilder = dbFactory.newDocumentBuilder();
	         
	         Document doc = dBuilder.parse(historyFile);
	         doc.getDocumentElement().normalize();
	         
	         System.out.println("Root element :" 
	            + doc.getDocumentElement().getNodeName());
	         NodeList nList = doc.getElementsByTagName("student");
	         System.out.println("----------------------------");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            System.out.println("\nCurrent Element :" 
	               + nNode.getNodeName());
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               System.out.println("Student roll no : " 
	                  + eElement.getAttribute("rollno"));
	               System.out.println("First Name : " 
	                  + eElement
	                  .getElementsByTagName("firstname")
	                  .item(0)
	                  .getTextContent());
	               System.out.println("Last Name : " 
	               + eElement
	                  .getElementsByTagName("lastname")
	                  .item(0)
	                  .getTextContent());
	               System.out.println("Nick Name : " 
	               + eElement
	                  .getElementsByTagName("nickname")
	                  .item(0)
	                  .getTextContent());
	               System.out.println("Marks : " 
	               + eElement
	                  .getElementsByTagName("marks")
	                  .item(0)
	                  .getTextContent());
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}

	public boolean[][] retrieveData(boolean[][] array){
		return null;
	}

	public void setHistoryFile(){

	}
}//end HistoryHandler