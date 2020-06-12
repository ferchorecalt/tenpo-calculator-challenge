package com.tenpo.challenge.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.tenpo.challenge.auth.exception.LoginUnsuccessfulException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({UsernameNotFoundException.class, LoginUnsuccessfulException.class})
	public ResponseEntity<?> handleResourceNotFoundException(Exception ex, WebRequest request) {
		return createErrorResponse(ex.getMessage(), request, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({InvalidArgumentException.class, IllegalArgumentException.class, BadCredentialsException.class, MissingServletRequestParameterException.class})
	public ResponseEntity<?> handleBadRequestException(Exception ex, WebRequest request) {
		return createErrorResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<?> handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) {
		return createErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(Exception ex, WebRequest request) {
		return createErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), request, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
		return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), request, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private static ResponseEntity<?> createErrorResponse(String message, WebRequest request, HttpStatus httpStatus) {
		ErrorDetail errorDetails = new ErrorDetail(new Date(), message, request.getDescription(false));
		return new ResponseEntity<>(errorDetails, httpStatus);
	}
}
