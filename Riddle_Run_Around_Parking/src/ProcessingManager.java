import java.util.Vector;
import java.io.*;



/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:22 AM
 */
public class ProcessingManager {

	private int[] currentSpots;
	public int refreshFreq;
	ImageProcessor imP;


	public ProcessingManager(){
		refreshFreq = 1; //indicates that analysis should refresh one per second
		
	}


	public boolean[] getLotHistory(){
		return null;
	}

	public void setLotHistory(boolean[] array){
		
	}

	public boolean[] getLotState(){
		return null;
	}

	public DataInputStream getLotVideoFeed(){
		return null;
	}

	public void updateStates(){
		imP.process();
	}
}//end ProcessigManager