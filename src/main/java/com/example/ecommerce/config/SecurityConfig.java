package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration with intentional security issues (technical debt)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Security filter chain configuration
     * Migrated from deprecated WebSecurityConfigurerAdapter to SecurityFilterChain pattern for Spring Boot 3.x
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // TECHNICAL DEBT: Disabling CSRF protection completely
            .csrf(csrf -> csrf.disable())
            
            // TECHNICAL DEBT: Allowing all requests without authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            
            // TECHNICAL DEBT: Disabling frame options for H2 console (security risk)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
            
        return http.build();
    }

    /**
     * Password encoder using BCrypt
     * Replaced NoOpPasswordEncoder for better security
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
