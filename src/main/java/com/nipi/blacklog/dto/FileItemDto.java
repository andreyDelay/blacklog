package com.nipi.blacklog.dto;

import lombok.Data;

@Data
public class FileItemDto {
	private String filename;
	private String filepath;
	private String fileStatus;
	private long size;
}
