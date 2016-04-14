package src;

import org.bytedeco.javacpp.indexer.UByteBufferIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;

//import opencv.core.Mat;

/**
 * Converter: org.bytedeco.javacpp.opencv_core.Mat -{@literal >} int[][]
 * binaryArray
 * 
 * @author Austin Musser
 * @version 1.0
 */

public class MatToBinary {

	/**
	 * Take a black and white mat image and create an integer array of ones and
	 * zeros. 1 being white and 0 being black.
	 * 
	 * @param mat
	 *            a mat image type
	 * @return binaryArray an array of integers that represents a two-tone
	 *         greyscale image.
	 */
	public int[][] toBinaryArray(Mat mat) {

		// indexer to access Mat image pixels
		UByteBufferIndexer index = mat.createIndexer();

		// match array dimensions to input image: 1440X1080
		int width = (int) mat.size().width();
		int height = (int) mat.size().height();
		int[][] binaryArray = new int[width][height];

		// cycle through pixels and copy value to matrix
		for (int x = 0; x <= width - 1; x++) {
			for (int y = 0; y <= height - 1; y++) {

				if (index.get(y, x) > 100) {

					// white pixels
					binaryArray[x][y] = 1;
				} else {
					// black pixels
					binaryArray[x][y] = 0;
				}
			}
		}
		return binaryArray;
	}
}
