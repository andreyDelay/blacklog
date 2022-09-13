package com.nipi.blacklog.controller;

import com.nipi.blacklog.dto.DownloadRequestDto;
import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.UploadResponseDto;
import com.nipi.blacklog.excel.WorkbookType;
import com.nipi.blacklog.exception.DownloadFileException;
import com.nipi.blacklog.service.ExcelFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
		Resource resource = excelFileService.downloadFile(downloadRequest.getFilepath());

		try {
			String filename = resource.getFilename() == null ? "requested file.xlsx" : resource.getFilename();
			ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
					.filename(filename, StandardCharsets.UTF_8)
					.build();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Expires", "0");
			headers.setContentDisposition(contentDisposition);

			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		} catch (IOException e) {
			throw new DownloadFileException(
					String.format("File was found, but error occurred during creating a response.\n" +
										" Error: %s.", e.getMessage()));
		}

	}
}
