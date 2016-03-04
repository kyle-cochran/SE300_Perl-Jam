import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.indexer.IntIndexer;

//import opencv.core.Mat;

/**
 * @author Austin
 * @version 1.0
 * @created 3-March-2016 1:00:00 PM
 * 
 * The method in this class will take a black and white mat image and create an integer
 * array of ones and zeros. 1 being white and 0 being black.
 */

public class MatToBinary {
	
	public int[][] toBinaryArray(Mat mat){ 
	
		System.loadLibrary("opencv_java2411");//has to be first
		
		//added indexer
		IntIndexer index = mat.createIndexer();
		
		//1440X1080
		int width = (int)mat.size().width();
		int height = (int)mat.size().height();
		int[][] binaryArray = new int[width][height];
		
		
		for(int x = 0; x <= width-1; x++){
			for(int y = 0; y <= height-1; y++){
				//old way:	if (mat.get(y, x)[0] > 100){
				if (index.get(y, x) > 100){
				
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
