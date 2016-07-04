package com.homework.services.analyser;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.homework.exceptions.FileProcessingException;


public interface FileAnalyserService {
	
	
	public Map<String, Long> getResultsMap();
	
	public Map<String, Long> process(File file) throws FileProcessingException;
	
	public Map<String, Long> process(InputStream inputStream) throws FileProcessingException;
	
	
	
}
