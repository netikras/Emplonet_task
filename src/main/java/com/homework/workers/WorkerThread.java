package com.homework.workers;

public abstract class WorkerThread extends Thread {
	
	
	public enum ThreadState {
		
		BROKEN(-1),
		STOPPED(0),
		PAUSED(1),
		RUNNING(2),
		BUSY(3);
		
		
		
		private final int value;
		
		ThreadState(int value) {
			this.value = value;
		}
		
		public int getValue(){
			return this.value;
		}
	}
	
	
	
	
	protected volatile ThreadState STATE = ThreadState.STOPPED;
	
	public ThreadState getWorkerState() {
		return this.STATE;
	}
	
	public synchronized ThreadState setWorkerState(ThreadState state) {
		/*while (getWorkerState() == ThreadState.BUSY) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}*/
		
		ThreadState oldState = getWorkerState();
		this.STATE = state;
		
		return oldState;
	}
	
	
	protected synchronized void setWorkerStateForce(ThreadState state) {
		this.STATE = state;
		this.notifyAll();
	}
	
	
}
