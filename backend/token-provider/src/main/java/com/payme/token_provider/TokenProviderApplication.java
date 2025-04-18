package com.payme.token_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
@Profile("dev")
@EnableScheduling
@SpringBootApplication
public class TokenProviderApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TokenProviderApplication.class);
		application.setAdditionalProfiles("dev");
		application.run(args);
	}

}
