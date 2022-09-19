package com.nipi.blacklog.service.impl;

import com.nipi.blacklog.config.RabbitMQConfig;
import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.dto.ResourceHolder;
import com.nipi.blacklog.excel.model.WorkbookType;
import com.nipi.blacklog.exception.DownloadFileException;
import com.nipi.blacklog.exception.UploadFileException;
import com.nipi.blacklog.feign.FileStorageFeignService;
import com.nipi.blacklog.mappers.MapStructMappers;
import com.nipi.blacklog.model.FileItem;
import com.nipi.blacklog.model.FileStatus;
import com.nipi.blacklog.repository.FilesMetadataRepository;
import com.nipi.blacklog.service.ExcelFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelFileServiceImpl implements ExcelFileService {

	private final FilesMetadataRepository filesMetadataRepository;
	private final FileStorageFeignService fileStorageFeignService;
	private final RabbitMQConfig rabbitMQConfiguration;
	private final RabbitMessagingTemplate rabbitTemplate;

	@Override
	public FileItemDto uploadFile(MultipartFile file, WorkbookType workbookType) {
		filesMetadataRepository.findByWorkbookType(workbookType)
				.ifPresent(filesMetadataRepository::delete);

		FileItem fileItem = FileItem.builder()
				.fileStatus(FileStatus.SAVING)
				.filename(file.getOriginalFilename())
				.workbookType(workbookType)
				.size(file.getSize())
				.build();

		//TODO сохраняем со статусом SAVING, однако если ошибка от feign client, то нас ведёт
		//TODO в декодер сласса FeignConfig и там мы не можем записать ошибку в базу по идее...

		//TODO можно ли сделать на эту задачу отдельную очередь, нужно ли тогда выность это в отдельный сервис?
		filesMetadataRepository.save(fileItem);

		FileItemDto savedFileDto = fileStorageFeignService.upload(file)
				.orElseThrow(() -> new UploadFileException(
						String.format("Couldn't upload the file. Filename: %s.", file.getName())));

		fileItem.setFilepath(savedFileDto.getFilepath());
		fileItem.setFileStatus(FileStatus.SAVED);
		filesMetadataRepository.save(fileItem);

		rabbitTemplate.convertAndSend(
				rabbitMQConfiguration.getExcelExchange(),
				rabbitMQConfiguration.getExcelParsingRoutingKey(),
				MapStructMappers.INSTANCE.fileItemToParsingRequest(fileItem));

		return MapStructMappers.INSTANCE.fileItemToFileItemDto(fileItem);
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
