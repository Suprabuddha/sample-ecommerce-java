package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration updated for Spring Boot 3.x / Spring Security 6.x
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Updated to use SecurityFilterChain pattern (Spring Security 6.x)
     * Replaced deprecated WebSecurityConfigurerAdapter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // TECHNICAL DEBT: Still disabling CSRF protection
            .csrf(csrf -> csrf.disable())
            
            // Updated to use authorizeHttpRequests and requestMatchers (Spring Security 6.x)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            
            // TECHNICAL DEBT: Disabling frame options for H2 console
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        return http.build();
    }

    /**
     * Updated to BCryptPasswordEncoder for better security
     * NOTE: Existing plain text passwords in database will need migration
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}