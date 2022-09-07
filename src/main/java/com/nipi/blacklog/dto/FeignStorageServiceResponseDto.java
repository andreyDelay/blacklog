package com.nipi.blacklog.dto;

import lombok.Data;

@Data
public class FeignStorageServiceResponseDto {
	private String filename;
	private String filepath;
	private long size;
}
