package com.nipi.blacklog.excel.exception;

import com.nipi.blacklog.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ParsingException extends ApiException {
	public ParsingException(String message) {
		super("PARSING_ERROR", message, HttpStatus.NOT_FOUND);
	}
}
