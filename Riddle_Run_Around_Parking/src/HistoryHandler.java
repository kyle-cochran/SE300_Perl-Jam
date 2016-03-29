import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:18 AM
 */
public class HistoryHandler {
	//Some class definitions
	File historyFile = new File("/src/media/7_day_history.xml"); //the file where the parking history will be stored
	DocumentBuilderFactory dbFactory; //A document builder builder
	DocumentBuilder dBuilder; //A document builder
	int histL = 7; //the number of past days that will be stored at any one time
	int timeIncr = 28; //the number of different time steps during the day that will be considered
	int numSpots = 32; //the number of parking spots in one lot
	int[][][] spots; //the matrix that holds all spot data, format: [day][time][spot no.]

	//some random dates used for testing
	GregorianCalendar[] dates = {
			new GregorianCalendar(2016,3,20),
			new GregorianCalendar(2016,3,21),
			new GregorianCalendar(2016,3,22),
			new GregorianCalendar(2016,3,23),
			new GregorianCalendar(2016,3,24),
			new GregorianCalendar(2016,3,25),
			new GregorianCalendar(2016,3,26)};

	//All the times of day
	String[] timeOfDay =  {"7:00 AM","7:30 AM","8:00 AM","8:30 AM","9:00 AM","9:30 AM","10:00 AM","10:30 AM","11:00 AM",
			"12:00 PM","12:30 PM","1:00 PM","1:30 PM","2:00 PM","2:30 PM","3:00 PM","3:30 PM","4:00 PM","4:30 PM",
			"5:00 PM","5:30 PM","6:00 PM","6:30 PM","7:00 PM","7:30 PM","8:00 PM","8:30 PM","9:00 PM"};

	/*
	public static void main(String[] args) {
		HistoryTester ht = new HistoryTester();
		ht.commitWeekData(ht.makeRandSpots3(), ht.dates);

		//ht.appendDay(ht.makeRandSpots2(), ht.dates[0]);
		System.out.println(ht.timeOfDay.length);
	}
	 */



	/**
	 * This method modifies the history file completely, committing a whole new set of data.
	 * 
	 * @param spots An array of integers that represents the new weeks worth of data
	 * @param dates An array of GregorianCalendar objects that holds the corresponding dates for the days being committed
	 */
	public void commitWeekData(int[][][] spots, GregorianCalendar[] dates){


		this.spots = spots;

		dbFactory = DocumentBuilderFactory.newInstance();
		try{
			dBuilder = dbFactory.newDocumentBuilder();
		}catch(Exception e){}

		//make new dom object using the builder
		Document wdoc = dBuilder.newDocument();

		//create the root element which is just a lot and add it to doc
		Element rootElement = wdoc.createElement("lot");
		wdoc.appendChild(rootElement);
		Element[] day = new Element[histL];
		Element[] spotmatrix = new Element[timeIncr];

		//Fill in the document with the content we want here
		//----------------------------------------------------------------
		//loop through days
		for(int i=0;i<histL;i++){

			//create an element for days, give it a date attribute, and add it as a child of lot
			day[i] = wdoc.createElement("day");
			rootElement.appendChild(day[i]);
			day[i].setAttribute("date", dates[i].get(Calendar.YEAR) + "-" + dates[i].get(Calendar.MONTH) + "-" + dates[i].get(Calendar.DAY_OF_MONTH));

			//loop through all the times of day
			for(int j=0;j<timeIncr;j++){
				//create an element to hold the actual data, give it a time attribute, add it as a child of day
				spotmatrix[j] = wdoc.createElement("spotmatrix");
				day[i].appendChild(spotmatrix[j]);
				spotmatrix[j].setAttribute("time", timeOfDay[j]);
				spotmatrix[j].appendChild(wdoc.createTextNode(intMatToStr(spots[i][j])));
			}
		}

		// write the content into xml file
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(wdoc);
			StreamResult result = new StreamResult(new File("/home/kyle/history.xml"));

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(source, result);
			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		}catch(Exception e){}
	}



	/**
	 * This method adds a single day to the end of the history and removes the oldest day if the history is at max capacity
	 * 
	 * @param spots An array of integers that represents a days worth of data
	 * @param dates A GregorianCalendar object that holds the corresponding date for the day being committed
	 */
	public void appendDay(int[][] spots, GregorianCalendar date){

		Document doc;
		dbFactory = DocumentBuilderFactory.newInstance();

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(historyFile);



			doc.getDocumentElement().normalize();

			//Fill in the document with the content we want here------------------------------------------------
			Element rootElement = doc.getDocumentElement();

			//If the file already has the maximum number of days stored, remove the oldest one
			if(rootElement.getElementsByTagName("day").getLength() < histL){
				rootElement.removeChild(rootElement.getElementsByTagName("day").item(0));
			}

			Element day = doc.createElement("day");
			rootElement.appendChild(day);
			Element[] spotmatrix = new Element[timeOfDay.length];

			day.setAttribute("date", date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));

			//loop through all the times of day
			for(int j=0;j<timeOfDay.length;j++){
				//create an element to hold the actual data, give it a time attribute, add it as a child of day
				spotmatrix[j] = doc.createElement("spotmatrix");
				day.appendChild(spotmatrix[j]);
				spotmatrix[j].setAttribute("time", timeOfDay[j]);
				spotmatrix[j].appendChild(doc.createTextNode(intMatToStr(spots[j])));
			}
			//---------------------------------------------------------------------------------------------------

			// write the content into xml file (this part is black magic)
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("/home/kyle/history.xml"));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);


			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	/**
	 * This method retrieves the current parking history and stores it in the spots class variable
	 * 
	 */
	private void readData(){

		spots = new int[histL][timeIncr][numSpots];

		try {	
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();

			//make a new DOM object from the history document
			Document doc = dBuilder.parse(historyFile);
			doc.getDocumentElement().normalize();

			//extract the root element
			Element rootElement = doc.getDocumentElement();

			//get this list of days
			NodeList days = rootElement.getElementsByTagName("day");


			//loop through days
			for(int i=0;i<histL;i++){
				//loop through time increments in day
				for(int j=0;j<timeIncr;j++){
					//get the spot array at day i, and time j and convert from string to int matrix
					spots[i][j] = strToIntMat(days.item(i).getChildNodes().item(j).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method reads the history file and returns last weeks parking data
	 * 
	 * @return spots an array of integers that represents the previous weeks data
	 */
	public int[][][] getWeekRaw(){
		readData();	
		return spots;
	}

	/**
	 * Returns all parking data from a specified day in the past. If history is exceeded, oldest entry is used.
	 * 
	 * @param numDaysAgo number of days in the past to look
	 * @return an array of integers that represents the parking data from the specified day
	 */
	public int[][] getDaysAgoRaw(int numDaysAgo){
		readData();
		try{
			return spots[histL-1-numDaysAgo];
		}catch(ArrayIndexOutOfBoundsException aioobe){
			return spots[0];
		}
	}

	/**
	 * This method reads the history file and returns the last weeks percentage full data
	 * 
	 * @return wPercents an array of double that represents the percentage full data of the last week
	 */
	public double[][] getWeekPercents(){
		readData();

		double[][] wPercents = new double[histL][timeIncr];

		int numFull;

		for(int i = 0; i<histL; i++){
			for(int j = 0; j<timeIncr; j++){

				numFull = 0;

				for(int k = 0; k<numSpots; k++){					
					numFull += spots[i][j][k];
				}

				wPercents[i][j] = 100*(new Integer(numFull).doubleValue())/(new Integer(numSpots).doubleValue());
			}
		}

		return wPercents;
	}

	

	/**
	 * This method reads the history file and returns the specified days percentage full data
	 * 
	 * @param numDaysAgo number of days in the past to look
	 * @return wPercents an array of double that represents the percentage full data of the specified day
	 */
	public double[] getDaysAgoPercents(int numDaysAgo){
		readData();		
		double[] dPercents = new double[timeIncr];		
		int numFull;
		for(int j = 0; j<timeIncr; j++){
			numFull = 0;
			for(int k = 0; k<numSpots; k++){	
				try{
					numFull += spots[histL-1-numDaysAgo][j][k];
				}catch(ArrayIndexOutOfBoundsException aioobe){
					numFull += spots[0][j][k];
				}
			}
			dPercents[j] = 100*(new Integer(numFull).doubleValue())/(new Integer(numSpots).doubleValue());
		}
		return dPercents;
	}


	/**
	 * Converts an integer array to a String
	 * 
	 * @param mat an array of integers
	 * @return str the string representation of the integers
	 */
	public String intMatToStr(int[] mat){
		String str = "";
		for(int i=0;i<mat.length;i++){
			str = str + mat[i];
		}
		return str;
	}

	/**
	 * Converts a string to an array of integers
	 * 
	 * @param str a string made up of numerical characters
	 * @return mat an array of integers
	 */
	public int[] strToIntMat(String str){
		int[] mat = new int[numSpots];
		for(int i=0;i<str.length();i++){
			mat[i] = Integer.valueOf(str.charAt(i));
		}
		return mat;
	}

	//these methods make random data for testing--------------------------
	public int[][][] makeRandSpots3(){

		int[][][] spots = new int[histL][timeIncr][numSpots];
		Random rando = new Random();

		for(int i=0;i<histL;i++){
			for(int j=0;j<timeIncr;j++){
				for(int k=0;k<numSpots;k++){
					spots[i][j][k] = rando.nextBoolean() ? 1 : 0;
				}
			}
		}
		return spots;
	}
	public int[][] makeRandSpots2(){

		int[][] spots = new int[timeIncr][numSpots];
		Random rando = new Random();


		for(int j=0;j<timeIncr;j++){
			for(int k=0;k<numSpots;k++){
				spots[j][k] = rando.nextBoolean() ? 1 : 0;
			}
		}
		return spots;
	}
	//--------------------------------------------------------------------


}//end HistoryHandler