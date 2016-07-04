package com.homework.services.resultsManager;

import java.util.HashMap;
import java.util.Map;

import com.homework.objects.WordCategory;
import com.homework.workers.ResultMapDispatcherWorker;
import com.homework.workers.WorkerFactory;

public class ResultsManagerServiceImpl implements ResultsManagerService{
	
	
	public static ResultsManagerService getInstance() {
		return new ResultsManagerServiceImpl();
	}
	
	@Override
	public void saveResults(String[] categories) {
		ResultMapDispatcherWorker dispatcher = WorkerFactory.getResultMapDispatcher();
		
		WordCategory[] cats;
		
		if (categories == null) {
			dispatcher.saveAllDictionaries();
		} else {
			cats = WordCategory.parseCategories(categories);
			
			dispatcher.saveDictionaries(cats);
		}
	}
	
	
	
	@Override
	public void distributeResultsMap(Map<String, Long> results) {
		
		if (results != null && results.size() > 0) {
			WorkerFactory.getResultMapDispatcher().addResultMap(results);
		}
		
	}

	@Override
	public Long getWordCount(String word) {
		Long count = Long.valueOf(0);
		
		WordCategory cat;
		Map<String, Long> writerBuffer;
		
		cat = WordCategory.determineCategory(word);
		
		if (cat != null) {
			writerBuffer = getWordsCount(new WordCategory[]{cat});
			if ( writerBuffer != null && writerBuffer.containsKey(word) ) { // better safe than sorry...
				count = writerBuffer.get(word);
			}
		}
		
		return count;
	}

	@Override
	public Map<String, Long> getAllWordsCounts() {
		
		return getWordsCount(WordCategory.values());
	}
	
	
	@Override
	public Map<String, Long> getWordsCount(WordCategory[] cats) {
		Map<String, Long> result = new HashMap<String, Long>();
		
		Map<String, Long> writerBuffer;
		
		if (cats != null) {
			for (WordCategory cat : cats) {
				writerBuffer = WorkerFactory.getResultMapDispatcher().getWriter(cat).getBuffer();
				if (writerBuffer != null && writerBuffer.size() > 0) {
					result.putAll(writerBuffer);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Map<String, Long> getWordsCount(String[] categories) {
		
		return getWordsCount(WordCategory.parseCategories(categories));
	}
	
	
}
