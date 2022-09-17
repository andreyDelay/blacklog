package com.nipi.blacklog.controller;

import com.nipi.blacklog.dto.DownloadRequestDto;
import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.ResourceHolder;
import com.nipi.blacklog.dto.UploadResponseDto;
import com.nipi.blacklog.excel.WorkbookType;
import com.nipi.blacklog.exception.DownloadFileException;
import com.nipi.blacklog.service.ExcelFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FilesController {

	private final ExcelFileService excelFileService;

	@PostMapping(value = "/upload/basetp",
				consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public UploadResponseDto uploadBaseTp(@RequestPart("file") MultipartFile file) {
		return excelFileService.uploadFile(file, WorkbookType.BASE_TP);
	}

	@GetMapping
	public List<FileItemDto> getFilesList() {
		return excelFileService.getFilesList();
	}

	@PostMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resource> downloadFile(@RequestBody DownloadRequestDto downloadRequest) {
		ResourceHolder resourceHolder = excelFileService.downloadFile(downloadRequest.getFilepath());

		try {
			return ResponseEntity.ok()
					.headers(resourceHolder.getHeaders())
					.contentLength(resourceHolder.getResource().contentLength())
					.contentType(resourceHolder.getMediaType())
					.body(resourceHolder.getResource());
		} catch (IOException e) {
			throw new DownloadFileException(
					String.format("File was found, but error occurred during creating a response.\n" +
										" Error: %s.", e.getMessage()));
		}

	}
}
