package com.payme.token_provider.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("dev")
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource postgresDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/test");
        config.setUsername("postgres");
        config.setPassword("");
        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }
}
