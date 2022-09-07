package com.nipi.blacklog.dto;

import com.nipi.blacklog.model.FileStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponseDto {
	private String filename;
	private FileStatus fileStatus;
}
