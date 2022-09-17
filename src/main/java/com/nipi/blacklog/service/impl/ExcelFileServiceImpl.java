package com.nipi.blacklog.service.impl;

import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.ResourceHolder;
import com.nipi.blacklog.dto.UploadResponseDto;
import com.nipi.blacklog.excel.WorkbookType;
import com.nipi.blacklog.exception.DownloadFileException;
import com.nipi.blacklog.exception.UploadFileException;
import com.nipi.blacklog.feign.FileStorageFeignService;
import com.nipi.blacklog.mappers.MapStructMappers;
import com.nipi.blacklog.model.FileItem;
import com.nipi.blacklog.model.FileStatus;
import com.nipi.blacklog.repository.FilesMetadataRepository;
import com.nipi.blacklog.service.ExcelFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelFileServiceImpl implements ExcelFileService {

	private final FilesMetadataRepository filesMetadataRepository;
	private final FileStorageFeignService fileStorageFeignService;

	@Override
	public UploadResponseDto uploadFile(MultipartFile file, WorkbookType workbookType) {
		FileItemDto fileItemDto = fileStorageFeignService.upload(file)
				.orElseThrow(() -> new UploadFileException(
						String.format("Couldn't upload the file. Filename: %s.", file.getName())));

		FileItem fileItem = MapStructMappers.INSTANCE.fileItemDtoToFileItem(fileItemDto);
		fileItem.setFileStatus(FileStatus.UPLOADED);
		FileItem savedFileItem = filesMetadataRepository.save(fileItem);

		return UploadResponseDto.builder()
				.filename(savedFileItem.getFilename())
				.fileStatus(savedFileItem.getFileStatus())
				.build();
	}

	@Override
	public ResourceHolder downloadFile(String filepath) {
		Resource resource = fileStorageFeignService.download(filepath)
				.orElseThrow(() -> new DownloadFileException(
						String.format("Couldn't download the file. Filepath: %s.", filepath)
				));

		String filename = resource.getFilename() == null ? "requested file.xlsx" : resource.getFilename();
		ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
				.filename(filename, StandardCharsets.UTF_8)
				.build();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Expires", "0");
		headers.setContentDisposition(contentDisposition);

		return ResourceHolder.builder()
				.resource(resource)
				.filename(filename)
				.headers(headers)
				.mediaType(MediaType.APPLICATION_OCTET_STREAM)
				.build();
	}

	@Override
	public List<FileItemDto> getFilesList() {
		return filesMetadataRepository.findAll()
				.stream()
				.map(MapStructMappers.INSTANCE::fileItemToFileItemDto)
				.toList();
	}
}
