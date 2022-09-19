package com.nipi.blacklog.exception;

import org.springframework.http.HttpStatus;

public class FileStorageServiceException extends ApiException {
	public FileStorageServiceException(String message) {
		super("FILE_STORAGE_SERVICE_ERROR", message, HttpStatus.SERVICE_UNAVAILABLE);
	}

	public FileStorageServiceException(String message, HttpStatus httpStatus) {
		super("FILE_STORAGE_SERVICE_ERROR", message, httpStatus);
	}
}
