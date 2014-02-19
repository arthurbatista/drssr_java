package br.ufam.drssr.socialgraph;

import java.util.HashMap;
import java.util.Map;

public class SocialUser {
	
	private Integer index;
	
	private Map<Long,Double> friendsF;
	
	private Map<Long,Double> friendsG;
	
	public SocialUser(){
		this.friendsF = new HashMap<Long, Double>();
		this.friendsG = new HashMap<Long, Double>();
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Map<Long, Double> getFriendsF() {
		return friendsF;
	}

	public void setFriendsF(Map<Long, Double> friendsF) {
		this.friendsF = friendsF;
	}

	public Map<Long, Double> getFriendsG() {
		return friendsG;
	}

	public void setFriendsG(Map<Long, Double> friendsG) {
		this.friendsG = friendsG;
	}

	public void addFrindF(Long index, Double sim){
		this.friendsF.put(index, sim);
	}
	
	public void addFrindG(Long index, Double sim){
		this.friendsG.put(index, sim);
	}
	
	public double simF(Long f) {
		return friendsF.get(f);
	}
	
	public double simG(Long g) {
		return friendsG.get(g);
	}
}
