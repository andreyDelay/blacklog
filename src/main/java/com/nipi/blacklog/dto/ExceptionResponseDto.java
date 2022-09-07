package com.nipi.blacklog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponseDto {
	private String code;
	private String message;
}
