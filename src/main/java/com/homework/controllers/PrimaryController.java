package com.homework.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.homework.exceptions.EmptyFileException;
import com.homework.exceptions.FileOpeningException;
import com.homework.exceptions.FileProcessingException;
import com.homework.objects.WordCategory;
import com.homework.services.analyser.FileAnalyserService;
import com.homework.services.analyser.FileAnalyzerServiceImpl;
import com.homework.services.resultsManager.ResultsManagerService;
import com.homework.services.resultsManager.ResultsManagerServiceImpl;



@RestController
public class PrimaryController {
	
	public PrimaryController() {
		System.out.println("INIT CONTROLLER");
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST, produces="application/json")
	@ResponseStatus(code=HttpStatus.OK)
	public Map<String, Long> uploadFile(
			@RequestPart(name="file", required=true) List<MultipartFile> files,
			HttpServletRequest request,
			HttpServletResponse response
			) throws EmptyFileException, FileOpeningException, FileProcessingException{
		
		
		System.out.println("Uploading files: "+files);
		
		Map<String, Long> resultsMap = new HashMap<String, Long>();
		
		for(MultipartFile file: files) {
			System.out.println("File: "+file.getName());
			if ( ! file.isEmpty()) {
				FileAnalyserService fileAnalyser = FileAnalyzerServiceImpl.getNewAnalyser();
				
				try {
					resultsMap = fileAnalyser.process(file.getInputStream());
				} catch (IOException e) {
					throw new FileOpeningException(file.getName());
				}
				
			} else {
				throw new EmptyFileException(file.getName());
			}
		}
		
		return resultsMap;
	}
	
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.CREATED, reason="Dictionaries have been saved")
	public void saveDicts(
			@RequestParam(name="category", required=false) String[] categories,
			HttpServletRequest request
			) {
		System.out.println("Flushing writers' buffers");
		ResultsManagerService resultsManagerService = ResultsManagerServiceImpl.getInstance();
		
		resultsManagerService.saveResults(categories);
		
		
	}
	
	
	@RequestMapping(value="/results", method=RequestMethod.GET, produces="application/json")
	@ResponseStatus(code=HttpStatus.OK)
	public Map<String, Long> getResultSet(
			HttpServletRequest request
			) {
		
		System.out.println("Getting results");
		
		Map<String, Long> resultMap = new HashMap<String, Long>();
		
		ResultsManagerService resultsManagerService = ResultsManagerServiceImpl.getInstance();
		
		resultMap = resultsManagerService.getAllWordsCounts();
		
		return resultMap;
	}
	
	
	
	
	@RequestMapping(value="/results/word/{word}", method=RequestMethod.GET, produces="application/json")
	@ResponseStatus(code=HttpStatus.OK)
	public Map<String, Long> getResultsCountWord(
			@PathVariable(value="word") String word,
			HttpServletRequest request
			) {
		System.out.println("getting results for word: "+word);
		
		Map<String, Long> resultMap = new HashMap<String, Long>();
		Long count = Long.valueOf(0);
		ResultsManagerService resultsManagerService = ResultsManagerServiceImpl.getInstance();
		
		count = resultsManagerService.getWordCount(word);
		
		resultMap.put(word, count);
		
		return resultMap;
	}
	
	
	@RequestMapping(value="/results/category/{cat}", method=RequestMethod.GET, produces="application/json")
	@ResponseStatus(code=HttpStatus.OK)
	public Map<String, Long> getResultSetByCategory(
			@PathVariable(value="cat") String cat,
			HttpServletRequest request
			) {
		System.out.println("getting results for category: "+cat);
		
		Map<String, Long> resultMap;
		ResultsManagerService resultsManagerService = ResultsManagerServiceImpl.getInstance();
		
		
		resultMap = resultsManagerService.getWordsCount(new String[]{cat});
		
		return resultMap;
	}
	
	
	@RequestMapping(value="/categories", method=RequestMethod.GET, produces="application/json")
	@ResponseStatus(code=HttpStatus.OK)
	public Map<String, String> getCategories(
			HttpServletRequest request
			){
		Map<String, String> resultMap = new HashMap<String, String>();
		
		for(WordCategory cat : WordCategory.values()) {
			resultMap.put(cat.name(), cat.getRegex());	
		}
		
		return resultMap;
	}
	
}
