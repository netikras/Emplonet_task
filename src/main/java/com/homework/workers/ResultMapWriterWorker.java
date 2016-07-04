package com.homework.workers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.homework.objects.Constants;
import com.homework.objects.WordCategory;

public class ResultMapWriterWorker extends WorkerThread {
	
	
	
	private final WordCategory category;
	
	private final Map<String, Long> buffer;
	
	private boolean dictHasBeenLoaded = false;
	
	
	public ResultMapWriterWorker(WordCategory cat) {
		Thread.currentThread().setName("ResultMapWriter: "+cat.name());
		this.category = cat;
		buffer = new HashMap<String, Long>();
	}
	
	
	
	
	
	
	private void loadExistingDict() {
		
		if (dictHasBeenLoaded) return; // let's save some overhead....
		// We already have thread-per-dict structure. No need to work their bottoms off... :)
		
		
		File dictFile = category.getFile();
		
		BufferedReader reader;
		String line;
		String[] lineFields;
		
		if (dictFile.exists() && dictFile.isFile() && dictFile.canRead()) {
			try {
				reader = new BufferedReader( new FileReader(dictFile));
				
				while ((line = reader.readLine()) != null){
					lineFields = line.split(Constants.output_IFS);
					
					if (lineFields.length == 2) {
						addResultToBuffer(lineFields[0], Long.parseLong(lineFields[1]));
					} else {
						System.err.println("Incorrect number of line fields: "+line);
					}
				}
				
				dictHasBeenLoaded = true;
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
		}
	}
	
	
	
	private void writeUpdatedDict() {
		
		System.out.println("Starting to write dicts");
		File dictFile = category.getFile();
		
		BufferedWriter writer = null;
		StringBuilder sb;
		ThreadState prevState;
		
		System.out.println("Setting writer state to BUSY");
		prevState = setWorkerState(ThreadState.BUSY);
		System.out.println("Writer is now BUSY");
		if ( ! dictFile.exists()) {
			try {
				dictFile.createNewFile();
			} catch (IOException e) {
				System.err.println("Cannot create a dict file: "+dictFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
		
		if (dictFile.exists() && dictFile.isFile() && dictFile.canWrite()) {
			try {
				writer = new BufferedWriter(new FileWriter(dictFile));
				
				System.out.println("Buffer size: "+buffer.size());
				
				for(String word : getBuffer().keySet()){
					
					sb = new StringBuilder(word);
					sb.append(Constants.output_IFS);
					sb.append(buffer.get(word).longValue());
					sb.append(Constants.lineEnd);
					System.out.println("Writing to "+dictFile.getAbsolutePath()+": "+sb.toString());
					writer.write(sb.toString());
					
					writer.flush();
				}
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) 
						writer.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			
		} else {
			System.err.println("ERROR: dictFile exists="+dictFile.exists()+"; dictFile.isFile="+dictFile.isFile()+"; dictFile.canWrite="+dictFile.canWrite());
		}
		System.out.println("restoring writer state");
		this.setWorkerStateForce(prevState);
	}
	
	
	
	public synchronized void addResultToBuffer(String word, Long count){
		ThreadState prevState;
		
		if (getWorkerState() == ThreadState.BUSY) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
		prevState = setWorkerState(ThreadState.BUSY);
		
		Long lastCount = buffer.get(word);
		
		if (lastCount == null) {
			buffer.put(word, count);
		} else {
			buffer.put(word, lastCount + count);
		}
		setWorkerState(prevState);
		
		
	}
	
	
	public Map<String, Long> getBuffer() {
		return this.buffer;
	}
	
	
	public synchronized void saveAllResults() {
		//this.notify();
		ThreadState prevState;
		
		prevState = this.setWorkerState(ThreadState.BUSY);
		
		this.writeUpdatedDict();
		
		this.setWorkerState(prevState);
	}
	
	
	
	
	@Override
	public void run() {
		
		setWorkerState(ThreadState.RUNNING);
		
		while(getWorkerState() != ThreadState.STOPPED) {
		
			loadExistingDict();
			
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				
			}
			
			saveAllResults();
		}
		
	}
	
	

}
