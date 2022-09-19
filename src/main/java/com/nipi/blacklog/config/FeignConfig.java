package com.nipi.blacklog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nipi.blacklog.exception.ApiException;
import com.nipi.blacklog.exception.FileStorageServiceException;
import feign.Logger;
import feign.Response;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class FeignConfig {

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.BASIC;
	}

	@Bean
	public ErrorDecoder errorDecoder() {
		return new FeignResponseErrorDecoder();
	}

	@Bean
	public Encoder multipartFormEncoder() {
		return new SpringFormEncoder(new SpringEncoder(() ->
				new HttpMessageConverters(new RestTemplate().getMessageConverters())));
	}

	static class FeignResponseErrorDecoder implements ErrorDecoder {

		private ErrorDecoder errorDecoder = new Default();

		@Override
		public Exception decode(String s, Response response) {
			if (response.status() > 299) { //TODO кривое сравнение, но как сделать лучше...
				ObjectMapper mapper = new ObjectMapper();
				try {
					ApiException apiException = mapper.readValue(
							response.body().asInputStream(),
							FileStorageServiceException.class);

					throw new FileStorageServiceException(
							apiException.getMessage(),
							HttpStatus.valueOf(response.status()));
				} catch (IOException e) {
					throw new FileStorageServiceException(
							String.format("Storage service cannot process this operation." +
									"Internal error: %s", e.getMessage()));
				}
			}
			return errorDecoder.decode(s, response);
		}
	}
}
