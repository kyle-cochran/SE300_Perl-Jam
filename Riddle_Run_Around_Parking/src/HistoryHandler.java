package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.xml.sax.SAXException;

/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:18 AM
 */

public class HistoryHandler {
	// Some class definitions
	File historyFile;// the file
	// where the
	// parking
	// history
	// will be
	// stored
	File parkingHistoryFile; // file
	// used
	// to
	// display
	// parking
	// data
	DocumentBuilderFactory dbFactory; // A document builder builder (for making
	// a document object)
	DocumentBuilder dBuilder; // A document builder (for making a document
	// object)
	Document doc;

	TransformerFactory transformerFactory; // For writing the document object to
	// a file
	Transformer transformer; // For writing the document object to a file
	DOMSource source; // For writing document object to a file
	StreamResult result;// output to write to

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // this is how
	// the date will
	// be formatted
	// in the xml
	DateFormat timeFormat = new SimpleDateFormat("H:mm a"); // this is how the
	// time will be
	// formatted in the
	// xml

	int histL = 8; // the number of past days that will be stored at any one
	// time
	int timeIncr = 28; // the number of different time steps during the day that
	// will be considered
	int numSpots = 32; // the number of parking spots in one lot
	int[][][] spots; // the matrix that holds all spot data, format:
	// [day][time][spot no.]

	// some random dates used for testing
	GregorianCalendar[] dates = { new GregorianCalendar(2016, 3, 20),
			new GregorianCalendar(2016, 3, 21),
			new GregorianCalendar(2016, 3, 22),
			new GregorianCalendar(2016, 3, 23),
			new GregorianCalendar(2016, 3, 24),
			new GregorianCalendar(2016, 3, 25),
			new GregorianCalendar(2016, 3, 26) };

	// All the times of day
	String[] timeOfDay = { "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM",
			"9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM",
			"12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM",
			"3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM",
			"6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM",
			"9:00 PM" };

	public HistoryHandler() {

		historyFile = new File("src/media/8_day_history.xml"); 
		parkingHistoryFile = new File("/home/kyle/git/SE300_Perl-Jam/Riddle_Run_Around_Parking/src/media/Parking_History.txt");
		result = new StreamResult(historyFile);
		
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			// make the document builder, then the document
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(historyFile);

			// make the objects that do the writing
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();

			// makes the xml formatted nicely
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			// set the input and output for writing
			source = new DOMSource(doc);
			result = new StreamResult(historyFile);

		} catch (Exception e) {
		}
	}

	public void appendCurrentTime(int[] nowSpots) {

		try {
			// make a new DOM object from the history document
			doc = dBuilder.parse(historyFile);
			doc.getDocumentElement().normalize();

			// extract the root element
			Element rootElement = doc.getDocumentElement();

			// get this list of days
			NodeList days = rootElement.getElementsByTagName("day");

			// make sure that there is already a day for today
			boolean todayIsOnFile = days
					.item(days.getLength() - 1)
					.getAttributes()
					.getNamedItem("date")
					.toString()
					.equals(dateFormat.format(Calendar.getInstance().getTime()));
			if (!todayIsOnFile) {
				Element day = doc.createElement("day");
				rootElement.appendChild(day);
				day.setAttribute("date",
						dateFormat.format(Calendar.getInstance().getTime()));
				days = rootElement.getElementsByTagName("day");
			}

			// make a new element to hold the data at the current time, set it's
			// "time" attribute to be the current time, set it's text content to
			// be the data
			Element currentData = doc.createElement("spotmatrix");
			currentData.setAttribute("time",
					timeFormat.format(Calendar.getInstance().getTime()));
			currentData.setTextContent(intMatToStr(nowSpots));

			// add the current data element to the rest of the document object
			days.item(days.getLength() - 1).appendChild(currentData);

			// writes to the xml file
			transformer.transform(source, result);

		} catch (Exception e) {
		}

	}

	/**
	 * This method modifies the history file completely, committing a whole new
	 * set of data.
	 * 
	 * @param spots
	 *            An array of integers that represents the new weeks worth of
	 *            data
	 * @param dates
	 *            An array of GregorianCalendar objects that holds the
	 *            corresponding dates for the days being committed
	 */
	public void commitWeekData(int[][][] spots, GregorianCalendar[] dates) {

		this.spots = spots;

		// make new dom object using the builder
		doc = dBuilder.newDocument();

		// create the root element which is just a lot and add it to doc
		Element rootElement = doc.createElement("lot");
		doc.appendChild(rootElement);
		Element[] day = new Element[histL];
		Element[] spotmatrix = new Element[timeIncr];

		// Fill in the document with the content we want here
		// ----------------------------------------------------------------
		// loop through days
		for (int i = 0; i < histL; i++) {

			// create an element for days, give it a date attribute, and add it
			// as a child of lot
			day[i] = doc.createElement("day");
			rootElement.appendChild(day[i]);
			day[i].setAttribute(
					"date",
					dates[i].get(Calendar.YEAR) + "-"
							+ dates[i].get(Calendar.MONTH) + "-"
							+ dates[i].get(Calendar.DAY_OF_MONTH));

			// loop through all the times of day
			for (int j = 0; j < timeIncr; j++) {
				// create an element to hold the actual data, give it a time
				// attribute, add it as a child of day
				spotmatrix[j] = doc.createElement("spotmatrix");
				day[i].appendChild(spotmatrix[j]);
				spotmatrix[j].setAttribute("time", timeOfDay[j]);
				spotmatrix[j].appendChild(doc
						.createTextNode(intMatToStr(spots[i][j])));
			}
		}
		// --------------------------------------------------------------

		// write the content into xml file
		try {
			transformer.transform(source, result);
		} catch (Exception e) {
		}
	}

	/**
	 * This method adds a single day to the end of the history and removes the
	 * oldest day if the history is at max capacity
	 * 
	 * @param spots
	 *            An array of integers that represents a days worth of data
	 * @param dates
	 *            A GregorianCalendar object that holds the corresponding date
	 *            for the day being committed
	 */
	public void appendDay(int[][] spots, GregorianCalendar date) {

		try {
			doc = dBuilder.parse(historyFile);

			doc.getDocumentElement().normalize();

			// Fill in the document with the content we want
			// here------------------------------------------------
			Element rootElement = doc.getDocumentElement();

			// If the file already has the maximum number of days stored, remove
			// the oldest one
			if (rootElement.getElementsByTagName("day").getLength() < histL) {
				rootElement.removeChild(rootElement.getElementsByTagName("day")
						.item(0));
			}

			Element day = doc.createElement("day");
			rootElement.appendChild(day);
			Element[] spotmatrix = new Element[timeOfDay.length];

			day.setAttribute("date",
					date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH)
							+ "-" + date.get(Calendar.DAY_OF_MONTH));

			// loop through all the times of day
			for (int j = 0; j < timeOfDay.length; j++) {
				// create an element to hold the actual data, give it a time
				// attribute, add it as a child of day
				spotmatrix[j] = doc.createElement("spotmatrix");
				day.appendChild(spotmatrix[j]);
				spotmatrix[j].setAttribute("time", timeOfDay[j]);
				spotmatrix[j].appendChild(doc
						.createTextNode(intMatToStr(spots[j])));
			}
			// ---------------------------------------------------------------------------------------------------

			// write the DOM to the xml file
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the history file and creates a new DOM object in the doc class
	 * variable
	 */
	public void getDOM() {
		// make a new DOM object from the history document
		try {
			doc = dBuilder.parse(historyFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
	}

	/**
	 * This method retrieves the current parking history and stores it in the
	 * spots class variable
	 * 
	 */
	private void readData() {

		spots = new int[histL][timeIncr][numSpots];

		getDOM();

		try {

			// extract the root element
			Element rootElement = doc.getDocumentElement();

			// get this list of days
			NodeList days = rootElement.getElementsByTagName("day");

			// loop through days
			for (int i = 0; i < histL; i++) {
				// loop through time increments in day
				for (int j = 0; j < timeIncr; j++) {
					// get the spot array at day i, and time j and convert from
					// string to int matrix
					spots[i][j] = strToIntMat(days.item(i).getChildNodes()
							.item(j).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method reads the history file and returns last weeks parking data
	 * 
	 * @return spots an array of integers that represents the previous weeks
	 *         data
	 */
	public int[][][] getWeekRaw() {
		readData();
		return spots;
	}

	/**
	 * Returns all parking data from a specified day in the past. If history is
	 * exceeded, oldest entry is used.
	 * 
	 * @param numDaysAgo
	 *            number of days in the past to look
	 * @return an array of integers that represents the parking data from the
	 *         specified day
	 */
	public int[][] getDaysAgoRaw(int numDaysAgo) {
		readData();
		try {
			return spots[histL - 1 - numDaysAgo];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return spots[0];
		}
	}

	/**
	 * This method reads the history file and returns the last weeks percentage
	 * full data
	 * 
	 * @return wPercents an array of double that represents the percentage full
	 *         data of the last week
	 */
	public double[][] getAllPercents() {
		readData();

		double[][] wPercents = new double[histL][timeIncr];

		int numFull;

		for (int i = 0; i < histL; i++) {
			for (int j = 0; j < timeIncr; j++) {

				numFull = 0;

				for (int k = 0; k < numSpots; k++) {
					numFull += spots[i][j][k];
				}

				wPercents[i][j] = 100 * (new Integer(numFull).doubleValue())
						/ (new Integer(numSpots).doubleValue());
			}
		}

		return wPercents;
	}

	/**
	 * Prints the data to a human-readable plaintext file
	 * 
	 * @param parkingHistoryFile
	 * @throws FileNotFoundException
	 */
	public void saveAsPlainText(String parkingHistoryFile)
			throws FileNotFoundException {

		getDOM(); // make sure the history Document Object Model is up to date
		NodeList days = doc.getDocumentElement().getElementsByTagName("day");// a
																				// list
																				// of
																				// the
																				// day
																				// DOM
																				// objects
		double[][] percents = getAllPercents(); // get the percent full data for
												// the saved history
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(parkingHistoryFile));
			writer.write("Date:\tTime:\t%Full:\n");
			for (int i = 0; i < histL; i++) {
				for (int j = 0; j < timeIncr; j++) {
					writer.write(days.item(i).getAttributes()
							.getNamedItem("date")
							+ "\t"
							+ days.item(i).getChildNodes().item(j)
									.getAttributes().getNamedItem("time")
							+ "\t" + String.valueOf(percents[i][j]) + "\n");
				}
			}
			writer.write("\n\nPerl Jam Software LLC. sincerely thanks you for your support.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method reads the history file and returns the specified days
	 * percentage full data
	 * 
	 * @param numDaysAgo
	 *            number of days in the past to look
	 * @return wPercents an array of double that represents the percentage full
	 *         data of the specified day
	 */
	public double[] getDaysAgoPercents(int numDaysAgo) {
		readData();
		double[] dPercents = new double[timeIncr];
		int numFull;
		for (int j = 0; j < timeIncr; j++) {
			numFull = 0;
			for (int k = 0; k < numSpots; k++) {
				try {
					numFull += spots[histL - 1 - numDaysAgo][j][k];
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					numFull += spots[0][j][k];
				}
			}
			dPercents[j] = 100 * (new Integer(numFull).doubleValue())
					/ (new Integer(numSpots).doubleValue());
		}
		return dPercents;
	}

	/**
	 * Converts an integer array to a String
	 * 
	 * @param mat
	 *            an array of integers
	 * @return str the string representation of the integers
	 */
	public String intMatToStr(int[] mat) {
		String str = "";
		for (int i = 0; i < mat.length; i++) {
			str = str + mat[i];
		}
		return str;
	}

	/**
	 * Converts a string to an array of integers
	 * 
	 * @param str
	 *            a string made up of numerical characters
	 * @return mat an array of integers
	 */
	public int[] strToIntMat(String str) {
		int[] mat = new int[numSpots];
		for (int i = 0; i < str.length(); i++) {
			mat[i] = Integer.valueOf(str.charAt(i));
		}
		return mat;
	}

	// these methods make random data for testing--------------------------
	public int[][][] makeRandSpots3() {

		int[][][] spots = new int[histL][timeIncr][numSpots];
		Random rando = new Random();

		for (int i = 0; i < histL; i++) {
			for (int j = 0; j < timeIncr; j++) {
				for (int k = 0; k < numSpots; k++) {
					spots[i][j][k] = rando.nextBoolean() ? 1 : 0;
				}
			}
		}
		return spots;
	}

	public int[][] makeRandSpots2() {

		int[][] spots = new int[timeIncr][numSpots];
		Random rando = new Random();

		for (int j = 0; j < timeIncr; j++) {
			for (int k = 0; k < numSpots; k++) {
				spots[j][k] = rando.nextBoolean() ? 1 : 0;
			}
		}
		return spots;
	}
	// --------------------------------------------------------------------

}// end HistoryHandler