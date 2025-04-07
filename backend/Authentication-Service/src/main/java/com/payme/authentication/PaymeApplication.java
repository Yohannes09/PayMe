package com.payme.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@SpringBootApplication
public class PaymeApplication {

    public static void main(String[] args) {
        SpringApplication authenticationService = new SpringApplication(PaymeApplication.class);
        authenticationService.setAdditionalProfiles("dev");
        authenticationService.run(args);
    }

}
