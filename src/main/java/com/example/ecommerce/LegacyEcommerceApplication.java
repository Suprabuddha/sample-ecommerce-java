package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main application class for Legacy Ecommerce Application
 * Migrated to use WebMvcConfigurer interface directly (Spring Boot 3.x compatible)
 */
@SpringBootApplication
public class LegacyEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegacyEcommerceApplication.class, args);
    }

    /**
     * CORS configuration using modern WebMvcConfigurer
     * Migrated from deprecated WebMvcConfigurerAdapter
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
