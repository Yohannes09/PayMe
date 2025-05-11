package com.payme.token_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // save for gateway. Dont forget to add cors in WebSecurityConfig
//    @Bean
//    public WebMvcConfigurer corsConfig(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                // Remember to fix this so it's not hardcoded.
//                registry.addMapping("api/**")
//                        .allowedOrigins("http://localhost:8081")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//                        .allowedHeaders("*")
//                        .allowCredentials(true); // Indicates whether user credentials (like cookies or auth headers) are allowed in CORS requests
//            }
//        };
//    }
}
