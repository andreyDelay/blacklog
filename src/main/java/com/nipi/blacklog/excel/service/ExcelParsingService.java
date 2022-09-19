package com.nipi.blacklog.excel.service;

import com.nipi.blacklog.excel.model.ParsingRequest;
import com.nipi.blacklog.excel.parser.ExcelParser;
import com.nipi.blacklog.excel.parser.ParserResolver;
import com.nipi.blacklog.excel.exception.ParsingException;
import com.nipi.blacklog.feign.FileStorageFeignService;
import com.nipi.blacklog.model.FileItem;
import com.nipi.blacklog.model.FileStatus;
import com.nipi.blacklog.repository.FilesMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExcelParsingService {

	private final ParserResolver parserResolver;
	private final FilesMetadataRepository filesMetadataRepository;
	private final FileStorageFeignService storageFeignService;

	@RabbitListener(queues = "${rabbitmq.excel.queue.parsing}")
	public void parse(ParsingRequest parsingRequest) {
		Resource resource = storageFeignService.download(parsingRequest.getFilepath())
				.orElseThrow(() -> new ParsingException(
						String.format("File with path %s not found in file storage",
										parsingRequest.getFilepath())
				));

		//TODO на строчку ниже столкнулся с проблемой , если в очереди есть сообщение на парсинг, а в базе этого
		//TODO файла не нашлось, то цикличная ошибка без остановки, пока не придумал как пофиксить
		FileItem fileItem = filesMetadataRepository.findByWorkbookType(parsingRequest.getWorkbookType())
				.orElseThrow(() -> new ParsingException(
						String.format("File metadata not found in database. Filename - %s",
								parsingRequest.getFilename())
				));
		//TODO если вверху возникает ошибка, не могу придумать как лучше записать FileStatus.ERROR

		fileItem.setFileStatus(FileStatus.PARSING);
		filesMetadataRepository.save(fileItem);

		try {
			ExcelParser excelParser = parserResolver.resolveParser(parsingRequest.getWorkbookType());
			excelParser.parse(resource.getInputStream());

			fileItem.setFileStatus(FileStatus.PROCESSED);
			filesMetadataRepository.save(fileItem);
		} catch (IOException e) {
			fileItem.setFileStatus(FileStatus.PARSING_FAIL);
			filesMetadataRepository.save(fileItem);
			throw new ParsingException(
					String.format("Parsing failed, cannot get stream from resource. Error: %s", e.getMessage()));
		}
	}
}
