package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main application class for Legacy Ecommerce Application
 * Updated to use WebMvcConfigurer interface directly (Spring Boot 3.x compatible)
 */
@SpringBootApplication
public class LegacyEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegacyEcommerceApplication.class, args);
    }

    /**
     * CORS configuration using modern WebMvcConfigurer lambda pattern
     * Updated for Spring Boot 3.x compatibility
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}