package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Main application class for Legacy Ecommerce Application
 * Uses deprecated WebMvcConfigurerAdapter for technical debt
 */
@SpringBootApplication
public class LegacyEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegacyEcommerceApplication.class, args);
    }

    /**
     * CORS configuration using deprecated WebMvcConfigurerAdapter
     * This is intentional technical debt
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
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