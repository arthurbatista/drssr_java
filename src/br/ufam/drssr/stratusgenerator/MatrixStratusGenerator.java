package br.ufam.drssr.stratusgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixStratusGenerator {
	
	public static void main(String[] args) {

		int[][] R = { 
				{ 5, 4, 0, 1, 3, 0 }, 
				{ 5, 0, 4, 0, 0, 1 },
				{ 0, 5, 0, 1, 1, 0 }, 
				{ 1, 0, 1, 5, 4, 0 },
				{ 0, 1, 0, 0, 5, 4 }, 
				{ 1, 0, 2, 5, 0, 0 } };

		selectStratus(R, 4);
		
	}

	public static void selectStratus(int[][] R, int blockSize) {
		
		while (true) {
			if ((R[0].length % blockSize == 0))
				break;
			else
				blockSize--;
		}

		List<String> list = generateRamdonStratus(R[0].length, blockSize);

		for (String st : list) {

			int r = new Integer(st.split(",")[0]);
			int c = new Integer(st.split(",")[1]);

			for (int i = 0; i < blockSize; i++) {
				for (int j = 0; j < blockSize; j++) {
					System.out.print(R[r][c++] + ",");
				}
				c = new Integer(st.split(",")[1]);
				r++;
				System.out.println("");
			}

			System.out.println("------");

		}

	}
	
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
	
	public static List<String> generateRamdonStratus(int rowSize, int blockSize) {

		Random random = new Random();

		List<String> list = new ArrayList<>();

		int matrixSize = rowSize / blockSize;

		String[][] indexMatrix = new String[matrixSize][matrixSize];

		int row = 0;
		int col = 0;

		for (int i = 0; i < matrixSize; i++) {

			for (int j = 0; j < matrixSize; j++) {

				indexMatrix[i][j] = row + "," + col;
				col += blockSize;
			}
			col = 0;
			row += blockSize;
		}

		row = 0;

		while (list.size() < matrixSize) {

			int randomCol = random.nextInt(rowSize);

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
