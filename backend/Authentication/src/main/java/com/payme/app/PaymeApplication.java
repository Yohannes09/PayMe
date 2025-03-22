package com.payme.app;

import com.payme.app.entity.User;
import com.payme.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class PaymeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymeApplication.class, args);

    }

}
