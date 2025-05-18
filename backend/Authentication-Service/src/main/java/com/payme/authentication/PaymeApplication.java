package com.payme.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class PaymeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymeApplication.class, args);
    }

}
