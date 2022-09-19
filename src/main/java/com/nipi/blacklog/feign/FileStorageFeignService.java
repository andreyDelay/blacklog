package com.nipi.blacklog.feign;

import com.nipi.blacklog.config.FeignConfig;
import com.nipi.blacklog.dto.FileItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@FeignClient(value = "storage-service", url = "${feign.storage-service-url}", configuration = FeignConfig.class)
public interface FileStorageFeignService {

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	Optional<FileItemDto> upload(@RequestPart(value = "file") MultipartFile file);

	@GetMapping(value = "/download")
	Optional<Resource> download(@RequestParam String filepath);
}
