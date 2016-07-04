package com.homework.objects;

import java.io.File;
import java.util.ArrayList;

public enum WordCategory {
	A_G("^[a-gA-G].*"),
	H_N("^[h-nH-N].*"),
	O_U("^[o-uO-U].*"),
	V_Z("^[v-zV-Z].*");
	
	
	
	private WordCategory(final String regex) {
		this.regex = regex;
	}
	
	private String regex;
	
	
	
	public String getRegex() {
		return this.regex;
	}
	
	
	public boolean matches(String word) {
		boolean match = false;
		
		if (word != null) {
			match = word.matches(regex);
		}
		
		return match;
	}
	
	/**
	 * Method attempts to determine word category. If none match - null is returned
	 * @param word - word to work on
	 * @return either NULL or WordCategories value
	 */
	public static WordCategory determineCategory(String word) {
		
		for (WordCategory cat : WordCategory.values()) {
			if (cat.matches(word)) {
				return cat;
			}
		}
		
		return null;
	}
	
	
	public static WordCategory parseCategory(String categoryName) {
		WordCategory result = null;
		if (categoryName != null && !categoryName.isEmpty()) {
			categoryName = categoryName.toUpperCase(Constants.locale);
			
			for(WordCategory cat: values()) {
				if (cat.toString().equals(categoryName)) {
					result = cat;
					break;
				}
			}
		}
		
		return result;
	}
	
	public static WordCategory[] parseCategories(String[] categoryNames) {
		ArrayList<WordCategory> cats = new ArrayList<WordCategory>();
		WordCategory cat;
		
		for (String catName: categoryNames) {
			
			cat = WordCategory.parseCategory(catName);
			if (cat != null) {
				cats.add(cat);
			} else {
				System.err.println("Cannot parse WordCategory from String: "+cat);
			}
		}
		
		return cats.toArray(new WordCategory[]{});
	}
	
	public File getFile() {
		File file = new File(Constants.workdir+File.separator+name());
		
		return file;
	}
	
	
}
