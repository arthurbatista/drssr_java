package br.ufam.drssr.util;

public class MatrixUtil {
	
	public static double dot(double[] a, double[] b) {

		double sum = 0;

		for (int i = 0; i < a.length; i++) {
			sum += a[i] * b[i];
		}

		return sum;
	}

	public static double[] dot(double number, double[] array) {

		double[] result = new double[array.length];

		for (int i = 0; i < array.length; i++) {
			result[i] = array[i] * number;
		}

		return result;
	}

	public static double[] subtracts(double[] a, double[] b) {

		double[] array = new double[a.length];

		for (int i = 0; i < a.length; i++) {
			array[i] = a[i] - b[i];
		}

		return array;
	}
	
	public static double[][] subtracts(double[][] a, double[][] b) {

		double[][] array = new double[a.length][a[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				array[i][j] = a[i][j] - b[i][j];
			}
		}

		return array;
	}

	public static double[] add(double[] a, double[] b) {

		double[] array = new double[a.length];

		for (int i = 0; i < a.length; i++) {
			array[i] = a[i] + b[i];
		}

		return array;
	}

	public static double[][] multiply(double[][] a, double[][] b) {

		double[][] bT = transposeMatrix(b);

		int aRows = a.length;
		int aColumns = a[0].length;
		int bRows = bT.length;
		int bColumns = bT[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns
					+ " did not match B:Columns " + bRows + ".");
		}
		double[][] C = new double[aRows][bColumns];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				C[i][j] = 0.00000;
			}
		}
		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					C[i][j] += a[i][k] * bT[k][j];
				}
			}
		}
		return C;
	}

	public static double[][] transposeMatrix(double[][] matrix) {

		double[][] result = new double[matrix[0].length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {

			for (int j = 0; j < matrix[0].length; j++) {

				result[j][i] = matrix[i][j];
			}

		}

		return result;
	}
	
	public static void printMatrix(double[][] R) {

		for (int i = 0; i < R.length; i++) {
			for (int j = 0; j < R[0].length; j++) {
				System.out.print(R[i][j]+",");
			}
			System.out.println("");
		}
	}
	
	public static void printArray(double[] R) {

		for (int i = 0; i < R.length; i++) {
			System.out.print(Math.round(R[i])+",");
		}
		
		System.out.println("");
	}
	
	public static void printArray(int[] R) {

		for (int i = 0; i < R.length; i++) {
			System.out.print(R[i]+",");
		}
		
		System.out.println("");
	}
	
	public static double rmse(double[][] predictions, int[][] R){
		double rmse = 0;
		
		for (int i = 0; i < R.length; i++) {
			for (int j = 0; j < R.length; j++) {
				if (R[i][j] > 0)
					rmse += Math.sqrt( Math.pow(predictions[i][j] - R[i][j],2));
			}
		}
	    return rmse;
	}    
}
