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
     * TECHNICAL DEBT: Using SecurityFilterChain (modernized from WebSecurityConfigurerAdapter)
     * Should use SecurityFilterChain in newer versions
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // TECHNICAL DEBT: Disabling CSRF protection completely
            .csrf(csrf -> csrf.disable())
            
            // TECHNICAL DEBT: Allowing all requests without authentication
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().permitAll()
            )
            
            // TECHNICAL DEBT: Disabling frame options for H2 console (security risk)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

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
