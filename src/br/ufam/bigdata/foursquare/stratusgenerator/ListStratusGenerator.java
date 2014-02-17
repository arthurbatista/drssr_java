package br.ufam.bigdata.foursquare.stratusgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListStratusGenerator {
	
	public static final String PATH_FOLDER_STRATUS = "/Users/arthurbatista/datasets/stratus/";
	
	/**
	 * Return the low size form Row x Column
	 * @param matrix
	 * @return
	 */
	public static int getMatrixSize(List<String> matrix) {
		
		int rowSize = matrix.size();
		int columnSize = matrix.get(0).split(",").length;
		
		return rowSize < columnSize ? rowSize : columnSize;
	}
	
	/**
	 * Return the block size. E.g.: If the matrix is 628x628 and you want to process
	 * 4 blocks per time, the return will be 628/4, that is equal to 157. So, every
	 * block will be 157x157
	 * @param matrixSize
	 * @return
	 */
	public static int getBlockSize(int matrixSize) {
	    
		boolean isPrime = true;
		
		int i=5;
		
		while(isPrime) {
			for(; i<matrixSize; i--) {
		        if(matrixSize%i==0) {
		        	isPrime = false;
		            break;
		        }
		    }
			matrixSize--;
		}
		
	    return matrixSize / i;
	}
	
	public static void selectStratus(List<String> matrix) {
		
		int matrixSize = getMatrixSize(matrix);
		
		int blockSize = getBlockSize(matrixSize);
		
		List<String> list = getIndexMatrix(matrixSize,blockSize);
		
		for (String st : list) {
			
			int r = new Integer(st.split(",")[0]);
			int c = new Integer(st.split(",")[1]);
			
			String stratusName = "STRATUS_"+r+"_"+c;
			
			try {
				
				File file = new File(PATH_FOLDER_STRATUS+stratusName);
	 
				if (!file.exists()) {
					file.createNewFile();
				}
	 
				FileWriter fw = new FileWriter(file);
				PrintWriter pw = new PrintWriter(fw);

				for (int i = 0; i < blockSize; i++) {
					
					String[] user = matrix.get(r).split(",");
					
					String userRating = "";
					
					for (int j = 0; j < blockSize; j++) {
						
						userRating += user[c++];
						
						if(j < blockSize-1)
							userRating += ",";
					}
//					System.out.println(userRating);
					pw.println(userRating);
					
					c = new Integer(st.split(",")[1]);
					r++;
				}
				
				pw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<String> getIndexMatrix(int matrixSize, int blockSize) {
		
		List<String> indexMatrix = new ArrayList<>();
		
		int miniMatrixSize = matrixSize / blockSize;

//		String[][] indexMatrix = new String[miniMatrixSize][miniMatrixSize];

		int row = 0;
		int col = 0;

		for (int i = 0; i < miniMatrixSize; i++) {

			for (int j = 0; j < miniMatrixSize; j++) {
				indexMatrix.add(row + "," + col);
//				indexMatrix[i][j] = row + "," + col;
				col += blockSize;
			}
			col = 0;
			row += blockSize;
		}
		
		return indexMatrix;
		
	}
	
	public static List<String> generateRamdonStratus(int matrixSize, int blockSize) {

		Random random = new Random();

		List<String> list = new ArrayList<>();

		int miniMatrixSize = matrixSize / blockSize;

		int row = 0;

		while (list.size() < miniMatrixSize) {

			int randomCol = random.nextInt(matrixSize);

			if (randomCol % blockSize != 0)
				continue;

			boolean validRandomCol = true;

			for (String st : list) {

				int sColumn = new Integer(st.split(",")[1]);

				if (sColumn == randomCol) {
					validRandomCol = false;
					break;
				}
			}

			if (validRandomCol) {
				list.add(row + "," + randomCol);
				row += blockSize;
			}
		}

		return list;
	}
}
