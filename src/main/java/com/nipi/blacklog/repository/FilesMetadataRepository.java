package com.nipi.blacklog.repository;

import com.nipi.blacklog.model.FileItem;
import org.springframework.data.repository.CrudRepository;

public interface FilesMetadataRepository extends CrudRepository<FileItem, Long> {

}
