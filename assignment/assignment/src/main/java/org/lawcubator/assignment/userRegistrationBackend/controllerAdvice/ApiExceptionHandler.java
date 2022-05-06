package org.lawcubator.assignment.userRegistrationBackend.controllerAdvice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global Exception Handler of the application
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String NO_DATA_ERROR_MESSAGE = "No data found with given input";
	
	/**
	 * Handles {@code DataIntegrityViolationException} in the case of violation of any unique key constraints
	 * for any attribute
	 * 
	 * @param rex The Exception encountered
	 * @param wx The current Web Request
	 * @return Response Entity with appropriate message and status
	 */
	@ExceptionHandler(value = DataIntegrityViolationException.class)
	private ResponseEntity<Object> handleInvalidCredentials(RuntimeException rex, WebRequest wx) {
		return handleExceptionInternal(rex, rex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, wx);
	}
	
	/**
	 * Handles {@code EmptyResultDataAccessException} in the case if any entity being requested 
	 * is not present in the database
	 * 
	 * @param rex The Exception encountered
	 * @param wx The current Web Request
	 * @return Response Entity with appropriate message and status
	 */
	@ExceptionHandler(value = EmptyResultDataAccessException.class)
	private ResponseEntity<Object> handleNoDataResponse(RuntimeException rex, WebRequest wx) {
		return handleExceptionInternal(rex, NO_DATA_ERROR_MESSAGE, new HttpHeaders(), HttpStatus.NOT_FOUND, wx);
	}
	
	/**
	 * Handles {@code IllegalArgumentException} in the case of any activity or action being 
	 * requested was not allowed to be performed, or, if User credentials are not present in the database
	 * or are invalid
	 * 
	 * @param rex The Exception encountered
	 * @param wx The current Web Request
	 * @return Response Entity with appropriate message and status
	 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	private ResponseEntity<Object> handleIllegalAccess(RuntimeException rex, WebRequest wx) {
		return handleExceptionInternal(rex, rex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, wx);
	}
}
