package com.techelevator.tenmo;

import com.techelevator.tenmo.service.RestTransferService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TenmoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenmoApplication.class, args);
    }

//    @Bean
//    public RestTransferService transferService(){
//        return  new RestTransferService();
//    }

}
