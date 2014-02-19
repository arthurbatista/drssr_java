package br.ufam.drssr.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MatrixGenerator {

	private static final String FILE_PATH = "/home/arthur/projects/mestrado/bigdata/foursquare/TEST_UIR";

	public static void main(String[] args) {

		Random random = new Random();

		int[][] matrix = new int[80][80];

		for (int[] user : matrix) {

			for (int i = 0; i < 30; i++) {

				int item = random.nextInt(79);

				int rating = 0;
				
				while(rating==0)
					rating = random.nextInt(6);

				user[item] = rating;
			}
		}
		
		try {
			
			File file = new File(FILE_PATH);
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					if(matrix[i][j] != 0) {
//						System.out.println(i+","+j+","+matrix[i][j]);
						int user = i+1;
						int item = j+1;
						pw.println(user+","+item+","+matrix[i][j]);
					}
				}
			}
			
//			for (int[] user : matrix) {
//				for (int item : user) {
//					pw.println(item + ",");
//				}
//			}
			
			pw.close();
			
		} catch (IOException e) {
			
		}
	}

}
