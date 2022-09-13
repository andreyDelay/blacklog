package com.nipi.blacklog.service.impl;

import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.UploadResponseDto;
import com.nipi.blacklog.excel.WorkbookType;
import com.nipi.blacklog.exception.DownloadFileException;
import com.nipi.blacklog.exception.UploadFileException;
import com.nipi.blacklog.feign.FileStorageFeignService;
import com.nipi.blacklog.model.FileItem;
import com.nipi.blacklog.model.FileStatus;
import com.nipi.blacklog.repository.FilesMetadataRepository;
import com.nipi.blacklog.service.ExcelFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelFileServiceImpl implements ExcelFileService {

	private final FilesMetadataRepository filesMetadataRepository;
	private final FileStorageFeignService fileStorageFeignService;

	@Override
	public UploadResponseDto uploadFile(MultipartFile file, WorkbookType workbookType) {
		FileItemDto response = fileStorageFeignService.upload(file)
				.orElseThrow(() -> new UploadFileException(
						String.format("Couldn't upload the file. Filename: %s.", file.getName())));

		FileItem fileItem = FileItem.builder()
				.filename(response.getFilename())
				.filepath(response.getFilepath())
				.size(response.getSize())
				.fileStatus(FileStatus.UPLOADED)
				.build();

		FileItem savedFileItem = filesMetadataRepository.save(fileItem);

		return UploadResponseDto.builder()
				.filename(savedFileItem.getFilename())
				.fileStatus(savedFileItem.getFileStatus())
				.build();
	}

	@Override
	public Resource downloadFile(String filepath) {
		return fileStorageFeignService.download(filepath)
				.orElseThrow(() -> new DownloadFileException(
						String.format("Couldn't download the file. Filepath: %s.", filepath)
				));
	}

	@Override
	public List<FileItemDto> getFilesList() {
		return filesMetadataRepository.findAll()
				.stream()
				.map(FileItemDto::fromFileItem)
				.toList();
	}
}
