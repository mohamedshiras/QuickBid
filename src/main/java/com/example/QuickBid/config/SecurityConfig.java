package com.example.QuickBid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // Allow authentication endpoints
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/*.html").permitAll() // Allow static resources
                        .requestMatchers("/", "/index.html", "/login.html", "/signup.html").permitAll() // Allow public pages
                        .anyRequest().authenticated() // Require authentication for other endpoints
                )
                .formLogin(form -> form.disable()) // Disable default form login
                .httpBasic(httpBasic -> httpBasic.disable()); // Disable HTTP Basic auth

        return http.build();
    }
}