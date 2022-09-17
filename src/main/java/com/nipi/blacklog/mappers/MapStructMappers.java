package com.nipi.blacklog.mappers;

import com.nipi.blacklog.dto.FileItemDto;
import com.nipi.blacklog.model.FileItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapStructMappers {
	MapStructMappers INSTANCE = Mappers.getMapper(MapStructMappers.class);

	FileItemDto fileItemToFileItemDto(FileItem fileItem);
	FileItem fileItemDtoToFileItem(FileItemDto fileItemDto);
}
