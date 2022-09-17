package com.nipi.blacklog.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Builder
@Getter
public class ResourceHolder {
	private Resource resource;
	private String filename;
}
