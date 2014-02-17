package br.ufam.bigdata.foursquare.dataset;

import java.util.HashMap;
import java.util.Map;

public class SocialUser {
	
	private Integer index;
	
	private Map<Integer,Double> friendsF;
	
	private Map<Integer,Double> friendsG;
	
	public SocialUser(){
		this.friendsF = new HashMap<Integer, Double>();
		this.friendsG = new HashMap<Integer, Double>();
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Map<Integer, Double> getFriendsF() {
		return friendsF;
	}

	public void setFriendsF(Map<Integer, Double> friendsF) {
		this.friendsF = friendsF;
	}

	public Map<Integer, Double> getFriendsG() {
		return friendsG;
	}

	public void setFriendsG(Map<Integer, Double> friendsG) {
		this.friendsG = friendsG;
	}

	public void addFrindF(Integer index, Double sim){
		this.friendsF.put(index, sim);
	}
	
	public void addFrindG(Integer index, Double sim){
		this.friendsG.put(index, sim);
	}
}
