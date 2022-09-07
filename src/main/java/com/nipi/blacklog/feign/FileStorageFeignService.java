package com.nipi.blacklog.feign;

import com.nipi.blacklog.config.FeignConfig;
import com.nipi.blacklog.dto.DownloadRequestDto;
import com.nipi.blacklog.dto.FeignStorageServiceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@FeignClient(value = "storage-service",
			url = "${feign-client.url.file-storage-service}",
			configuration = FeignConfig.class)
public interface FileStorageFeignService {

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	Optional<FeignStorageServiceResponseDto> upload(@RequestPart(value = "file") MultipartFile file);

	@PostMapping("/download")
	Optional<Resource> download(@RequestBody() DownloadRequestDto requestDto);
}
