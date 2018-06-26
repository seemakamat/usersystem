package com.example.usersystem.exception.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.usersystem.exception.UserNotFoundException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
				ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {UserNotFoundException.class})
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, WebRequest req) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
				ex.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest req) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
				ex.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
	}

}
