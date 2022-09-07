package com.nipi.blacklog.service;

import com.nipi.blacklog.dto.DownloadRequestDto;
import com.nipi.blacklog.dto.UploadResponseDto;
import com.nipi.blacklog.excel.WorkbookType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelFileService {
	UploadResponseDto uploadFile(MultipartFile file, WorkbookType workbookType);

	Resource downloadFile(DownloadRequestDto downloadRequest);
}
