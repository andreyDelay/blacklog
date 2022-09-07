package com.nipi.blacklog.exception;


import org.springframework.http.HttpStatus;

public class DownloadFileException extends ApiException {
	public DownloadFileException(String message) {
		super("CANNOT_DOWNLOAD_FILE", message, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
