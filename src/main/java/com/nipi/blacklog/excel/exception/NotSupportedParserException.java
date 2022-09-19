package com.nipi.blacklog.excel.exception;

import com.nipi.blacklog.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotSupportedParserException extends ApiException {
	public NotSupportedParserException(String message) {
		super("PARSER_NOT_FOUND", message, HttpStatus.NOT_ACCEPTABLE);
	}
}
