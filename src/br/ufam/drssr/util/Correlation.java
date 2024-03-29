package br.ufam.drssr.util;

import java.util.List;

public class Correlation {

	public static double getPearsonCorrelation(List<Integer> scores1,
			List<Integer> scores2) {
		
		double result = 0;
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = scores1.get(0);
		double mean_y = scores2.get(0);
		for (int i = 2; i < scores1.size() + 1; i += 1) {
			double sweep = Double.valueOf(i - 1) / i;
			double delta_x = scores1.get(i - 1) - mean_x;
			double delta_y = scores2.get(i - 1) - mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x / scores1.size());
		double pop_sd_y = (double) Math.sqrt(sum_sq_y / scores1.size());
		double cov_x_y = sum_coproduct / scores1.size();
		result = cov_x_y / (pop_sd_x * pop_sd_y);
		
		if(result>0)
			System.out.println(result);
		
		return (result+1)/2;
	}

}
