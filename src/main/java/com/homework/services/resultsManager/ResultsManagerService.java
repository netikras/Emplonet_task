package com.homework.services.resultsManager;

import java.util.Map;
import com.homework.objects.WordCategory;

public interface ResultsManagerService {
	
	public void saveResults(String[] categories);
	
	public void distributeResultsMap(Map<String, Long> results);
	
	public Long getWordCount(String word);
	
	public Map<String, Long> getAllWordsCounts();
	
	public Map<String, Long> getWordsCount(String[] cats);
	
	public Map<String, Long> getWordsCount(WordCategory[] categories);
	
	
}
