package com.nipi.blacklog.repository;

import com.nipi.blacklog.model.FileItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilesMetadataRepository extends CrudRepository<FileItem, Long> {
	List<FileItem> findAll();
}
