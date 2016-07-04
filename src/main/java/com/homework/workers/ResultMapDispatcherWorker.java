package com.homework.workers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.homework.objects.WordCategory;

public class ResultMapDispatcherWorker extends WorkerThread {
	
	
	
	private BlockingQueue<Map<String, Long>> queue;
	
	private Map<WordCategory, ResultMapWriterWorker> writers;
	
	
	/**
	 * Intended to be run as a singleton.
	 * <br>This worker works on a queue-basis. It processes Map<String, Long> data
	 * <br> by determining word category and passing that data to respective 
	 * <br> writer thread. Writer will buffer all that data up and flush it to 
	 * <br> respective dictionary file.
	 */
	public ResultMapDispatcherWorker() {
		Thread.currentThread().setName("ResultMapDispatcher");
		
		//this.queue = new LinkedList<Map<String,Long>>();
		queue = new LinkedBlockingQueue<Map<String,Long>>();
		this.writers = new HashMap<WordCategory, ResultMapWriterWorker>();
		
		startWriters(WordCategory.values());
		
	}
	
	
	
	public synchronized void startWriters(WordCategory[] cats) {
		for (WordCategory cat: cats) {
			if ( ! writers.containsKey(cat)) { // no need to run multiple workers doing the same job
				writers.put(cat, new ResultMapWriterWorker(cat));
				new Thread(writers.get(cat)).start();
			}
		}
	}
	
	
	public void stopWriters(WordCategory[] cats) {
		// let NullPointerException be thrown if it has to be
		for(WordCategory cat: cats) {
			if (writers.containsKey(cat)) {
				getWriter(cat).setWorkerState(ThreadState.STOPPED);
			}
		}
	}
	
	
	public ResultMapWriterWorker getWriter(WordCategory cat) {
		return writers.get(cat);
	}
	
	
	public synchronized void saveAllDictionaries() {
		saveDictionaries(WordCategory.values());
	}
	
	public synchronized void saveDictionaries(WordCategory[] cats) {
		for(WordCategory cat : cats) {
			if (writers.containsKey(cat)) {
				writers.get(cat).saveAllResults();
			}
		}
	}
	
	
	
	public synchronized void addResultMap(Map<String, Long> resultMap) {
		System.out.println("Adding a result map to dispatcher queue");
		this.queue.add(resultMap);
		if (getWorkerState() == ThreadState.PAUSED && peekNextResult() == null) {
			setWorkerState(ThreadState.RUNNING);
			this.notify();
		}
	}
	
	
	
	private Map<String, Long> peekNextResult() {
		return queue.peek();
	}
	
	private Map<String, Long> getNextResult() {
		System.out.println("DISPATCHER getting a next result...");
		try {
			return queue.take();
		} catch (InterruptedException e) {
			
		}
		return null;
	}
	
	@Override
	public void run() {
		
		Map<String, Long> result = null;
		WordCategory cat;
		
		setWorkerState(ThreadState.RUNNING);
		
		while(getWorkerState() != ThreadState.STOPPED) {

			result = getNextResult();
			System.out.println("Dispatcher got a next result");
			System.out.println("Processing dispatcher queue....");

			if (result != null) {
				for (String word : result.keySet()) {
					System.out.println("next word in a queue: "+word);
					cat = WordCategory.determineCategory(word);
					
					if (cat != null) {
						try{
							writers.get(cat).addResultToBuffer(word, result.get(word));
						} catch(NullPointerException e) {
							System.err.println("Cannot find a writer for word "+word+" categorized as "+cat.name());
						}
					} else {
						System.err.println("Cannot determine a category for word: "+word);
					}
					
				}
			}
			
		}
		System.err.println("DISPATCHER DIED");
		
	}
	
}
