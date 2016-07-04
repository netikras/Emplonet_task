package com.homework.exceptions.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.homework.exceptions.FileProcessingException;
import com.homework.objects.ErrorBody;


@ControllerAdvice
public class FileProcessingExceptionHandler {
	
	public FileProcessingExceptionHandler() {
		System.out.println("Loading FileProcessingExceptionHandler");
	}
	
	
	@ExceptionHandler(value=FileProcessingException.class)
	public @ResponseBody ErrorBody fileProcessingError(
			HttpServletResponse response,
			HttpServletRequest request,
			Exception exception
			) {
		ErrorBody errorBody = new ErrorBody();
		
		errorBody.setMessage1("File cannot be processed");
		errorBody.setMessage2(exception.getLocalizedMessage());
		
		request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		
		response.setContentType("application/json");
		
		response.setStatus(422);
		
		
		return errorBody;
	}
	
}
