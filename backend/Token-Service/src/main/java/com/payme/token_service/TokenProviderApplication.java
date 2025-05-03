package com.payme.token_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TokenProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenProviderApplication.class, args);
	}

}
