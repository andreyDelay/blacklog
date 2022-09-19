package com.nipi.blacklog.service;

import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.ResourceHolder;
import com.nipi.blacklog.excel.model.WorkbookType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelFileService {
	FileItemDto uploadFile(MultipartFile file, WorkbookType workbookType);

	ResourceHolder downloadFile(String filepath);

	List<FileItemDto> getFilesList();

}
