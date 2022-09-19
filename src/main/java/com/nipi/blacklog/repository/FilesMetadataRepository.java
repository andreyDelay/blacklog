package com.nipi.blacklog.repository;

import com.nipi.blacklog.excel.model.WorkbookType;
import com.nipi.blacklog.model.FileItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FilesMetadataRepository extends CrudRepository<FileItem, Long> {
	List<FileItem> findAll();

	Optional<FileItem> findByWorkbookType(WorkbookType workbookType);
}
