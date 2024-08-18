package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.service.RestTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferServiceConfig {

    @Bean
    public RestTransferService transferService(){
        return  new RestTransferService();
    }
}
