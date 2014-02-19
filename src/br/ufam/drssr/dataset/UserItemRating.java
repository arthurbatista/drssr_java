package br.ufam.drssr.dataset;

import java.util.HashMap;
import java.util.Map;

public class UserItemRating {
	
	private int userIndex;
	
	private Map<Integer,Integer> item;
	
	public UserItemRating() {
		item = new HashMap<Integer, Integer>();
	}

	public int getUserIndex() {
		return userIndex;
	}

	public void setUserIndex(int userIndex) {
		this.userIndex = userIndex;
	}

	public Map<Integer, Integer> getItem() {
		return item;
	}

	public void setItem(Map<Integer, Integer> item) {
		this.item = item;
	}
	
	public void addItem(int itemIndex, int rating){
		item.put(itemIndex, rating);
	}
	
	@Override
	public String toString() {
		
		String str = "" + userIndex;
		
		for (Map.Entry<Integer, Integer> entry : item.entrySet()) {
			str += "<" + entry.getKey() + ","+ entry.getValue()+">";
		}
		
		return str;

	}
}
