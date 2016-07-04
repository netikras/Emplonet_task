package com.homework.workers;

public class WorkerFactory {
	
	
	private volatile static ResultMapDispatcherWorker resultMapDispatcherWorker = null;
	
	
	public static synchronized ResultMapDispatcherWorker getResultMapDispatcher() {
		if (resultMapDispatcherWorker == null) {
			
			synchronized (WorkerFactory.class) {
				if (resultMapDispatcherWorker == null) {
					resultMapDispatcherWorker = new ResultMapDispatcherWorker();
					new Thread(resultMapDispatcherWorker).start();
				}
			}
			
		}
		
		return resultMapDispatcherWorker;
	}
	
	
	
}
