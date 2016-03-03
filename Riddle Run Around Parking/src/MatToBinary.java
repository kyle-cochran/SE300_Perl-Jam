import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

/**
 * @author Austin
 * @version 1.0
 * @created 3-March-2016 1:00:00 PM
 * 
 * The method in this class will take a black and white mat image and create an integer
 * array of ones and zeros. 1 being white and 0 being black.
 */

public class MatToBinary {
	
//	public int[][] toBinaryArray(Mat mat){ //TODO use this when your ready to pass it a mat
	public int[][] toBinaryArray(){
		
		System.loadLibrary("opencv_java2411");//has to be first
		Mat mat = Highgui.imread("diff.JPG");//TODO get rid of this when your ready to pass it a mat
		
		//1440X1080
		int width = (int)mat.size().width;
		int height = (int)mat.size().height;
		int[][] binaryArray = new int[width][height];
		
		for(int x = 0; x <= width-1; x++){
			for(int y = 0; y <= height-1; y++){
				if (mat.get(y, x)[0] > 100){
					//white pixels
					binaryArray[x][y] = 1;
				}
				else{
					//black pixels
					binaryArray[x][y] = 0;
				}
//				System.out.println(binaryArray[x][y]);
			}
		}
		return binaryArray;
	}
}
