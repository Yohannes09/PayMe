//package com.payme.token_service.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//public class DataSourceConfig {
//    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
//
//    @Value("${DB_URL}") private String dbUrl;
//
//    @Value("${DB_USER}") private String dbUsername;
//
//    @Value("${DB_PASSWORD}") private String dbPassword;
//
//    @Profile({"dev", "prod"})
//    @Bean
//    public DataSource postgresDataSource(){
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(dbUrl);
//        config.setUsername(dbUsername);
//        config.setPassword(dbPassword);
//        config.setDriverClassName(POSTGRES_DRIVER);
//        return new HikariDataSource(config);
//    }
//
//}
