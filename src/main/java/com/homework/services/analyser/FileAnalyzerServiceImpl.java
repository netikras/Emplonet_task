package com.homework.services.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

import com.homework.exceptions.FileProcessingException;
import com.homework.objects.Constants;
import com.homework.services.resultsManager.ResultsManagerService;
import com.homework.services.resultsManager.ResultsManagerServiceImpl;

public class FileAnalyzerServiceImpl implements FileAnalyserService {

	
	Map<String, Long> resultMap = new HashMap<String, Long>();
	
	public static FileAnalyserService getNewAnalyser() {
		return new FileAnalyzerServiceImpl();
	}

	@Override
	public Map<String, Long> getResultsMap() {
		return resultMap;
	}

	@Override
	public Map<String, Long> process(File file) throws FileProcessingException {
		Map<String, Long> resutlMap = new HashMap<String, Long>();
		if (file.exists() && file.canRead()) {
			try {
				resutlMap = process(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new FileProcessingException(e.getLocalizedMessage());
			}
		}
		
		return resutlMap;
	}

	@Override
	public Map<String, Long> process(InputStream inputStream) throws FileProcessingException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		BreakIterator breakIterator = BreakIterator.getWordInstance(Constants.locale);
		String line;
		String word;
		
		int firstIndex = 0;
		int lastIndex  = 0;
		
		try {
			while ((line = bufferedReader.readLine()) != null) {
				breakIterator.setText(line);
				
				lastIndex = breakIterator.first();
				
				while (lastIndex != BreakIterator.DONE) {
					firstIndex = lastIndex;
					lastIndex = breakIterator.next();
					
					if (lastIndex != BreakIterator.DONE
							&& Character.isLetter(line.charAt(firstIndex))) {
						word = line.substring(firstIndex, lastIndex);
						System.out.println("Adding a result: "+word);
						addResult(word);
					}
				}
				
			}
			
			ResultsManagerService resultsManagerService = ResultsManagerServiceImpl.getInstance();
			resultsManagerService.distributeResultsMap(getResultsMap());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getResultsMap();
		
	}
	
	
	
	private Long addResult(String word) {
		
		Long count = getResultsMap().get(word);
		
		if (count != null) {
			getResultsMap().put(word, Long.valueOf(count.longValue()+1));
		} else {
			getResultsMap().put(word, Long.valueOf(1));
		}
		
		return getResultsMap().get(word);
	}
	

}
