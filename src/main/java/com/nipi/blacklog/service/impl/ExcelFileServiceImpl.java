package com.nipi.blacklog.service.impl;

import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.ResourceHolder;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelFileServiceImpl implements ExcelFileService {

	private final FilesMetadataRepository filesMetadataRepository;
	private final FileStorageFeignService fileStorageFeignService;

	@Override
	public FileItemDto uploadFile(MultipartFile file, WorkbookType workbookType) {
		String originalFilename = file.getOriginalFilename();
		FileItem fileItem = FileItem.builder()
				.fileStatus(FileStatus.SAVING)
				.filename(originalFilename)
				.build();

		filesMetadataRepository.save(fileItem);

		FileItemDto savedFileDto = fileStorageFeignService.upload(file)
				.orElseThrow(() -> new UploadFileException(
						String.format("Couldn't upload the file. Filename: %s.", file.getName())));

		fileItem.setFilepath(savedFileDto.getFilepath());
		fileItem.setFileStatus(FileStatus.SAVED);
		filesMetadataRepository.save(fileItem);

		savedFileDto.setFileStatus(fileItem.getFileStatus().name());
		return savedFileDto;
	}

	@Override
	public ResourceHolder downloadFile(String filepath) {
		Resource resource = fileStorageFeignService.download(filepath)
				.orElseThrow(() -> new DownloadFileException(
						String.format("Couldn't download the file. Filepath: %s.", filepath)
				));

		String filename = resource.getFilename() == null ? "requested file.xlsx" : resource.getFilename();
		return ResourceHolder.builder()
				.resource(resource)
				.filename(filename)
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
