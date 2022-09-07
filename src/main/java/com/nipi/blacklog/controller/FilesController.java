package com.nipi.blacklog.controller;

import com.nipi.blacklog.dto.DownloadRequestDto;
import com.nipi.blacklog.dto.UploadResponseDto;
import com.nipi.blacklog.excel.WorkbookType;
import com.nipi.blacklog.exception.DownloadFileException;
import com.nipi.blacklog.service.ExcelFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FilesController {

	private final ExcelFileService excelFileService;

	@PostMapping("/upload/basetp")
	public UploadResponseDto uploadBaseTp(@RequestParam("file") MultipartFile file) {
		return excelFileService.uploadFile(file, WorkbookType.BASE_TP);
	}

	@PostMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> downloadFile(@RequestBody DownloadRequestDto downloadRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=response.xlsx");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Expires", "0");

		Resource resource = excelFileService.downloadFile(downloadRequest);
		try {
			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		} catch (IOException e) {
			throw new DownloadFileException(
					String.format("File was found, but occurred during creating a response.\n" +
							" Error: %s.", e.getMessage()));
		}

	}
}
