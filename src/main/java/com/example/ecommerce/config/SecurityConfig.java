package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration with intentional security issues (technical debt)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Migrated from WebSecurityConfigurerAdapter to SecurityFilterChain bean pattern
     * Compatible with Spring Boot 2.x and 3.x
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // TECHNICAL DEBT: Disabling CSRF protection completely
            .csrf().disable()
            
            // TECHNICAL DEBT: Allowing all requests without authentication
            .authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            
            // TECHNICAL DEBT: Disabling frame options for H2 console (security risk)
            .and()
            .headers().frameOptions().disable();
        
        return http.build();
    }

    /**
     * TECHNICAL DEBT: Using deprecated NoOpPasswordEncoder
     * Passwords are stored and compared in plain text
     */
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}