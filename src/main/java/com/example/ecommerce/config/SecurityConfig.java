package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration with intentional security issues (technical debt)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * TECHNICAL DEBT: Using deprecated WebSecurityConfigurerAdapter
     * Should use SecurityFilterChain in newer versions
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
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