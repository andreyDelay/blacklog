package com.nipi.blacklog.dto;

import com.nipi.blacklog.model.FileItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileItemDto {
	private String filename;
	private String filepath;
	private String status;
	private long size;

	public static FileItemDto fromFileItem(FileItem fileItem) {
		return FileItemDto.builder()
				.filename(fileItem.getFilename())
				.filepath(fileItem.getFilepath())
				.size(fileItem.getSize())
				.status(fileItem.getFileStatus().toString())
				.build();
	}
}
