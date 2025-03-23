package com.payme.emailService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("dev")
public class EmailServiceApplication{

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

}
