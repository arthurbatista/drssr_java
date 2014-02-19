package br.ufam.drssr.factorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import br.ufam.drssr.dataset.UserItemRating;
import br.ufam.drssr.socialgraph.SocialUser;
import br.ufam.drssr.util.MatrixUtil;

public class LowRankFactorization {
	
	public static void main(String[] args) {

		int[][] R = { 
				{ 5, 4, 0, 1, 3, 0 }, 
				{ 5, 0, 4, 0, 0, 1 },
				{ 0, 5, 0, 1, 1, 0 }, 
				{ 1, 0, 1, 5, 4, 0 },
				{ 0, 1, 0, 0, 5, 4 }, 
				{ 1, 0, 2, 5, 0, 0 } };
		
		Map<Long, UserItemRating> matrixUser = new HashMap<Long, UserItemRating>();
		
		for (int i = 0; i < R.length; i++) {
			
			UserItemRating u = new UserItemRating();
			
			for (int j = 0; j < R[0].length; j++) {
				
				if(R[i][j] != 0 ) {
					u.addItem(j, R[i][j]);
				}
			}
			matrixUser.put((long)i, u);
		}

		
		double[][] U = generateLatentMatrix(R.length,2);
		double[][] V = generateLatentMatrix(R.length,2);
		
//		List<double[][]> list = sgd(R,U,V);
		List<double[][]> list = sgdMemory(matrixUser,U,V);

		U = list.get(0);
		V = list.get(1);

		double[][] lrMatrix = MatrixUtil.multiply(U, V);
		
		System.out.println(MatrixUtil.rmse(lrMatrix, R));
	}

	
	public static void sgd(Map<Long, UserItemRating> matrixUser, int m, int n, Map<Long, SocialUser> socialGraph) {
		
		int[][] R = new int[m][n];
		
		for(Map.Entry<Long, UserItemRating> user : matrixUser.entrySet()) {
			
			for(Map.Entry<Integer, Integer> item : user.getValue().getItem().entrySet()) {
				R[user.getKey().intValue()-1][item.getKey()-1] = item.getValue();
			}
		}
		
		double[][] U = generateLatentMatrix(R.length,2);
		double[][] V = generateLatentMatrix(R.length,2);
		
		double[][] U1 = U.clone();
		double[][] V1 = V.clone();
		
		List<double[][]> list = sgd(R,U,V);
		List<double[][]> list1 = sgdMemory(matrixUser,U1,V1);

		U = list.get(0);
		V = list.get(1);
		
		U1 = list1.get(0);
		V1 = list1.get(1);
		
		double[][] lrMatrix = MatrixUtil.multiply(U, V);
		double[][] lrMatrix1 = MatrixUtil.multiply(U1, V1);
		
//		MatrixUtil.printArray(R[40]);
//		MatrixUtil.printArray(lrMatrix[40]);
		
		System.out.println(MatrixUtil.rmse(lrMatrix, R));
		System.out.println(MatrixUtil.rmse(lrMatrix1, R));
	}
	
	public static List<double[][]> sgdMemory(Map<Long, UserItemRating> matrixUser, double[][] U, double[][] V) {

		Random random = new Random();

		int steps = 1800000;
		double alpha = 0.0002;
		double lamb = 0.02;
		
		for (int l = 0; l < steps; l++) {

			int i = random.nextInt(U.length) + 1;
			UserItemRating user = matrixUser.get((long)i);
			
			if(user == null)
				System.out.println(i);
			
			Set<Integer> items = user.getItem().keySet();
			int j = random.nextInt(user.getItem().size());
			
			Integer key = (Integer)items.toArray()[j];
			
			Integer Rij = user.getItem().get(key);
			
			j = key - 1;
			i = i - 1;
			
			if (Rij > 0) {
				double[] Ui = U[i];
				double[] Vj = V[j];
				double Ra = Rij - MatrixUtil.dot(Ui, Vj);
				double[] u_temp = MatrixUtil.add(Ui,MatrixUtil.dot(2 * alpha,MatrixUtil.subtracts(MatrixUtil.dot(Ra, Vj), MatrixUtil.dot(lamb / U.length, Ui))));
						   V[j] = MatrixUtil.add(Vj,MatrixUtil.dot(2 * alpha,MatrixUtil.subtracts(MatrixUtil.dot(Ra, Ui), MatrixUtil.dot(lamb / V.length, Vj))));
				U[i] = u_temp;
			}
		}

		List<double[][]> list = new ArrayList<double[][]>();

		list.add(U);
		list.add(V);

		return list;
	}
	
	public static List<double[][]> sgd(int[][] R, double[][] U, double[][] V) {

		Random random = new Random();

		int steps = 1800000;
		double alpha = 0.0002;
		double lamb = 0.02;
		
		for (int l = 0; l < steps; l++) {

			int i = random.nextInt(U.length);
			int j = random.nextInt(V.length);
			if (R[i][j] > 0) {
				double[] Ui = U[i];
				double[] Vj = V[j];
				double Ra = R[i][j] - MatrixUtil.dot(Ui, Vj);
				double[] u_temp = MatrixUtil.add(Ui,MatrixUtil.dot(2 * alpha,MatrixUtil.subtracts(MatrixUtil.dot(Ra, Vj), MatrixUtil.dot(lamb / U.length, Ui))));
						   V[j] = MatrixUtil.add(Vj,MatrixUtil.dot(2 * alpha,MatrixUtil.subtracts(MatrixUtil.dot(Ra, Ui), MatrixUtil.dot(lamb / V.length, Vj))));
				U[i] = u_temp;
			}
		}

		List<double[][]> list = new ArrayList<double[][]>();

		list.add(U);
		list.add(V);

		return list;
	}
	
	
	public static List<double[][]> sgdsr(int[][] R, double[][] U, double[][] V, Map<Long, SocialUser> socialGraph) {

		Random random = new Random();

		int steps = 1800000000;
		double alpha = 0.0002;
		double lamb = 0.02;
		
		for (int l = 0; l < steps; l++) {

			int i = random.nextInt(U.length);
			int j = random.nextInt(V.length);
			if (R[i][j] > 0) {
				double[] Ui = U[i];
				double[] Vj = V[j];
				double Ra = R[i][j] - MatrixUtil.dot(Ui, Vj);
				double[] u_temp = MatrixUtil.add(Ui,MatrixUtil.dot(2 * alpha,MatrixUtil.subtracts(MatrixUtil.dot(Ra, Vj), MatrixUtil.dot(lamb / U.length, Ui))));
						   V[j] = MatrixUtil.add(Vj,MatrixUtil.dot(2 * alpha,MatrixUtil.subtracts(MatrixUtil.dot(Ra, Ui), MatrixUtil.dot(lamb / V.length, Vj))));
				U[i] = MatrixUtil.add(MatrixUtil.add(u_temp,regularizationF(new Long(i), socialGraph, Ui, U)),regularizationG(new Long(i), socialGraph, Ui, U));
			}
		}

		List<double[][]> list = new ArrayList<double[][]>();

		list.add(U);
		list.add(V);

		return list;
	}
	
	public static double[] regularizationF(Long userId, Map<Long, SocialUser> socialGraph, double[] Ui, double[][] U) {
		
		double beta = 0.001; 
		
		double[] reg = new double[Ui.length];
		
		SocialUser sUser = socialGraph.get(userId+1);
		
		for(Map.Entry<Long, Double> friendF : sUser.getFriendsF().entrySet()) {
			
			if(!friendF.getValue().isNaN())
				reg = MatrixUtil.add( reg,
									  MatrixUtil.dot( friendF.getValue(), MatrixUtil.subtracts(Ui,U[friendF.getKey().intValue()-1]) )
									);
		}
		
		return MatrixUtil.dot(beta,reg);
	}
	
	public static double[] regularizationG(Long userId, Map<Long, SocialUser> socialGraph, double[] Ui, double[][] U) {
		
		double beta = 0.001; 
		
		double[] reg = new double[Ui.length];
		
		SocialUser sUser = socialGraph.get(userId+1);
		
		for(Map.Entry<Long, Double> friendG : sUser.getFriendsG().entrySet()) {
			
			if(!friendG.getValue().isNaN())
				reg = MatrixUtil.add( reg,
									  MatrixUtil.dot( friendG.getValue(), MatrixUtil.subtracts(Ui,U[friendG.getKey().intValue()-1]) )
									);
		}
		
		return MatrixUtil.dot(beta,reg);
	}
	
	public static double[][] generateLatentMatrix(int m, int n) {
		
		Random random = new Random();
		
		double[][] matrix = new double[m][n];
		
		for (int i = 0; i < m; i++) {
			
			for (int j = 0; j < n; j++) {
				
				matrix[i][j] = random.nextDouble();
			}
		}
		
		return matrix;
	}
	
	
}
