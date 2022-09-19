package com.nipi.blacklog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BlacklogApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlacklogApplication.class, args);
	}

}
