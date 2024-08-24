package com.techelevator.tenmo.configuration;

import org.springframework.context.annotation.Configuration;

/*
 * Creates a bean for RestTenmoService ensuring
 * it's managed by Spring and available for dependency injection. Without this
 * configuration, the application won't be able to inject or use RestTenmoService
 * leading to failures.
 *
 * - RestTenmoService No args constructor has its own instances of: AccountRepo, TransferRepo.
 * - Can also be injected with a JdbcTemplate as well, w/ Args Constructor.
 */
@Configuration
public class TransferServiceConfig {

//    @Bean
//    public RestTenmoService transferService(){
//        return new RestTenmoService();
//    }
}
