package com.nipi.blacklog.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Builder
@Getter
public class ResourceHolder {
	private Resource resource;
	private String filename;
	private HttpHeaders headers;
	private MediaType mediaType;
}
