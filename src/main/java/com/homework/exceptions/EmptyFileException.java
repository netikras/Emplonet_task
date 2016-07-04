package com.homework.exceptions;

public class EmptyFileException extends Exception {

	private static final long serialVersionUID = -9043861044639521789L;
	
	
	public EmptyFileException() {
		super();
	}
	
	
	public EmptyFileException(String message) {
		super(message);
	}
	
	
	
}
