import java.io.*;
import java.util.GregorianCalendar;


/**
 * @author Kyle
 * @version 1.0
 * @created 18-Feb-2016 11:36:18 AM
 */
public class HistoryHandler {

	private File historyFile;
	private GregorianCalendar dateKeeper;
	private BufferedWriter writer;
	private BufferedReader reader;
	
	
	
	public HistoryHandler(){
		historyFile = new File("src/7_day_history.txt");
		dateKeeper = new GregorianCalendar();
	}

	@SuppressWarnings("deprecation")
	public void commitData(boolean[] spotStates){
		
		String currLine;
		
		dateKeeper.getTime().getDate();
		dateKeeper.getTime().getHours();
		dateKeeper.getTime().getMinutes();
		
		try{
        
		writer = new BufferedWriter(new FileWriter(historyFile)); 
        reader = new BufferedReader(new FileReader(historyFile));
        
        reader.mark(300);
        
        while(!(currLine=reader.readLine()).equals(null)){
        
        	if(currLine.charAt(0)!='#' && currLine.charAt(0)!=' '){
        		
        	}
        
        }
        
        
		} catch (Exception e) {
	            e.printStackTrace();
	            
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	                reader.close();
	            } catch (Exception e) {
	            }
	        }
        
        
        
	}

	public boolean[] retrieveData(boolean[] array){
		return null;
	}

	public boolean[][] retrieveData(boolean[][] array){
		return null;
	}

	public void setHistoryFile(){

	}
}//end HistoryHandler