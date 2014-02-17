package br.ufam.bigdata.foursquare.dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SocialGraph {
	
	private Map<Integer,SocialUser> socialGraph;

	public static final String PATH_FILE_SOCIAL_GRAPH = "/home/arthur/projects/mestrado/ml/data_process/NY";

	public void generateSocialGraph(Map<Integer, UserItemRating> matrixUser,
			Map<Long, Integer> mapUser) {
		
		this.linkFriendsF(matrixUser, mapUser);
		this.linkFriendsG();
	}
	
	private void linkFriendsF(Map<Integer, UserItemRating> matrixUser,
			Map<Long, Integer> mapUser) {
		
		this.socialGraph = new HashMap<Integer, SocialUser>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				PATH_FILE_SOCIAL_GRAPH))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				String[] line = sCurrentLine.split(",");

				Long userIdA = new Long(line[0]);
				Long userIdB = new Long(line[1]);
				
				Integer indexA = mapUser.get(userIdA);
				Integer indexB = mapUser.get(userIdB);

				if (indexA != null && indexB != null) {
					
					SocialUser userA = this.socialGraph.get(indexA);
					SocialUser userB = this.socialGraph.get(indexB);
					
					if(userA != null) {
						userA = new SocialUser();
						userA.setIndex(indexA);
					}
					
					if(userB != null) {
						userB = new SocialUser();
						userB.setIndex(indexB);
					}
					
					userA.addFrindF(indexB, 0D);
					userB.addFrindF(indexA, 0D);
					
					socialGraph.put(indexA, userA);
					socialGraph.put(indexB, userB);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void linkFriendsG() {
		
		for (Map.Entry<Integer, SocialUser> entry : socialGraph.entrySet()) {
			
			SocialUser sUser = entry.getValue();
			
			for (Map.Entry<Integer, Double> friendF : sUser.getFriendsF().entrySet()) {
				
				SocialUser socialUserF = socialGraph.get(friendF.getKey());
				
				for (Map.Entry<Integer, Double> friendG : socialUserF.getFriendsF().entrySet()) {
					
					if(!friendG.getKey().equals(entry.getKey())) {
						
						Double sim = sUser.getFriendsF().get(friendG.getKey());
					
						if(sim == null) {
							sUser.addFrindG(friendG.getKey(), 0D);
						}
					}
					
				}
				
			}
			
		}
	}
}
