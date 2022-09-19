package com.nipi.blacklog.exception;
import org.springframework.http.HttpStatus;

public class UploadFileException extends ApiException {
	public UploadFileException(String message) {
		super("CANNOT_UPLOAD_FILE", message, HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
