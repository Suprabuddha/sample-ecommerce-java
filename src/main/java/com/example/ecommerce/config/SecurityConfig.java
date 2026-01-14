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
     * Modern Spring Security configuration using SecurityFilterChain
     * Migrated from deprecated WebSecurityConfigurerAdapter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // TECHNICAL DEBT: Disabling CSRF protection completely
            .csrf().disable()
            
            // TECHNICAL DEBT: Allowing all requests without authentication
            .authorizeRequests()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            
            // TECHNICAL DEBT: Disabling frame options for H2 console (security risk)
            .and()
            .headers().frameOptions().disable();
        
        return http.build();
    }

    /**
     * Using BCryptPasswordEncoder instead of deprecated NoOpPasswordEncoder
     * Note: This still maintains technical debt as the application logic may need updates
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
