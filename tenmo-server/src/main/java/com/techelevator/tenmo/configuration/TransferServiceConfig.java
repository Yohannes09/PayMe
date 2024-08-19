package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.service.RestTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Creates a bean for RestTransferService ensuring
 * it's managed by Spring and available for dependency injection. Without this
 * configuration, the application won't be able to inject or use RestTransferService
 * leading to failures.
 *
 * - RestTransferService No args constructor has its own instances of: AccountRepo, TransferRepo.
 * - Can also be injected with a JdbcTemplate as well, w/ Args Constructor.
 */
@Configuration
public class TransferServiceConfig {

    @Bean
    public RestTransferService transferService(){
        return new RestTransferService();
    }
}
