package br.ufam.bigdata.foursquare.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufam.bigdata.foursquare.stratusgenerator.ListStratusGenerator;

public class MatrixGenerator {
	
	private Map<Integer,UserItemRating> matrixUser;
	private Map<Long,Integer> mapUser;
	private Map<Long,Integer> mapItem;
	
	public static final String PATH_FILE_TEST = "/Users/arthurbatista/datasets/AK";
	public static final String PATH_FILE_MATRIX = "/Users/arthurbatista/datasets/AK_MATRIX";
	
	public static void main(String[] args) {
		
		MatrixGenerator mGenerator = new MatrixGenerator();
		
		mGenerator.processUser();
		mGenerator.processItem();
		mGenerator.processMatrix();
		mGenerator.writeMatrixtoFile();
	}
	
	public void processUser() {
		
		mapUser = new HashMap<Long, Integer>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE_TEST))) {
 
			String sCurrentLine;
			
			int i = 0;
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				String[] line = sCurrentLine.split(",");
				
				Long userId = new Long(line[0]);

				if(mapUser.get(userId) == null)
					mapUser.put(userId, i++);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void processItem() {
		
		mapItem = new HashMap<Long, Integer>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE_TEST))) {
 
			String sCurrentLine;
			
			int i = 0;
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				String[] line = sCurrentLine.split(",");
				
				Long itemId = new Long(line[1]);

				if(mapItem.get(itemId) == null)
					mapItem.put(itemId, i++);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processMatrix() {
		
		matrixUser = new HashMap<Integer, UserItemRating>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE_TEST))) {
 
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				String[] line = sCurrentLine.split(",");
				
				Integer userIndex = mapUser.get(new Long(line[0]));
				Integer itemIndex = mapItem.get(new Long(line[1]));
				Integer rating    = new Integer(line[2]);
				
				UserItemRating user = matrixUser.get(userIndex);
				
				if(user == null) {
					user = new UserItemRating();
					user.setUserIndex(userIndex);
					user.addItem(itemIndex, rating);
					matrixUser.put(userIndex, user);
				} else {
					user.addItem(itemIndex, rating);
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeMatrixtoFile() {
		
		List<String> matrix = new ArrayList<>();
		
		try {
			File file = new File(PATH_FILE_MATRIX);
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			
			for (int i = 0; i < mapUser.size(); i++) {
				
				UserItemRating user = matrixUser.get(i);
				
				String line = "";
				
				for (int j = 0; j < mapItem.size(); j++) {
					
					Integer rating = user.getItem().get(j);
					
					if(rating == null)
						line += "0";
					else
						line += rating;
					
					if(j < mapItem.size() - 1)
						line += ",";
				}
				pw.println(line);
				matrix.add(line);
			}
			
			pw.close();

			System.out.println(matrix.size());
			System.out.println(matrix.get(0).split(",").length);
			System.out.println("------");
			ListStratusGenerator.selectStratus(matrix);
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
