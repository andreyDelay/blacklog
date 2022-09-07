package com.nipi.blacklog.exception;

import com.nipi.blacklog.dto.ExceptionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<?> handleException(ApiException exception) {
		ExceptionResponseDto exceptionResponse = new ExceptionResponseDto(exception.getCode(), exception.getMessage());
		return new ResponseEntity<>(exceptionResponse, exception.getHttpStatus());
	}
}
