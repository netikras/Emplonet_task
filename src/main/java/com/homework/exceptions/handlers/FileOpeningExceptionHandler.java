package com.homework.exceptions.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.homework.exceptions.FileOpeningException;
import com.homework.objects.ErrorBody;


@ControllerAdvice
public class FileOpeningExceptionHandler {
	
	public FileOpeningExceptionHandler() {
		System.out.println("Loading FileOpeningExceptionHandler");
	}
	
	
	@ExceptionHandler(value=FileOpeningException.class)
	public @ResponseBody ErrorBody fileUploadError(
			HttpServletResponse response,
			HttpServletRequest request,
			Exception exception
			) {
		ErrorBody errorBody = new ErrorBody();
		
		errorBody.setMessage1("File could not be opened");
		errorBody.setMessage2(exception.getLocalizedMessage());
		
		request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		
		response.setContentType("application/json");
		
		response.setStatus(415);
		
		
		return errorBody;
	}
	
}
