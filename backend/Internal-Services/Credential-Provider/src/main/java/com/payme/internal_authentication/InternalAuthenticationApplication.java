package com.payme.internal_authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternalAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication authenticationService = new SpringApplication(InternalAuthenticationApplication.class);
		authenticationService.setAdditionalProfiles("dev");
		authenticationService.run(args);
	}

}
