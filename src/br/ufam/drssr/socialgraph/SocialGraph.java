package br.ufam.drssr.socialgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import br.ufam.drssr.dataset.UserItemRating;
import br.ufam.drssr.factorization.LowRankFactorization;

public class SocialGraph {

	public static final String PATH_FILE_SOCIAL_GRAPH = "/home/arthur/projects/mestrado/bigdata/foursquare/TEST_GRAPH";
	public static final String PATH_FILE_SPLITED_SOCIAL_GRAPH = "/home/arthur/projects/mestrado/bigdata/foursquare/TEST_SG";
	public static final String PATH_FILE_STATE = "/home/arthur/projects/mestrado/bigdata/foursquare/TEST_UIR";

	Map<Long, UserItemRating> matrixUser;
	Set<Long> users;
	Map<Long,Integer> items;
	Map<Long, SocialUser> socialGraph;
	PearsonsCorrelation pearson = new PearsonsCorrelation();
	
	public static void main(String[] args) {

		SocialGraph sg = new SocialGraph();
		sg.generateSolcialGraphPerState();
		
	}

	public void generateSolcialGraphPerState() {
		
		System.out.println("Load users");
		this.loadUser();
		
		System.out.println("Load items");
		this.loadItem();
		
		System.err.println("Load Matrix");
		this.loadMatrix();
		
		System.out.println("Load Social Graph");
		this.loadSocialGraph();
		
		LowRankFactorization.sgd(matrixUser, 80, 80, socialGraph);
//		
//		System.out.println("Write to file");
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			objectMapper.writeValue(new File(PATH_FILE_SPLITED_SOCIAL_GRAPH), socialGraph);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			File file = new File(PATH_FILE_SPLITED_SOCIAL_GRAPH);
// 
//			if (!file.exists()) {
//				file.createNewFile();
//			}
// 
//			PrintWriter pw = new PrintWriter(new FileWriter(file));
//			
//			for(Long userId : users) {
//				
//				SocialUser user = socialGraph.get(userId);
//				
//				if(user != null) {
//				
//					String line = userId.toString() + ":";
//					
//					for(Map.Entry<Long, Double> friendF : user.getFriendsF().entrySet()) {
//						line += friendF.getKey()+",";
//					}
//					
//					line += ":";
//					
//					for(Map.Entry<Long, Double> friendG : user.getFriendsG().entrySet()) {
//						line += friendG.getKey()+",";
//					}
//					
//					pw.println(line);
//				}
//			}
//			
//			pw.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("Done !");
	}

	/**
	 * Load the user for the specified state
	 * @return A set of users
	 */
	public Set<Long> loadUser() {

		this.users = new HashSet<Long>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				PATH_FILE_STATE))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				String[] line = sCurrentLine.split(",");

				Long userId = new Long(line[0]);

				this.users.add(userId);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return this.users;
	}
	
	public Map<Long, Integer> loadItem() {

		this.items = new HashMap<Long, Integer>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				PATH_FILE_STATE))) {

			String sCurrentLine;

			int i = 0;

			while ((sCurrentLine = br.readLine()) != null) {

				String[] line = sCurrentLine.split(",");

				Long itemId = new Long(line[1]);

				if (this.items.get(itemId) == null)
					this.items.put(itemId, i++);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this.items;
	}
	
	public Map<Long, UserItemRating> loadMatrix() {

		this.matrixUser = new HashMap<Long, UserItemRating>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				PATH_FILE_STATE))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				String[] line = sCurrentLine.split(",");

				Long userId = new Long(line[0]);
				Long itemId = new Long(line[1]);
				Integer rating = new Integer(line[2]);

				UserItemRating user = this.matrixUser.get(userId);

				if (user == null) {
					user = new UserItemRating();
					user.addItem(itemId.intValue(), rating);
					this.matrixUser.put(userId, user);
				} else {
					user.addItem(itemId.intValue(), rating);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this.matrixUser;
	}
	
	public double calculatePearsonSim(Long u1, Long u2) {
		
		UserItemRating ur1 = matrixUser.get(u1);
		UserItemRating ur2 = matrixUser.get(u2);
		
		List<Integer> scores1 = new ArrayList<Integer>();
		List<Integer> scores2 = new ArrayList<Integer>();
		
		for (Map.Entry<Integer, Integer> entry : ur1.getItem().entrySet()) {
			
			Integer score = ur2.getItem().get(entry.getKey());
			
			if(score != null) {
				scores1.add(entry.getValue());
				scores2.add(score);
			}
		}
		
		if(scores1.size() < 2 )
			return 0;
		
		return pearson.correlation(castToArray(scores1), castToArray(scores2));
	}
	
	public double[] castToArray(List<Integer> l) {
		
		double[] d = new double[l.size()];
		
		int i = 0;
		for(Integer it : l) {
			d[i++] = it;
		}
		
		return d;
	}

	/**
	 * This method generates the social graph for the specified users
	 * @param targetUsers
	 * @return A SocialUser with firendsF and friendsG
	 */
	public Map<Long, SocialUser> loadSocialGraph() {

		this.socialGraph = new HashMap<Long, SocialUser>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				PATH_FILE_SOCIAL_GRAPH))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

//				String[] users = sCurrentLine.split("\\|");
				String[] users = sCurrentLine.split(",");

				Long u0 = new Long(users[0].trim());
				Long u1 = new Long(users[1].trim());

				if (this.users.contains(u0) && this.users.contains(u1)) {

					SocialUser socialUser = this.socialGraph.get(u0);

					if (socialUser == null) {
						socialUser = new SocialUser();
						double sim = calculatePearsonSim(u0, u1);
						socialUser.addFrindF(u1, sim);
						this.socialGraph.put(u0, socialUser);
					} else {
						double sim = calculatePearsonSim(u0, u1);
						socialUser.addFrindF(u1, sim);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Map.Entry<Long, SocialUser> entry : this.socialGraph.entrySet()) {
			
			Long userId = entry.getKey();

			SocialUser user = entry.getValue();

			for (Map.Entry<Long, Double> friendF : user.getFriendsF()
					.entrySet()) {

				SocialUser fUser = this.socialGraph.get(friendF.getKey());

				for (Map.Entry<Long, Double> friendG : fUser.getFriendsF()
						.entrySet()) {
					
					if (!userId.equals(friendG.getKey())
							&& user.getFriendsF().get(friendG.getKey()) == null) {
						
						double sim = calculatePearsonSim(userId,friendG.getKey());
						
						user.addFrindG(friendG.getKey(), sim);
					}
				}
			}
		}

		return this.socialGraph;
	}
}
